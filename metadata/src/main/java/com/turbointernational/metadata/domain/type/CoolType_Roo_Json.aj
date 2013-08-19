// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.type.CoolType;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect CoolType_Roo_Json {
    
    public String CoolType.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String CoolType.toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static CoolType CoolType.fromJsonToCoolType(String json) {
        return new JSONDeserializer<CoolType>().use(null, CoolType.class).deserialize(json);
    }
    
    public static String CoolType.toJsonArray(Collection<CoolType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String CoolType.toJsonArray(Collection<CoolType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<CoolType> CoolType.fromJsonArrayToCoolTypes(String json) {
        return new JSONDeserializer<List<CoolType>>().use(null, ArrayList.class).use("values", CoolType.class).deserialize(json);
    }
    
}
