package com.turbointernational.metadata.service;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbointernational.metadata.Auditable;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Manufacturer;
import com.turbointernational.metadata.web.dto.Part;
import com.turbointernational.metadata.web.dto.PartType;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class ArangoDbConnectorService {

    private final static Logger log = LoggerFactory.getLogger(ArangoDbConnectorService.class);

    @Value("${rest.arangodb.service.protocol}")
    private String restArangoDbServiceProtocol;

    @Value("${rest.arangodb.service.host}")
    private String restArangoDbServiceHost;

    @Value("${rest.arangodb.service.port}")
    private Integer restArangoDbServicePort;

    @Autowired
    private RestTemplate restArangoDbService;

    @Autowired
    private ObjectMapper jsonSerializer;

    private HttpHeaders headers;

    private UriTemplate uriTmplGetPartById;

    private UriTemplate uriTmplGetInterchageById;

    private UriTemplate uriTmplGetInterchageForPart;

    private UriTemplate uriTmplLeaveGroup;

    private UriTemplate uriTmplMergePickedAllToPart;

    private UriTemplate uriTmplMovePartToOtherGroup;

    private UriTemplate uriTmplGetAncestors;

    private UriTemplate uriTmplGetBoms;

    private UriTemplate uriTmplModifyBom;

    private UriTemplate uriTmplRemovePartFromBom;

    private UriTemplate uriTmplGetParentBoms;

    private UriTemplate uriTmplGetAltBoms;

    private UriTemplate uriTmplCreateAltBom;

    private UriTemplate uriTmplDeleteAltBom;

    private UriTemplate uriTmplCreateAltBomGroup;

    private UriTemplate uriTmplDeleteAltBomGroup;

    public static class Response {

        private boolean success;

        private String msg;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }

    public static class GetManufacturerResponse extends Response {

        private Long id;

        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class GetPartTypeResponse extends Response {

        private Long id;

        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class GetPartResponse extends Response {

        private Long partId;

        private String name;

        private String descritpion;

        private String partNumber;

        private GetPartTypeResponse partType;

        private GetManufacturerResponse manufacturer;

        public Long getPartId() {
            return partId;
        }

        public void setPartId(Long partId) {
            this.partId = partId;
        }

        public String getPartNumber() {
            return partNumber;
        }

        public void setManufacturerPartNumber(String partNumber) {
            this.partNumber = partNumber;
        }

        public GetPartTypeResponse getPartType() {
            return partType;
        }

        public void setPartType(GetPartTypeResponse partType) {
            this.partType = partType;
        }

        public GetManufacturerResponse getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(GetManufacturerResponse manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescritpion() {
            return descritpion;
        }

        public void setDescritpion(String descritpion) {
            this.descritpion = descritpion;
        }

    }

    public static class GetInterchangeResponse extends Response {

        private Long headerId; // interchange header ID

        private GetPartResponse[] parts;

        public GetInterchangeResponse() {
        }

        public GetInterchangeResponse(Long headerId, GetPartResponse[] parts) {
            this.headerId = headerId;
            this.parts = parts;
        }

        public Long getHeaderId() {
            return headerId;
        }

        public void setHeaderId(Long headerId) {
            this.headerId = headerId;
        }

        public GetPartResponse[] getParts() {
            return parts;
        }

        public void setParts(GetPartResponse[] parts) {
            this.parts = parts;
        }

    }

    public static class CreateInterchangeResponse extends Response {

        private Long headerId;

        public Long getHeaderId() {
            return headerId;
        }

        public void setHeaderId(Long headerId) {
            this.headerId = headerId;
        }

    }

    public static class MigrateInterchangeResponse extends Response {

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

    public static class GetAncestorsResponse extends Response {

        @JsonInclude(JsonInclude.Include.ALWAYS)
        public static class Row {

            @JsonView({ View.Summary.class })
            private Long partId;

            @JsonView({ View.Summary.class })
            private String name;

            @JsonView({ View.Summary.class })
            private String descritption;

            @JsonView({ View.Summary.class })
            private String partNumber;

            @JsonView({ View.Summary.class })
            private Manufacturer manufacturer;

            @JsonView({ View.Summary.class })
            private PartType partType;

            @JsonView({ View.Summary.class })
            private boolean relationType;

            @JsonView({ View.Summary.class })
            private int relationDistance;

            public Long getPartId() {
                return partId;
            }

            public void setPartId(Long partId) {
                this.partId = partId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescritption() {
                return descritption;
            }

            public void setDescritption(String descritption) {
                this.descritption = descritption;
            }

            public String getPartNumber() {
                return partNumber;
            }

            public void setPartNumber(String partNumber) {
                this.partNumber = partNumber;
            }

            public Manufacturer getManufacturer() {
                return manufacturer;
            }

            public void setManufacturer(Manufacturer manufacturer) {
                this.manufacturer = manufacturer;
            }

            public PartType getPartType() {
                return partType;
            }

            public void setPartType(PartType partType) {
                this.partType = partType;
            }

            public boolean isRelationType() {
                return relationType;
            }

            public void setRelationType(boolean relationType) {
                this.relationType = relationType;
            }

            public int getRelationDistance() {
                return relationDistance;
            }

            public void setRelationDistance(int relationDistance) {
                this.relationDistance = relationDistance;
            }

        }

        private Row[] rows;

        public Row[] getRows() {
            return rows;
        }

        public GetAncestorsResponse() {
        }

        public GetAncestorsResponse(Row[] rows) {
            this.rows = rows;
        }

        public void setRows(Row[] rows) {
            this.rows = rows;
        }

    }

    public static class GetBomsResponse extends Response {

        public static class Row implements Auditable {

            private Long partId;

            private String partNumber;

            private PartType partType;

            private Manufacturer manufacturer;

            private Integer qty;

            private Part[] interchanges;

            public Long getPartId() {
                return partId;
            }

            public void setPartId(Long partId) {
                this.partId = partId;
            }

            public String getPartNumber() {
                return partNumber;
            }

            public void setPartNumber(String partNumber) {
                this.partNumber = partNumber;
            }

            public PartType getPartType() {
                return partType;
            }

            public void setPartType(PartType partType) {
                this.partType = partType;
            }

            public Manufacturer getManufacturer() {
                return manufacturer;
            }

            public void setManufacturer(Manufacturer manufacturer) {
                this.manufacturer = manufacturer;
            }

            public Integer getQty() {
                return qty;
            }

            public void setQty(Integer qty) {
                this.qty = qty;
            }

            public Part[] getInterchanges() {
                return interchanges;
            }

            public void setInterchanges(Part[] interchanges) {
                this.interchanges = interchanges;
            }

            @Override
            public String toString() {
                return "Row [partId=" + partId + ", partNumber=" + partNumber + ", partType=" + partType
                        + ", manufacturer=" + manufacturer + ", qty=" + qty + ", interchanges="
                        + Arrays.toString(interchanges) + "]";
            }

            @Override
            public String toAuditLog() {
                return null;
            }

        }

        private Row[] rows;

        public GetBomsResponse() {
        }

        public GetBomsResponse(Row[] rows) {
            this.setRows(rows);
        }

        public Row[] getRows() {
            return rows;
        }

        public void setRows(Row[] rows) {
            this.rows = rows;
        }

    }

    public static class GetAltBomsResponse extends Response {

        public static class Group {

            private Long altHeaderId;

            public Long getAltHeaderId() {
                return altHeaderId;
            }

            public void setAltHeaderId(Long headerId) {
                this.altHeaderId = headerId;
            }

            private GetPartResponse[] parts;

            public GetPartResponse[] getParts() {
                return parts;
            }

            public void setParts(GetPartResponse[] parts) {
                this.parts = parts;
            }

        }

        private Group[] groups;

        public Group[] getGroups() {
            return groups;
        }

        public void setGroups(Group[] groups) {
            this.groups = groups;
        }

    }

    public static class CreateAltBomResponse extends Response {

        private Long altHeaderId;

        public Long getAltHeaderId() {
            return altHeaderId;
        }

        public void setAltHeaderId(Long altHeaderId) {
            this.altHeaderId = altHeaderId;
        }

    };

    public static class DeleteAltBomResponse extends Response {

        private GetAltBomsResponse.Group[] groups;

        public GetAltBomsResponse.Group[] getGroups() {
            return groups;
        }

        public void setGroups(GetAltBomsResponse.Group[] groups) {
            this.groups = groups;
        }

    }

    public static class CreateAltBomGroupResponse extends Response {

        private Long altHeaderId;

        public Long getAltHeaderId() {
            return altHeaderId;
        }

        public void setAltHeaderId(Long altHeaderId) {
            this.altHeaderId = altHeaderId;
        }

    }

    public static class DeleteAltBomGroupResponse extends Response {

        private GetAltBomsResponse.Group[] groups;

        public GetAltBomsResponse.Group[] getGroups() {
            return groups;
        }

        public void setGroups(GetAltBomsResponse.Group[] groups) {
            this.groups = groups;
        }

    }

    @PostConstruct
    public void init() {
        headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        uriTmplGetPartById = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/parts/{id}");
        uriTmplGetInterchageById = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/interchanges/{id}");
        uriTmplGetInterchageForPart = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost
                + ":" + restArangoDbServicePort + "/parts/{partId}/interchanges");
        uriTmplLeaveGroup = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/interchanges/{partId}/leave_group");
        uriTmplMergePickedAllToPart = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost
                + ":" + restArangoDbServicePort + "/interchanges/{partId}/merge_group/{pickedPartId}/all");
        uriTmplMovePartToOtherGroup = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost
                + ":" + restArangoDbServicePort + "/interchanges/{partId}/merge_group/{pickedPartId}");
        uriTmplGetAncestors = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/parts/{partId}/ancestors");
        uriTmplGetBoms = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/parts/{partId}/boms");
        uriTmplModifyBom = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/boms/{parentPartId}/descendant/{descendantPartId}");
        uriTmplRemovePartFromBom = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/boms/{parentPartId}/descendant/{descendantPartId}");
        uriTmplGetParentBoms = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/parts/{partId}/boms/parents");
        uriTmplGetAltBoms = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/boms/{parentPartId}/children/{childPartId}/alternatives");
        uriTmplCreateAltBom = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/boms/{parentPartId}/children/{childPartId}/alternatives/parts/{partId}");
        uriTmplDeleteAltBom = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/boms/alternatives/{altHeaderId}/parts/{partId}");
        uriTmplCreateAltBomGroup = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/boms/{parentPartId}/children/{childPartId}/alternatives");
        uriTmplDeleteAltBomGroup = new UriTemplate(restArangoDbServiceProtocol + "://" + restArangoDbServiceHost + ":"
                + restArangoDbServicePort + "/boms/{parentPartId}/children/{childPartId}/alternatives/{altHeaderId}");

    }

    /**
     * Check response from ArangoDb server.
     *
     * If response signals that call to a GraphDb service was not successful the exception is thrown.
     *
     * @param response
     * @throws DataAccessResourceFailureException
     */
    public static void checkSuccess(Response response) throws DataAccessResourceFailureException {
        if (!response.isSuccess()) {
            log.warn("Call to GraphDb searvice returns an error: " + response.getMsg());
            throw new DataAccessResourceFailureException(response.getMsg());
        }
    }

    /**
     * A general method to do GET request to a GraphDb service.
     *
     * The method also do check of response and if it is not successful an exception is thrown.
     *
     * @param uri
     * @param responseClazz
     * @return
     * @throws DataAccessResourceFailureException
     */
    private <T extends Response> T get(URI uri, Class<T> responseClazz) {
        T response = restArangoDbService.getForObject(uri, responseClazz);
        // checkSuccess(response);
        return response;
    }

    private <T extends Response> T exchange(URI uri, HttpMethod method, Object body, Class<T> responseClazz)
            throws JsonProcessingException {
        HttpEntity<String> requestEntity = null;
        if (body != null) {
            String s = jsonSerializer.writeValueAsString(body);
            requestEntity = new HttpEntity<>(s, headers);
        }
        ResponseEntity<T> responseEntity = restArangoDbService.exchange(uri, method, requestEntity, responseClazz);
        T response = responseEntity.getBody();
        return response;
    }

    private <T extends Response> T post(URI uri, Class<T> responseClazz) throws JsonProcessingException {
        return exchange(uri, POST, null, responseClazz);
    }

    private <T extends Response> T post(URI uri, Object body, Class<T> responseClazz) throws JsonProcessingException {
        return exchange(uri, POST, body, responseClazz);
    }

    private Response post(URI uri, Object body) throws JsonProcessingException {
        return post(uri, body, Response.class);
    }

    private <T extends Response> T put(URI uri, Object body, Class<T> responseClazz) throws JsonProcessingException {
        return exchange(uri, PUT, body, responseClazz);
    }

    private <T extends Response> T put(URI uri, Class<T> responseClazz) throws JsonProcessingException {
        return put(uri, null, responseClazz);
    }

    private Response put(URI uri, Object body) throws JsonProcessingException {
        return put(uri, body, Response.class);
    }

    private <T extends Response> T delete(URI uri, Object body, Class<T> responseClazz) throws JsonProcessingException {
        return exchange(uri, DELETE, body, responseClazz);
    }

    private <T extends Response> T delete(URI uri, Class<T> responseClazz) throws JsonProcessingException {
        return delete(uri, null, responseClazz);
    }

    private Response delete(URI uri) throws JsonProcessingException {
        return delete(uri, Response.class);
    }

    public GetPartResponse findPartById(Long id) {
        URI uri = uriTmplGetPartById.expand(id);
        return get(uri, GetPartResponse.class);
    }

    public GetInterchangeResponse findInterchangeById(Long id) {
        URI uri = uriTmplGetInterchageById.expand(id);
        return get(uri, GetInterchangeResponse.class);
    }

    public GetInterchangeResponse findInterchangeForPart(Long partId) {
        URI uri = uriTmplGetInterchageForPart.expand(partId);
        return get(uri, GetInterchangeResponse.class);
    }

    public MigrateInterchangeResponse leaveInterchangeableGroup(Long partId) throws JsonProcessingException {
        URI uri = uriTmplLeaveGroup.expand(partId);
        return put(uri, MigrateInterchangeResponse.class);
    }

    public MigrateInterchangeResponse moveGroupToOtherInterchangeableGroup(Long srcPartId, Long trgPartId)
            throws JsonProcessingException {
        return migrateInterchange(uriTmplMergePickedAllToPart, trgPartId, srcPartId);
    }

    public MigrateInterchangeResponse movePartToOtherInterchangeGroup(Long srcPartId, Long trgPartId)
            throws JsonProcessingException {
        return migrateInterchange(uriTmplMovePartToOtherGroup, trgPartId, srcPartId);
    }

    private MigrateInterchangeResponse migrateInterchange(UriTemplate uriTmpl, Long... partIds)
            throws JsonProcessingException {
        URI uri;
        int n = partIds.length;
        switch (n) {
        case 1:
            uri = uriTmpl.expand(partIds[0]);
            break;
        case 2:
            uri = uriTmpl.expand(partIds[0], partIds[1]);
            break;
        default:
            throw new IllegalArgumentException("Unexpected number of optional arguments: " + n);
        }
        return put(uri, MigrateInterchangeResponse.class);
    }

    public GetAncestorsResponse getAncestors(Long partId) {
        URI uri = uriTmplGetAncestors.expand(partId);
        GetAncestorsResponse.Row[] rows = restArangoDbService.getForObject(uri, GetAncestorsResponse.Row[].class);
        return new GetAncestorsResponse(rows);
    }

    public GetBomsResponse getBoms(Long partId) {
        URI uri = uriTmplGetBoms.expand(partId);
        GetBomsResponse.Row[] rows = restArangoDbService.getForObject(uri, GetBomsResponse.Row[].class);
        return new GetBomsResponse(rows);
    }

    public GetBomsResponse getParentsBoms(Long partId) {
        URI uri = uriTmplGetParentBoms.expand(partId);
        GetBomsResponse.Row[] rows = restArangoDbService.getForObject(uri, GetBomsResponse.Row[].class);
        return new GetBomsResponse(rows);
    }

    /**
     * Button on the UI "Add Child".
     *
     * @return
     * @throws JsonProcessingException
     * @throws DataAccessResourceFailureException
     */
    public Response addPartToBom(Long parentPartId, Long childPartId, Integer quantity) throws JsonProcessingException {
        URI uri = uriTmplModifyBom.expand(parentPartId, childPartId);
        Map<String, Integer> body = new HashMap<>();
        body.put("qty", quantity);
        return post(uri, body);
    }

    /**
     * @return
     * @throws JsonProcessingException
     * @throws DataAccessResourceFailureException
     */
    public Response modifyPartInBom(Long parentPartId, Long childPartId, Integer quantity)
            throws JsonProcessingException {
        URI uri = uriTmplModifyBom.expand(parentPartId, childPartId);
        Map<String, Integer> body = new HashMap<>();
        body.put("qty", quantity);
        return put(uri, body);
    }

    /**
     * @param parentPartId
     * @param childPartId
     * @return
     * @throws JsonProcessingException
     * @throws DataAccessResourceFailureException
     */
    public Response removePartFromBom(Long parentPartId, Long childPartId) throws JsonProcessingException {
        URI uri = uriTmplRemovePartFromBom.expand(parentPartId, childPartId);
        return delete(uri);
    }

    public GetAltBomsResponse.Group[] getAltBoms(Long parentPartId, Long childPartId) {
        URI uri = uriTmplGetAltBoms.expand(parentPartId, childPartId);
        return restArangoDbService.getForObject(uri, GetAltBomsResponse.Group[].class);
    }

    public CreateAltBomResponse createAltBom(Long parentPartId, Long childPartId, Long altHeaderId, Long altPartId)
            throws JsonProcessingException {
        URI uri = uriTmplCreateAltBom.expand(parentPartId, childPartId, altPartId);
        Map<String, Long> body = new HashMap<>();
        body.put("altHeaderId", altHeaderId);
        return post(uri, body, CreateAltBomResponse.class);
    }

    public DeleteAltBomResponse deleteAltBom(Long altHeaderId, Long altPartId) throws JsonProcessingException {
        URI uri = uriTmplDeleteAltBom.expand(altHeaderId, altPartId);
        return delete(uri, DeleteAltBomResponse.class);
    }

    public CreateAltBomGroupResponse createAltBomGroup(Long parentPartId, Long childPartId)
            throws JsonProcessingException {
        URI uri = uriTmplCreateAltBomGroup.expand(parentPartId, childPartId);
        return post(uri, CreateAltBomGroupResponse.class);
    }

    // TODO: parameters 'parentPartId' and 'childPartId' are excessive and useless
    public DeleteAltBomGroupResponse deleteAltBomGroup(Long parentPartId, Long childPartId, Long altHeaderId)
            throws JsonProcessingException {
        URI uri = uriTmplDeleteAltBomGroup.expand(parentPartId, childPartId, altHeaderId);
        return delete(uri, DeleteAltBomGroupResponse.class);
    }

}
