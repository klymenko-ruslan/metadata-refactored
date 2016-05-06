package com.turbointernational.metadata.services;

import flexjson.JSONSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by dmytro.trunykov@gmail.com on 5/6/16.
 */
public class JsonIdxNameTransformerTest {

    private static class FooBean {

        private String memA;
        private int memB;
        private double memC;
        private Map<String, String> memD;

        private FooBean(String memA, int memB, double memC, Map<String, String> memD) {
            this.memA = memA;
            this.memB = memB;
            this.memC = memC;
            this.memD = memD;
        }

        public String getMemA() {
            return memA;
        }

        public void setMemA(String memA) {
            this.memA = memA;
        }

        public int getMemB() {
            return memB;
        }

        public void setMemB(int memB) {
            this.memB = memB;
        }

        public double getMemC() {
            return memC;
        }

        public void setMemC(double memC) {
            this.memC = memC;
        }

        public Map<String, String> getMemD() {
            return memD;
        }

        public void setMemD(Map<String, String> memD) {
            this.memD = memD;
        }
    }

    @Test
    public void testTransform() throws Exception {
        FooBean fooBean = new FooBean("hello", 12, 3.14, new HashMap<String, String>() {{
            put("k0", "ddd");
            put("k1", "FFdddww");
        }});
        JSONSerializer jsonSerializer = new JSONSerializer();
        jsonSerializer.include("memA", "memB", "memC", "memD").exclude("*.class");
        jsonSerializer.transform(new SearchServiceEsImpl.JsonIdxNameTransformer("memA_transformed"), "memA");
        jsonSerializer.transform(new SearchServiceEsImpl.JsonIdxNameTransformer("memB_transformed"), "memB");
        jsonSerializer.transform(new SearchServiceEsImpl.JsonIdxNameTransformer("memC_transformed"), "memC");
        jsonSerializer.transform(new SearchServiceEsImpl.JsonIdxNameTransformer("memD_transformed"), "memD");
        String json = jsonSerializer.serialize(fooBean);
//        System.out.println(json);
        Assert.assertEquals("Serialization to JSON failed.", "{\"memA_transformed\":\"hello\"," +
                "\"memB_transformed\":12,\"memC_transformed\":3.14," +
                "\"memD_transformed\":{\"k0\":\"ddd\",\"k1\":\"FFdddww\"}}", json);
    }

}