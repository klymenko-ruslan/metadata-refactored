// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.other;

import com.turbointernational.metadata.domain.other.Manufacturer;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Manufacturer_Roo_Json {
    
    public String Manufacturer.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String Manufacturer.toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static Manufacturer Manufacturer.fromJsonToManufacturer(String json) {
        return new JSONDeserializer<Manufacturer>().use(null, Manufacturer.class).deserialize(json);
    }
    
    public static String Manufacturer.toJsonArray(Collection<Manufacturer> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String Manufacturer.toJsonArray(Collection<Manufacturer> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Manufacturer> Manufacturer.fromJsonArrayToManufacturers(String json) {
        return new JSONDeserializer<List<Manufacturer>>().use(null, ArrayList.class).use("values", Manufacturer.class).deserialize(json);
    }
    
}
