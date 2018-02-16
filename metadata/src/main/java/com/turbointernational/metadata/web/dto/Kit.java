package com.turbointernational.metadata.web.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@JsonInclude(ALWAYS)
public class Kit extends Part {

    @JsonView({ View.Summary.class })
    private KitType kitType;

    public Kit() {
        super();
    }

    public Kit(Long partId, String name, String description, String partNumber, PartType partType,
            Manufacturer manufacturer, boolean inactive, KitType kitType) {
        super(partId, name, description, partNumber, partType, manufacturer, inactive);
        this.setKitType(kitType);
    }

    public Kit(Long partId, String partNumber, KitType kitType) {
        super(partId, partNumber);
        this.setKitType(kitType);
    }

    public KitType getKitType() {
        return kitType;
    }

    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }

}
