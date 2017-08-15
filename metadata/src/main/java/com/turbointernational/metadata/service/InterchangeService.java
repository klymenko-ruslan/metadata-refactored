package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.INTERCHANGE;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART0;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART1;
import static com.turbointernational.metadata.util.FormatUtils.formatInterchange;
import static com.turbointernational.metadata.util.FormatUtils.formatPart;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.PUT;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import com.turbointernational.metadata.web.dto.Interchange;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-11-02.
 */
@Service
public class InterchangeService {

    @Autowired
    private BOMService bomService;

    @Autowired
    private PartDao partDao;

    /*
     * @Autowired private InterchangeDao interchangeDao;
     */

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private ChangelogSourceService changelogSourceService;

    @Qualifier("transactionManagerMetadata")
    @Autowired
    private PlatformTransactionManager txManagerMetadata;

    @Autowired
    private PartChangeService partChangeService;

    @Value("${rest.arangodb.service.protocol}")
    private String restArangoDbServiceProtocol;

    @Value("${rest.arangodb.service.host}")
    private String restArangoDbServiceHost;

    @Value("${rest.arangodb.service.port}")
    private Integer restArangoDbServicePort;

    private RestTemplate restArangoDbService;

    private UriTemplate uriTmplArangoDbServiceGetInterchageById;

    private UriTemplate uriTmplArangoDbServiceGetInterchageForPart;

    private UriTemplate uriTmplArangoDbServiceLeaveGroup;

    private UriTemplate uriTmplMergePickedAllToPart;

    private UriTemplate uriTmplMovePartToOtherGroup;

    public static class GetInterchangeResponse {
        private Long headerId; // interchange header ID
        private Long[] parts; // parts IDs

        public GetInterchangeResponse() {
        }

        public GetInterchangeResponse(Long headerId, Long[] parts) {
            this.headerId = headerId;
            this.parts = parts;
        }

        public Long getHeaderId() {
            return headerId;
        }

        public void setHeaderId(Long headerId) {
            this.headerId = headerId;
        }

        public Long[] getParts() {
            return parts;
        }

        public void setParts(Long[] parts) {
            this.parts = parts;
        }
    }

    public static class CreateInterchangeResponse {

        private Long headerId;

        public Long getHeaderId() {
            return headerId;
        }

        public void setHeaderId(Long headerId) {
            this.headerId = headerId;
        }

    }

    public static class MigrateInterchangeResponse {

        private Long oldHeaderId;

        private Long newHeaderId;

        public Long getOldHeaderId() {
            return oldHeaderId;
        }

        public void setOldHeaderId(Long oldHeaderId) {
            this.oldHeaderId = oldHeaderId;
        }

        public Long getNewHeaderId() {
            return newHeaderId;
        }

        public void setNewHeaderId(Long newHeaderId) {
            this.newHeaderId = newHeaderId;
        }

    }

    @PostConstruct
    public void init() {
        restArangoDbService = new RestTemplate();
        uriTmplArangoDbServiceGetInterchageById = new UriTemplate(restArangoDbServiceProtocol + "://"
                + restArangoDbServiceHost + ":" + restArangoDbServicePort + "/interchanges/{id}");
        uriTmplArangoDbServiceGetInterchageForPart = new UriTemplate(restArangoDbServiceProtocol + "://"
                + restArangoDbServiceHost + ":" + restArangoDbServicePort + "/parts/{partId}/interchanges");
        uriTmplArangoDbServiceLeaveGroup = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost
                + ":" + restArangoDbServicePort + "/interchanges/{partId}/leave_group");
        uriTmplMergePickedAllToPart = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost
                + ":" + restArangoDbServicePort + "/interchanges/{partId}/merge_group/{pickedPartId}/all");
        uriTmplMovePartToOtherGroup = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost
                + ":" + restArangoDbServicePort + "/interchanges/{partId}/merge_group/{pickedPartId}");
    }

    /**
     * Find an interchangeable by its ID.
     *
     * @param id
     * @return the found entity instance or null if the entity does not exist
     */
    @Transactional
    public Interchange findById(Long id) {
        URI uri = uriTmplArangoDbServiceGetInterchageById.expand(id);
        GetInterchangeResponse response = restArangoDbService.getForObject(uri, GetInterchangeResponse.class);
        Set<Part> parts = new HashSet<>();
        Optional.of(response.getParts()).ifPresent(partIds -> {
            for (Long pid : partIds) {
                Part p = partDao.findOne(pid);
                parts.add(p);
            }
        });
        return new Interchange(id, parts);
    }

    public Interchange findForPart(Part part) {
        return findForPart(part.getId());
    }

    public Interchange findForPart(Long partId) {
        URI uri = uriTmplArangoDbServiceGetInterchageForPart.expand(partId);
        GetInterchangeResponse response = restArangoDbService.getForObject(uri, GetInterchangeResponse.class);
        Set<Part> parts = loadParts(response);
        return new Interchange(response.getHeaderId(), parts);
    }

    public void initInterchange(Part part) {
        Interchange interchange = findForPart(part);
        part.setInterchange(interchange);
    }

    /**
     * Persists interchangeable in a storage.
     */
    @Transactional
    @Deprecated
    public void create(HttpServletRequest httpRequest, List<Long> partIds, Long[] sourcesIds, Integer[] ratings,
            String description, Long[] attachIds) throws IOException {
        /*
         * // Link it with the Hibernate parts Set<Part> canonicalParts = new
         * HashSet<>(); // Map the incoming part IDs to their canonical part for
         * (Long partId : partIds) { Part canonicalPart =
         * partDao.findOne(partId); if (canonicalPart.getInterchange() != null)
         * { throw new IllegalArgumentException( "Part " +
         * formatPart(canonicalPart) + " already has interchangeable parts."); }
         * canonicalParts.add(canonicalPart); } Interchange interchange = new
         * Interchange(); interchange.getParts().addAll(canonicalParts);
         * interchangeDao.persist(interchange); List<RelatedPart> relatedParts =
         * new ArrayList<>(canonicalParts.size()); for (Part canonicalPart :
         * canonicalParts) { canonicalPart.setInterchange(interchange);
         * partDao.merge(canonicalPart); relatedParts.add(new
         * RelatedPart(canonicalPart.getId(), null)); } interchangeDao.flush();
         * // Update the changelog. Changelog changelog =
         * changelogService.log(INTERCHANGE, "Created interchange: " +
         * formatInterchange(interchange) + ".", interchange.toJson(),
         * relatedParts); changelogSourceService.link(httpRequest, changelog,
         * sourcesIds, ratings, description, attachIds);
         * bomService.rebuildBomDescendancyForParts(partIds, true);
         * partChangeService.changedInterchange(interchange.getId(), null);
         */
        throw new NotImplementedException();
    }

    /**
     * Set 'asInterchange' as interchange for the 'part'.
     *
     * Group of interchanges for the 'part' is removed and 'part' is added to a
     * group of the 'asInterchange' so 'asInterchange' becomes interchange for
     * the 'part'.
     *
     * @param part
     * @param asInterchange
     */
    public void create(Long partId, Long asInterchangePartId) throws IOException {
        Interchange interchange = moveGroupToOtherInterchangeableGroup(partId, asInterchangePartId);
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(asInterchangePartId, ChangelogPart.Role.PART1));
        changelogService.log(INTERCHANGE, "Created interchange: " + formatInterchange(interchange) + ".",
                interchange.toJson(), relatedParts);
        bomService.rebuildBomDescendancyForParts(asList(partId, asInterchangePartId), true);
        partChangeService.changedInterchange(asInterchangePartId, interchange.getId());
    }

    /**
     * Remove part from interchangeable group.
     *
     * Every part must belong to an interchangeable group. When part is removed
     * from an interchangeable group a new group should be created and this part
     * added to that group.
     *
     * @param partId
     *            ID of a part form remove.
     */
    @Transactional
    public void leaveInterchangeableGroup(Long partId) throws IOException {
        Part part = partDao.findOne(partId);
        Interchange interchange = findForPart(partId);
        URI uri = uriTmplArangoDbServiceLeaveGroup.expand(partId);
        ResponseEntity<MigrateInterchangeResponse> responseEntity = restArangoDbService.exchange(uri, PUT, null,
                MigrateInterchangeResponse.class);
        // Update the changelog.
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(partId, PART0));
        Long newInterchangeId = responseEntity.getBody().getNewHeaderId();
        changelogService.log(INTERCHANGE, "The part [" + formatPart(part) + "] migrated from interchange group ["
                + interchange.getId() + "] to [" + newInterchangeId + "].", relatedParts);
        bomService.rebuildBomDescendancyForPart(partId, true);
        bomService.rebuildBomDescendancyForParts(interchange.getParts().iterator(), true);
        partChangeService.changedInterchange(interchange.getId(), newInterchangeId);
    }

    /**
     * Add picked part to interchange group of this part and remove picked part
     * from its existing interchange.
     *
     * @param partId
     * @param pickedPartId
     */
    @Transactional
    public void mergePickedAloneToPart(HttpServletRequest httpRequest, long partId, long pickedPartId,
            Long[] sourcesIds, Integer[] ratings, String description, Long[] attachIds) throws IOException {
        Interchange interchange0 = findForPart(pickedPartId);
        Interchange interchange1 = movePartToOtherInterchangeGroup(pickedPartId, partId);
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Part pickedPart = partDao.findOne(pickedPartId);
        Part part = partDao.findOne(partId);
        Changelog changelog = changelogService.log(INTERCHANGE,
                "Part " + formatPart(pickedPart) + " added to the part " + formatPart(part) + " as interchange.",
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        bomService.rebuildBomDescendancyForPart(partId, true);
        bomService.rebuildBomDescendancyForPart(pickedPartId, true);
        partChangeService.changedInterchange(interchange0.getId(), interchange1.getId());
    }

    /**
     * Add this part to interchange group of the picked part and remove this
     * part from its existing interchange.
     *
     * @param partId
     * @param pickedPartId
     */
    @Transactional
    public void mergePartAloneToPicked(HttpServletRequest httpRequest, long partId, long pickedPartId,
            Long[] sourcesIds, Integer[] ratings, String description, Long[] attachIds) throws IOException {
        Interchange interchange0 = findForPart(partId);
        Interchange interchange1 = movePartToOtherInterchangeGroup(partId, pickedPartId);
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Part pickedPart = partDao.findOne(pickedPartId);
        Part part = partDao.findOne(partId);
        Changelog changelog = changelogService.log(INTERCHANGE,
                "Part " + formatPart(part) + " added to the part " + formatPart(pickedPart) + " as interchange.",
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        bomService.rebuildBomDescendancyForPart(partId, true);
        bomService.rebuildBomDescendancyForPart(pickedPartId, true);
        partChangeService.changedInterchange(interchange0.getId(), interchange1.getId());
    }

    /**
     * Add the picked part and all its existing interchange parts to interchange
     * group of this part.
     *
     * @param partId
     * @param pickedPartId
     */
    @Transactional
    public void mergePickedAllToPart(HttpServletRequest httpRequest, Long partId, Long pickedPartId, Long[] sourcesIds,
            Integer[] ratings, String description, Long[] attachIds) throws IOException {
        Interchange interchange0 = findForPart(partId);
        Interchange interchange1 = moveGroupToOtherInterchangeableGroup(pickedPartId, partId);
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Part pickedPart = partDao.findOne(pickedPartId);
        Part part = partDao.findOne(partId);
        Changelog changelog = changelogService.log(INTERCHANGE, "Part " + formatPart(pickedPart)
                + " and all its interchanges added to the part " + formatPart(part) + " as interchanges.",
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        bomService.rebuildBomDescendancyForPart(partId, true);
        bomService.rebuildBomDescendancyForPart(pickedPartId, true);
        partChangeService.changedInterchange(interchange0.getId(), interchange1.getId());
    }

    private Set<Part> loadParts(GetInterchangeResponse response) {
        return loadParts(response.getParts());
    }

    private Set<Part> loadParts(Long[] partIds) {
        Set<Part> parts = new HashSet<>();
        Optional.of(partIds).ifPresent(pids -> {
            for (Long pid : pids) {
                Part p = partDao.findOne(pid);
                parts.add(p);
            }
        });
        return parts;
    }

    private Interchange moveGroupToOtherInterchangeableGroup(Long srcPartId /* picked */, Long trgPartId) {
        migrateRestCall(uriTmplMergePickedAllToPart, trgPartId, srcPartId);
        return findForPart(trgPartId); // TODO: can be optimized if this info is
                                       // returned as result in the operation
                                       // response
    }

    private Interchange movePartToOtherInterchangeGroup(Long srcPartId /* picked */, Long trgPartId) {
        migrateRestCall(uriTmplMovePartToOtherGroup, trgPartId, srcPartId);
        return findForPart(trgPartId); // TODO: can be optimized if this info is
                                       // returned as result in the operation
                                       // response
    }

    private MigrateInterchangeResponse migrateRestCall(UriTemplate uriTmpl, Long... partIds) {
        ResponseEntity<MigrateInterchangeResponse> responseEntity;
        int n = partIds.length;
        switch (n) {
        case 1:
            responseEntity = restArangoDbService.exchange(uriTmpl.expand(partIds[0]), PUT, null,
                    MigrateInterchangeResponse.class);
            break;
        case 2:
            responseEntity = restArangoDbService.exchange(uriTmpl.expand(partIds[0], partIds[1]), PUT, null,
                    MigrateInterchangeResponse.class);
            break;
        default:
            throw new IllegalArgumentException("Unexpected number of optional arguments: " + n);
        }
        return responseEntity.getBody();
    }

}
