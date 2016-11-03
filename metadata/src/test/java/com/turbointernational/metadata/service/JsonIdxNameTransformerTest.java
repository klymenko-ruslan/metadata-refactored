package com.turbointernational.metadata.service;

import com.turbointernational.metadata.entity.CriticalDimensionEnumVal;
import flexjson.JSONSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dmytro.trunykov@gmail.com on 5/6/16.
 */
public class JsonIdxNameTransformerTest {

    private static class FooBean {

        private String memA;
        private int memB;
        private double memC;
        private Map<String, String> memD;
        private CriticalDimensionEnumVal memEnum;

        private FooBean(String memA, int memB, double memC, Map<String, String> memD, CriticalDimensionEnumVal memEnum) {
            this.memA = memA;
            this.memB = memB;
            this.memC = memC;
            this.memD = memD;
            this.memEnum = memEnum;
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

        public CriticalDimensionEnumVal getMemEnum() {
            return memEnum;
        }

        public void setMemEnum(CriticalDimensionEnumVal memEnum) {
            this.memEnum = memEnum;
        }
    }

    @Test
    public void testTransform() throws Exception {
        CriticalDimensionEnumVal memEnum = new CriticalDimensionEnumVal();
        memEnum.setId(5);
        memEnum.setVal("Foo");
        FooBean fooBean = new FooBean("hello", 12, 3.14, new HashMap<String, String>() {{
            put("k0", "ddd");
            put("k1", "FFdddww");
        }}, memEnum);
        JSONSerializer jsonSerializer = new JSONSerializer();
        jsonSerializer.include("memA", "memB", "memC", "memD").exclude("*.class");
        jsonSerializer.transform(new CriticalDimensionService.JsonIdxNameTransformer("memA_transformed", false), "memA");
        jsonSerializer.transform(new CriticalDimensionService.JsonIdxNameTransformer("memB_transformed", false), "memB");
        jsonSerializer.transform(new CriticalDimensionService.JsonIdxNameTransformer("memC_transformed", false), "memC");
        jsonSerializer.transform(new CriticalDimensionService.JsonIdxNameTransformer("memD_transformed", false), "memD");
        jsonSerializer.transform(new CriticalDimensionService.JsonIdxNameTransformer("memEnum", true), "memEnum");
        String json = jsonSerializer.serialize(fooBean);
//        System.out.println(json);
        Assert.assertEquals("Serialization to JSON failed.", "{\"memA_transformed\":\"hello\"," +
                "\"memB_transformed\":12,\"memC_transformed\":3.14," +
                "\"memD_transformed\":{\"k0\":\"ddd\",\"k1\":\"FFdddww\"},\"memEnum\":5,\"memEnumLabel\":\"Foo\"}",
                json);
    }

    /**
     * Check that field of type CriticalDimensionEnumVal is serialized even
     * if it is null.
     *
     * @throws Exception
     */
    @Test
    public void testTransform2() throws Exception {
        CriticalDimensionEnumVal memEnum = null;
        FooBean fooBean = new FooBean("hello", 12, 3.14, new HashMap<String, String>() {{
            put("k0", "ddd");
            put("k1", "FFdddww");
        }}, memEnum);
        JSONSerializer jsonSerializer = new JSONSerializer();
        jsonSerializer.include("memA", "memB", "memC", "memD").exclude("*.class");
        jsonSerializer.transform(new CriticalDimensionService.JsonIdxNameTransformer("memA_transformed", false), "memA");
        jsonSerializer.transform(new CriticalDimensionService.JsonIdxNameTransformer("memB_transformed", false), "memB");
        jsonSerializer.transform(new CriticalDimensionService.JsonIdxNameTransformer("memC_transformed", false), "memC");
        jsonSerializer.transform(new CriticalDimensionService.JsonIdxNameTransformer("memD_transformed", false), "memD");
        jsonSerializer.transform(new CriticalDimensionService.JsonIdxNameTransformer("memEnum", true), "memEnum");
        String json = jsonSerializer.serialize(fooBean);
//        System.out.println(json);
        Assert.assertEquals("Serialization to JSON failed.", "{\"memA_transformed\":\"hello\"," +
                "\"memB_transformed\":12,\"memC_transformed\":3.14," +
                "\"memD_transformed\":{\"k0\":\"ddd\",\"k1\":\"FFdddww\"},\"memEnum\":null,\"memEnumLabel\":null}",
                json);
    }

}