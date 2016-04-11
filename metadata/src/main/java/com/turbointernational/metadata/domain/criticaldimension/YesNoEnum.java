package com.turbointernational.metadata.domain.criticaldimension;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;

/**
 * Created by dmytro.trunykov on 4/11/16.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum YesNoEnum {

    NO("No", false), YES("Yes", true);

    @JsonView(View.Summary.class)
    @JsonProperty("name")
    public final String name;

    @JsonView(View.Summary.class)
    @JsonProperty("value")
    public final boolean value;

    YesNoEnum(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

}
