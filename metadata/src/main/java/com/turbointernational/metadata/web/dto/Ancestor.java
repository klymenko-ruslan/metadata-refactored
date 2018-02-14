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
    private Interchange interchange;

    @JsonView({ View.Summary.class })
    private int relationDistance;

    /**
     * Relation type: true - direct false - interchange
     */
    @JsonView({ View.Summary.class })
    private boolean relationType;

    public Ancestor() {
    }

    public Ancestor(Part part, Interchange interchange, int relationDistance, boolean isDirect) {
        this.setPart(part);
        this.setInterchange(interchange);
        this.setRelationDistance(relationDistance);
        this.setRelationType(isDirect);
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

    public boolean getRelationType() {
        return isRelationType();
    }

    public void setRelationType(boolean relationType) {
        this.relationType = relationType;
    }

    public Interchange getInterchange() {
        return interchange;
    }

    public void setInterchange(Interchange interchange) {
        this.interchange = interchange;
    }

    public boolean isRelationType() {
        return relationType;
    }

}
