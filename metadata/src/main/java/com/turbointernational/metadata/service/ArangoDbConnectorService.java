package com.turbointernational.metadata.service;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.URI;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Manufacturer;
import com.turbointernational.metadata.web.dto.Part;
import com.turbointernational.metadata.web.dto.PartType;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class ArangoDbConnectorService {

    @Value("${rest.arangodb.service.protocol}")
    private String restArangoDbServiceProtocol;

    @Value("${rest.arangodb.service.host}")
    private String restArangoDbServiceHost;

    @Value("${rest.arangodb.service.port}")
    private Integer restArangoDbServicePort;

    private RestTemplate restArangoDbService;

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

        public static class Row {

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

    @PostConstruct
    public void init() {
        headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        restArangoDbService = new RestTemplate();
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
    }

    public GetPartResponse findPartById(Long id) {
        URI uri = uriTmplGetPartById.expand(id);
        return restArangoDbService.getForObject(uri, GetPartResponse.class);
    }

    public GetInterchangeResponse findInterchangeById(Long id) {
        URI uri = uriTmplGetInterchageById.expand(id);
        return restArangoDbService.getForObject(uri, GetInterchangeResponse.class);
    }

    public GetInterchangeResponse findInterchangeForPart(Long partId) {
        URI uri = uriTmplGetInterchageForPart.expand(partId);
        return restArangoDbService.getForObject(uri, GetInterchangeResponse.class);
    }

    public MigrateInterchangeResponse leaveInterchangeableGroup(Long partId) {
        URI uri = uriTmplLeaveGroup.expand(partId);
        return restArangoDbService.exchange(uri, PUT, null, MigrateInterchangeResponse.class).getBody();
    }

    public MigrateInterchangeResponse moveGroupToOtherInterchangeableGroup(Long srcPartId, Long trgPartId) {
        return migrateInterchange(uriTmplMergePickedAllToPart, trgPartId, srcPartId);
    }

    public MigrateInterchangeResponse movePartToOtherInterchangeGroup(Long srcPartId, Long trgPartId) {
        return migrateInterchange(uriTmplMovePartToOtherGroup, trgPartId, srcPartId);
    }

    private MigrateInterchangeResponse migrateInterchange(UriTemplate uriTmpl, Long... partIds) {
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

    /**
     * Button on the UI "Add Child".
     *
     * @return
     */
    public Response addPartToBom(Long parentPartId, Long childPartId, Integer quantity) {
        URI uri = uriTmplModifyBom.expand(parentPartId, childPartId);
        HttpEntity<String> body = new HttpEntity<>("{\"qty\":" + quantity + "}", headers);
        ResponseEntity<Response> response = restArangoDbService.exchange(uri, POST, body, Response.class);
        return response.getBody();
    }

    /**
     * @return
     */
    public Response modifyPartInBom(Long parentPartId, Long childPartId, Integer quantity) {
        URI uri = uriTmplModifyBom.expand(parentPartId, childPartId);
        //HttpHeaders hdrs = new HttpHeaders();
        //hdrs.add("Content-Type", "application/json");
        HttpEntity<String> body = new HttpEntity<>("{\"qty\":" + quantity + "}", /*hdrs*/ headers);
        ResponseEntity<Response> response = restArangoDbService.exchange(uri, PUT, body, Response.class);
        return response.getBody();
    }

    /**
     * @param parentPartId
     * @param childPartId
     * @return
     */
    public Response removePartFromBom(Long parentPartId, Long childPartId) {
        URI uri = uriTmplRemovePartFromBom.expand(parentPartId, childPartId);
        ResponseEntity<Response> response = restArangoDbService.exchange(uri, DELETE, null, Response.class);
        return response.getBody();
    }

}
