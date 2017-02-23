package com.turbointernational.metadata.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

@JsonInclude(ALWAYS)
public class BOMAncestor implements Serializable {

    @JsonView(View.Summary.class)
    private final long partId;

    @JsonView(View.Summary.class)
    private final String partTypeName;

    @JsonView(View.Summary.class)
    private final String manufacturerName;

    @JsonView(View.Summary.class)
    private final String partNumber;

    @JsonView(View.Summary.class)
    private final String relationType;

    @JsonView(View.Summary.class)
    private int distance;

    public BOMAncestor(long partId, String partTypeName, String manufacturerName, String partNumber, String relationType,
                       int distance) {
        this.partId = partId;
        this.partTypeName = partTypeName;
        this.manufacturerName = manufacturerName;
        this.partNumber = partNumber;
        this.relationType = relationType;
        this.distance = distance;
    }

    public long getPartId() {
        return partId;
    }

    public String getPartTypeName() {
        return partTypeName;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public String getRelationType() {
        return relationType;
    }

    public int getDistance() {
        return distance;
    }

}
