package com.turbointernational.metadata.web.dto;

import flexjson.JSONSerializer;
import flexjson.transformer.ObjectTransformer;

/**
 *
 * @author jrodriguez
 */
public class Status {
    
    private boolean bomAncestryRebuilding = false;

    public boolean getBomAncestryRebuilding() {
        return bomAncestryRebuilding;
    }

    public void setBomAncestryRebuilding(boolean bomAncestryRebuilding) {
        this.bomAncestryRebuilding = bomAncestryRebuilding;
    }
    
    public String toJson() {
        return new JSONSerializer()
            .transform(new ObjectTransformer(), this.getClass())
            .exclude(".class")
            .serialize(this);
    }
    
}
