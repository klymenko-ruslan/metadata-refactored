package com.turbointernational.metadata.web.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 *
 * @author jrodriguez
 */
@JsonInclude(ALWAYS)
public class Status {

    @JsonView(View.Summary.class)
    private boolean bomRebuilding = false;

    public boolean isBomRebuilding() {
        return bomRebuilding;
    }

    public void setBomRebuilding(boolean bomRebuilding) {
        this.bomRebuilding = bomRebuilding;
    }

}
