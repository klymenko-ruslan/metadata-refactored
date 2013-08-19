// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.part.BearingHousing;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect BearingHousing_Roo_Json {
    
    public String BearingHousing.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String BearingHousing.toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static BearingHousing BearingHousing.fromJsonToBearingHousing(String json) {
        return new JSONDeserializer<BearingHousing>().use(null, BearingHousing.class).deserialize(json);
    }
    
    public static String BearingHousing.toJsonArray(Collection<BearingHousing> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String BearingHousing.toJsonArray(Collection<BearingHousing> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<BearingHousing> BearingHousing.fromJsonArrayToBearingHousings(String json) {
        return new JSONDeserializer<List<BearingHousing>>().use(null, ArrayList.class).use("values", BearingHousing.class).deserialize(json);
    }
    
}
