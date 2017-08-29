package com.turbointernational.metadata.web.dto;

import com.turbointernational.metadata.service.ArangoDbConnectorService;

/**
 * @author dmytro.trunykov@gmail.com
 */
public class AltBom {

    private Long headerId;

    private Part[] parts;

    public AltBom() {
    }

    public AltBom(Long headerId, Part[] parts) {
        this.headerId = headerId;
        this.parts = parts;
    }

    public static AltBom from(ArangoDbConnectorService.GetAltBomsResponse response) {
        Part[] parts = Part.from(response.getParts());
        return new AltBom(response.getHeaderId(), parts);
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public Part[] getParts() {
        return parts;
    }

    public void setParts(Part[] parts) {
        this.parts = parts;
    }

}
