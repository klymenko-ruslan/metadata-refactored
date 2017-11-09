package com.turbointernational.metadata.web.dto;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.collections.comparators.ComparatorChain;

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

    private static Comparator<Ancestor> cmpDistance = (a0, a1) -> {return a0.getRelationDistance() - a1.getRelationDistance();};
    private static Comparator<Ancestor> cmpRelationType = (a0, a1) -> /* reverse */ a1.getRelationType().compareTo(a0.getRelationType());
    private static Comparator<Ancestor> cmpPartNumber = (a0, a1) -> a0.getPart().getPartNumber().compareTo(a1.getPart().getPartNumber());

    @SuppressWarnings("unchecked")
    public final static Comparator<Ancestor> cmpComplex = new ComparatorChain(Arrays.asList(cmpDistance, cmpRelationType, cmpPartNumber));

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
