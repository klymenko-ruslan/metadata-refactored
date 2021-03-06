package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.service.GraphDbService.GetAltBomsResponse;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class PartGroup {

    /**
     * Group ID.
     */
    @JsonView(View.Summary.class)
    protected Long id;

    @JsonView(View.Summary.class)
    protected Part[] parts;

    public PartGroup() {
    }

    public PartGroup(Long id, Part[] parts) {
        this.id = id;
        this.parts = parts;
    }

    public static PartGroup[] from(PartDao partDao, GetAltBomsResponse.Group[] groups) {
        int n = groups.length;
        PartGroup[] retVal = new PartGroup[n];
        for (int i = 0; i < n; i++) {
            GetAltBomsResponse.Group g = groups[i];
            Long altHeaderId = g.getAltHeaderId();
            Part[] parts = Part.from(partDao, g.getParts());
            retVal[i] = new PartGroup(altHeaderId, parts);
        }
        return retVal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Part[] getParts() {
        return parts;
    }

    public void setParts(Part[] parts) {
        this.parts = parts;
    }

}
