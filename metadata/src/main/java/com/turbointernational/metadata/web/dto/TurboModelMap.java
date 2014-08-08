
package com.turbointernational.metadata.web.dto;

/**
 *
 * @author jrodriguez
 */
public class TurboModelMap {

    String model;

    public String getModel() {
        return model;
    }
    
    String type;

    public String getType() {
        return type;
    }
    
    public TurboModelMap(String model, String type) {
        this.model = model;
        this.type = type;
    }
    
}
