// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.type.TurboType;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect TurboType_Roo_Json {
    
    public String TurboType.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String TurboType.toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static TurboType TurboType.fromJsonToTurboType(String json) {
        return new JSONDeserializer<TurboType>().use(null, TurboType.class).deserialize(json);
    }
    
    public static String TurboType.toJsonArray(Collection<TurboType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String TurboType.toJsonArray(Collection<TurboType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<TurboType> TurboType.fromJsonArrayToTurboTypes(String json) {
        return new JSONDeserializer<List<TurboType>>().use(null, ArrayList.class).use("values", TurboType.class).deserialize(json);
    }
    
}