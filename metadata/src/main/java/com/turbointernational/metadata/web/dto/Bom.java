package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class Bom {

    @JsonView({ View.Summary.class })
    private Part part;

    @JsonView({ View.Summary.class })
    private Integer qty;

    @JsonView({ View.Summary.class })
    private Part[] interchanges;

    public Bom() {
    }

    public Bom(Part part, Integer qty, Part[] interchanges) {
        this.setPart(part);
        this.qty = qty;
        this.interchanges = interchanges;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
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
