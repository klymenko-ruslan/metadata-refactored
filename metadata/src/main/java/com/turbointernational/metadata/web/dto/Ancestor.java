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
    private final Part part;

    @JsonView({ View.Summary.class })
    private final Interchange interchange;

    @JsonView({ View.Summary.class })
    private final int relationDistance;

    /**
     * Relation type: true - direct false - interchange
     */
    @JsonView({ View.Summary.class })
    private boolean relationType;

    public Ancestor(Part part, Interchange interchange, int relationDistance, boolean isDirect) {
        this.part = part;
        this.interchange = interchange;
        this.relationDistance = relationDistance;
        this.relationType = isDirect;
    }

    public Part getPart() {
        return part;
    }

    public int getRelationDistance() {
        return relationDistance;
    }

    public boolean getRelationType() {
        return relationType;
    }

    public Interchange getInterchange() {
        return interchange;
    }

}
