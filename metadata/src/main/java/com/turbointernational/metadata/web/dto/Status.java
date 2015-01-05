package com.turbointernational.metadata.web.dto;

import flexjson.JSONSerializer;
import flexjson.transformer.ObjectTransformer;

/**
 *
 * @author jrodriguez
 */
public class Status {
    
    private boolean bomRebuilding = false;

    public boolean isBomRebuilding() {
        return bomRebuilding;
    }

    public void setBomRebuilding(boolean bomRebuilding) {
        this.bomRebuilding = bomRebuilding;
    }
    
    public String toJson() {
        return new JSONSerializer()
            .transform(new ObjectTransformer(), this.getClass())
            .exclude("class")
            .serialize(this);
    }
    
}
