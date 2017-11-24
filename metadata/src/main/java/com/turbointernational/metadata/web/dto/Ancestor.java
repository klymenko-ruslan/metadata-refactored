package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Ancestor {

    @JsonView({ View.Summary.class })
    private Part part;

    @JsonView({ View.Summary.class })
    private int relationDistance;

    /**
     * Relation type:
     *  true - direct
     *  false  - interchange
     */
    @JsonView({ View.Summary.class })
    private Boolean relationType;

    public Ancestor() {
    }

    public Ancestor(Part part, int relationDistance, Boolean isDirect) {
        this.part = part;
        this.relationDistance = relationDistance;
        this.relationType = isDirect;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public int getRelationDistance() {
        return relationDistance;
    }

    public void setRelationDistance(int relationDistance) {
        this.relationDistance = relationDistance;
    }

    public Boolean getRelationType() {
        return relationType;
    }

    public void setRelationType(Boolean isInterchange) {
        this.relationType = isInterchange;
    }

}
