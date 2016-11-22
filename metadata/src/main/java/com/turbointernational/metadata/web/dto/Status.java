package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;
import flexjson.JSONSerializer;
import flexjson.transformer.ObjectTransformer;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

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

    /*
    public String toJson() {
        return new JSONSerializer()
            .transform(new ObjectTransformer(), this.getClass())
            .exclude("class")
            .serialize(this);
    }
    */
    
}
