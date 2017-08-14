package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.INTERCHANGE;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART0;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART1;
import static com.turbointernational.metadata.util.FormatUtils.formatInterchange;
import static com.turbointernational.metadata.util.FormatUtils.formatPart;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.PUT;

import java.io.IOException;
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

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.part.Interchange;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;

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

    private String urlArangoDbServiceGetInterchageById;

    private String urlArangoDbServiceGetInterchageForPart;

    private String urlArangoDbServiceLeaveGroup;

    private String urlMergePickedAllToPart;

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
        urlArangoDbServiceGetInterchageById = restArangoDbServiceHost + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/interchanges/{}";
        urlArangoDbServiceGetInterchageForPart = restArangoDbServiceHost + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/parts/{}/interchanges";
        urlArangoDbServiceLeaveGroup = restArangoDbServiceHost + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/interchanges/{}/leave_group";
        urlMergePickedAllToPart = restArangoDbServiceHost + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/{}/merge_group/{}/all";

    }

    /**
     * Find an interchangeable by its ID.
     *
     * @param id
     * @return the found entity instance or null if the entity does not exist
     */
    @Transactional
    public Interchange findById(Long id) {
        GetInterchangeResponse response = restArangoDbService.getForObject(urlArangoDbServiceGetInterchageById,
                GetInterchangeResponse.class, id);
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
        GetInterchangeResponse response = restArangoDbService.getForObject(urlArangoDbServiceGetInterchageForPart,
                GetInterchangeResponse.class, partId);
        Set<Part> parts = loadParts(response);
        return new Interchange(response.getHeaderId(), parts);
    }

    private Set<Part> loadParts(GetInterchangeResponse response) {
        Set<Part> parts = new HashSet<>();
        Optional.of(response.getParts()).ifPresent(partIds -> {
            for (Long pid : partIds) {
                Part p = partDao.findOne(pid);
                parts.add(p);
            }
        });
        return parts;
    }

    /**
     * Persists interchangeable in a storage.
     */
    @Transactional
    @Deprecated
    public void create(HttpServletRequest httpRequest, List<Long> partIds, Long[] sourcesIds, Integer[] ratings,
            String description, Long[] attachIds) throws IOException {
        /*
        // Link it with the Hibernate parts
        Set<Part> canonicalParts = new HashSet<>();
        // Map the incoming part IDs to their canonical part
        for (Long partId : partIds) {
            Part canonicalPart = partDao.findOne(partId);
            if (canonicalPart.getInterchange() != null) {
                throw new IllegalArgumentException(
                        "Part " + formatPart(canonicalPart) + " already has interchangeable parts.");
            }
            canonicalParts.add(canonicalPart);
        }
        Interchange interchange = new Interchange();
        interchange.getParts().addAll(canonicalParts);
        interchangeDao.persist(interchange);
        List<RelatedPart> relatedParts = new ArrayList<>(canonicalParts.size());
        for (Part canonicalPart : canonicalParts) {
            canonicalPart.setInterchange(interchange);
            partDao.merge(canonicalPart);
            relatedParts.add(new RelatedPart(canonicalPart.getId(), null));
        }
        interchangeDao.flush();
        // Update the changelog.
        Changelog changelog = changelogService.log(INTERCHANGE,
                "Created interchange: " + formatInterchange(interchange) + ".", interchange.toJson(), relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        bomService.rebuildBomDescendancyForParts(partIds, true);
        partChangeService.changedInterchange(interchange.getId(), null);
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
    public void create(Part part, Part asInterchange) throws IOException {
        normalizePartInterchange(part);
        normalizePartInterchange(asInterchange);
        Long srcInterchangeId = part.getInterchange().getId();
        Interchange interchange = moveInterchangeableGroupToOtherGroup(part, asInterchange);
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(part.getId(), PART0));
        relatedParts.add(new RelatedPart(asInterchange.getId(), PART1));
        changelogService.log(INTERCHANGE, "Created interchange: " + formatInterchange(interchange) + ".",
                interchange.toJson(), relatedParts);
        bomService.rebuildBomDescendancyForParts(asList(part.getId(), asInterchange.getId()), true);
        partChangeService.changedInterchange(srcInterchangeId, interchange.getId());
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
        ResponseEntity<MigrateInterchangeResponse> responseEntity = restArangoDbService.exchange(
                urlArangoDbServiceLeaveGroup, PUT, null, MigrateInterchangeResponse.class, partId);
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
        Part part = partDao.findOne(partId);
        normalizePartInterchange(part);
        Long interchangeId0 = part.getInterchange().getId();
        Part pickedPart = partDao.findOne(pickedPartId);
        normalizePartInterchange(pickedPart);
        Long interchangeId1 = pickedPart.getInterchange().getId();
        movePartToOtherInterchangeGroup(pickedPart, part);
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Changelog changelog = changelogService.log(INTERCHANGE,
                "Part " + formatPart(pickedPart) + " added to the part " + formatPart(part) + " as interchange.",
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        bomService.rebuildBomDescendancyForPart(partId, true);
        bomService.rebuildBomDescendancyForPart(pickedPartId, true);
        partChangeService.changedInterchange(interchangeId0, interchangeId1);
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
        Part part = partDao.findOne(partId);
        normalizePartInterchange(part);
        Part pickedPart = partDao.findOne(pickedPartId);
        normalizePartInterchange(pickedPart);
        movePartToOtherInterchangeGroup(part, pickedPart);
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Changelog changelog = changelogService.log(INTERCHANGE,
                "Part " + formatPart(part) + " added to the part " + formatPart(pickedPart) + " as interchange.",
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        bomService.rebuildBomDescendancyForPart(partId, true);
        bomService.rebuildBomDescendancyForPart(pickedPartId, true);
        partChangeService.changedInterchange(part.getInterchange().getId(), pickedPart.getInterchange().getId());
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
        ResponseEntity<CreateInterchangeResponse> responseEntity = restArangoDbService.exchange(urlMergePickedAllToPart, PUT,
                null, CreateInterchangeResponse.class, partId, pickedPartId);
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Changelog changelog = changelogService.log(INTERCHANGE, "Part " + formatPart(pickedPart)
                + " and all its interchanges added to the part " + formatPart(part) + " as interchanges.",
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        bomService.rebuildBomDescendancyForPart(partId, true);
        bomService.rebuildBomDescendancyForPart(pickedPartId, true);
        partChangeService.changedInterchange(interchangId0, interchangeId1);
    }

}
