package com.turbointernational.metadata.service;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.StandardOversizePartDao;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-13.
 */
@Service
public class StandardOversizePartService {

    public static class CreateStandardOversizePartRequest {

        public enum TypeEnum {
            STANDARD, OVERSIZE
        }

        @JsonView(View.Summary.class)
        private TypeEnum type;

        @JsonView(View.Summary.class)
        private Long mainPartId;

        @JsonView(View.Summary.class)
        private List<Long> partIds;

        public TypeEnum getType() {
            return type;
        }

        public void setType(TypeEnum type) {
            this.type = type;
        }

        public Long getMainPartId() {
            return mainPartId;
        }

        public void setMainPartId(Long mainPartId) {
            this.mainPartId = mainPartId;
        }

        public List<Long> getPartIds() {
            return partIds;
        }

        public void setPartIds(List<Long> partIds) {
            this.partIds = partIds;
        }

    }

    @JsonInclude(ALWAYS)
    public static class CreateStandardOversizePartResponse {

        @JsonView(View.Summary.class)
        private List<Part> parts;

        public CreateStandardOversizePartResponse(List<Part> parts) {
            this.parts = parts;
        }

        public List<Part> getParts() {
            return parts;
        }

        public void setParts(List<Part> parts) {
            this.parts = parts;
        }

    }

    @Autowired
    private StandardOversizePartDao standardOversizePartDao;

    @Secured("ROLE_READ")
    public List<Part> findOversizeParts(Long partId) {
        return standardOversizePartDao.findOversizeParts(partId);
    }

    @Secured("ROLE_READ")
    public List<Part> findStandardParts(Long partId) {
        return standardOversizePartDao.findStandardParts(partId);
    }

    @Transactional
    @Secured("ROLE_ALTER_PART")
    public CreateStandardOversizePartResponse create(CreateStandardOversizePartRequest request) {
        CreateStandardOversizePartResponse retVal;
        for (Long partId : request.getPartIds()) {
            Long standardPartId, oversizePartId;
            switch (request.getType()) {
            case OVERSIZE:
                standardPartId = request.getMainPartId();
                oversizePartId = partId;
                break;
            case STANDARD:
                standardPartId = partId;
                oversizePartId = request.getMainPartId();
                break;
            default:
                throw new AssertionError("Unknown mode: " + request.getType());
            }
            standardOversizePartDao.create(standardPartId, oversizePartId);
        }
        List<Part> parts;
        switch (request.getType()) {
        case OVERSIZE:
            parts = standardOversizePartDao.findOversizeParts(request.getMainPartId());
            break;
        case STANDARD:
            parts = standardOversizePartDao.findStandardParts(request.getMainPartId());
            break;
        default:
            throw new AssertionError("Unknown mode: " + request.getType());
        }
        retVal = new CreateStandardOversizePartResponse(parts);
        return retVal;
    }

    @Transactional
    @Secured("ROLE_ALTER_PART")
    public void delete(Long standardPartId, Long oversizePartId) {
        standardOversizePartDao.delete(standardPartId, oversizePartId);
    }

}
