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
    private Kit kit;

    @JsonView({ View.CommonComponentPart.class })
    private Part part;

    @JsonView({ View.Summary.class })
    private Boolean exclude;

    public CommonComponent() {
    }

    public CommonComponent(Long id, Kit kit, Part part, Boolean exclude) {
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

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Boolean isExclude() {
        return exclude;
    }

    public void setExclude(Boolean exclude) {
        this.exclude = exclude;
    }

}
