package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CommonComponent {
    
    @JsonView({ View.Summary.class })
    private Long id;

    @JsonView({ View.CommonComponentKit.class })
    private Part kit;

    @JsonView({ View.CommonComponentPart.class})
    private Part part;

    @JsonView({ View.Summary.class })
    private boolean exclude;

    public CommonComponent() {
    }

    public CommonComponent(Long id, Part kit, Part part, boolean exclude) {
        this.setId(id);
        this.setKit(kit);
        this.setPart(part);
        this.setExclude(exclude);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Part getKit() {
        return kit;
    }

    public void setKit(Part kit) {
        this.kit = kit;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public boolean isExclude() {
        return exclude;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }

}
