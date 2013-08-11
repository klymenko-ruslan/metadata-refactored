// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.part.Heatshield;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Heatshield_Roo_Json {
    
    public String Heatshield.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String Heatshield.toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static Heatshield Heatshield.fromJsonToHeatshield(String json) {
        return new JSONDeserializer<Heatshield>().use(null, Heatshield.class).deserialize(json);
    }
    
    public static String Heatshield.toJsonArray(Collection<Heatshield> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String Heatshield.toJsonArray(Collection<Heatshield> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Heatshield> Heatshield.fromJsonArrayToHeatshields(String json) {
        return new JSONDeserializer<List<Heatshield>>().use(null, ArrayList.class).use("values", Heatshield.class).deserialize(json);
    }
    
}
