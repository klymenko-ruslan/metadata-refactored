package com.turbointernational.metadata.service;

import static org.springframework.http.HttpMethod.PUT;

import java.net.URI;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

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

    private UriTemplate uriTmplGetInterchageById;

    private UriTemplate uriTmplGetInterchageForPart;

    private UriTemplate uriTmplLeaveGroup;

    private UriTemplate uriTmplMergePickedAllToPart;

    private UriTemplate uriTmplMovePartToOtherGroup;

    private UriTemplate uriTmplGetAncestors;

    private UriTemplate uriTmplGetBoms;

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

    public static class GetAncestorsResponse {

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
            private String manufacturer;

            @JsonView({ View.Summary.class })
            private String partType;

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

            public String getManufacturer() {
                return manufacturer;
            }

            public void setManufacturer(String manufacturer) {
                this.manufacturer = manufacturer;
            }

            public String getPartType() {
                return partType;
            }

            public void setPartType(String partType) {
                this.partType = partType;
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

    public static class GetBomsResponse {

        public static class Row {

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
        restArangoDbService = new RestTemplate();
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

}
