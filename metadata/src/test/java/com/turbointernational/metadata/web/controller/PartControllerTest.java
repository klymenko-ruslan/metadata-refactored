package com.turbointernational.metadata.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.turbointernational.metadata.AbstractFunctionalWebTest;
import com.turbointernational.metadata.dao.ChangelogDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.Manufacturer;
import com.turbointernational.metadata.entity.PartType;
import com.turbointernational.metadata.entity.TurboModel;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.types.Actuator;
import com.turbointernational.metadata.entity.part.types.Backplate;
import com.turbointernational.metadata.entity.part.types.BackplateSealplate;
import com.turbointernational.metadata.entity.part.types.BearingHousing;
import com.turbointernational.metadata.entity.part.types.BoltScrew;
import com.turbointernational.metadata.entity.part.types.CarbonSeal;
import com.turbointernational.metadata.entity.part.types.Cartridge;
import com.turbointernational.metadata.entity.part.types.Clamp;
import com.turbointernational.metadata.entity.part.types.CompressorCover;
import com.turbointernational.metadata.entity.part.types.CompressorWheel;
import com.turbointernational.metadata.entity.part.types.FastWearingComponent;
import com.turbointernational.metadata.entity.part.types.Fitting;
import com.turbointernational.metadata.entity.part.types.Gasket;
import com.turbointernational.metadata.entity.part.types.GasketKit;
import com.turbointernational.metadata.entity.part.types.HeatshieldShroud;
import com.turbointernational.metadata.entity.part.types.JournalBearing;
import com.turbointernational.metadata.entity.part.types.JournalBearingSpacer;
import com.turbointernational.metadata.entity.part.types.Kit;
import com.turbointernational.metadata.entity.part.types.MajorComponent;
import com.turbointernational.metadata.entity.part.types.MinorComponent;
import com.turbointernational.metadata.entity.part.types.Misc;
import com.turbointernational.metadata.entity.part.types.MiscMinorComponent;
import com.turbointernational.metadata.entity.part.types.NozzleRing;
import com.turbointernational.metadata.entity.part.types.Nut;
import com.turbointernational.metadata.entity.part.types.ORing;
import com.turbointernational.metadata.entity.part.types.OilDeflector;
import com.turbointernational.metadata.entity.part.types.Pin;
import com.turbointernational.metadata.entity.part.types.PistonRing;
import com.turbointernational.metadata.entity.part.types.Plug;
import com.turbointernational.metadata.entity.part.types.RetainingRing;
import com.turbointernational.metadata.entity.part.types.SealPlate;
import com.turbointernational.metadata.entity.part.types.Shroud;
import com.turbointernational.metadata.entity.part.types.Spring;
import com.turbointernational.metadata.entity.part.types.ThrustBearing;
import com.turbointernational.metadata.entity.part.types.ThrustCollar;
import com.turbointernational.metadata.entity.part.types.ThrustPart;
import com.turbointernational.metadata.entity.part.types.ThrustSpacer;
import com.turbointernational.metadata.entity.part.types.ThrustWasher;
import com.turbointernational.metadata.entity.part.types.TurbineHousing;
import com.turbointernational.metadata.entity.part.types.TurbineWheel;
import com.turbointernational.metadata.entity.part.types.Turbo;
import com.turbointernational.metadata.entity.part.types.Washer;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/19/16.
 */
public class PartControllerTest extends AbstractFunctionalWebTest {

    @Autowired
    private PartDao partDao;

    @Autowired
    private ChangelogDao changelogDao;

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateActuator() throws Exception {
        //@formatter:off
        String requestBody = "{"
                + "   \"origin\":{" 
                + "       \"turboModel\":{" 
                + "           \"turboType\":{}"
                + "       }," 
                + "       \"partType\":{" 
                + "           \"id\":30," 
                + "           \"name\":\"Actuator\","
                + "           \"value\":\"actuator\"," 
                + "           \"magentoAttributeSet\":\"Actuator\","
                + "           \"route\":\"parttype/json\"," 
                + "           \"reqParams\":null,"
                + "           \"restangularized\":true," 
                + "           \"fromServer\":true,"
                + "           \"parentResource\":null," 
                + "           \"restangularCollection\":false" 
                + "       },"
                + "       \"name\":\"fff\"," 
                + "       \"description\":\"dddd\"," 
                + "       \"manufacturer\":{"
                + "           \"id\":11," 
                + "           \"name\":\"Turbo International\","
                + "           \"route\":\"other/manufacturer/list\"," 
                + "           \"reqParams\":null,"
                + "           \"restangularized\":true," 
                + "           \"fromServer\":true,"
                + "           \"parentResource\":null," 
                + "           \"restangularCollection\":false" 
                + "       },"
                + "       \"class\":\"com.turbointernational.metadata.entity.part.types.Actuator\"" 
                + "    },"
                + "    \"partNumbers\":[\"409043-0033\"]}";
        //@formatter:on
        //@formatter:off
        String responseBody = "{" 
                + "    \"results\":[{" 
                + "       \"partId\":1,"
                + "       \"manufacturerPartNumber\":\"409043-0033\"," 
                + "       \"success\":true,"
                + "       \"errorMessage\":null" 
                + "    }]}";
        //@formatter:on
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Actuator);
        assertEquals("409043-0033", part.getManufacturerPartNumber());
        assertEquals("fff", part.getName());
        assertEquals("dddd", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(30L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateBackplate() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":34,\"name\":\"Backplate\",\"legendImgFilename\":\"34_ptlgnd_1478125523833.jpg\",\"value\":\"backplate\",\"magentoAttributeSet\":\"Backplate\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff1\",\"description\":\"dddd1\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.Backplate\"},\"partNumbers\":[\"409043-0034\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0034\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Backplate);
        assertEquals("409043-0034", part.getManufacturerPartNumber());
        assertEquals("ffff1", part.getName());
        assertEquals("dddd1", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(34L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateBackplateSealplate() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":14,\"name\":\"Backplate / Sealplate\",\"legendImgFilename\":\"14_ptlgnd_1478125247361.jpg\",\"value\":\"backplate_sealplate\",\"magentoAttributeSet\":\"Backplate / Sealplate\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"description\":\"dddd2\",\"name\":\"ffff2\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.BackplateSealplate\"},\"partNumbers\":[\"409043-0035\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0035\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof BackplateSealplate);
        assertEquals("409043-0035", part.getManufacturerPartNumber());
        assertEquals("ffff2", part.getName());
        assertEquals("dddd2", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(14L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateBearingHousing() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":13,\"name\":\"Bearing Housing\",\"value\":\"bearing_housing\",\"magentoAttributeSet\":\"Bearing Housing\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff3\",\"description\":\"dddd3\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.BearingHousing\"},\"partNumbers\":[\"409043-0036\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0036\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof BearingHousing);
        assertEquals("409043-0036", part.getManufacturerPartNumber());
        assertEquals("ffff3", part.getName());
        assertEquals("dddd3", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(13L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateBoltScrew() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":35,\"name\":\"Bolt - Screw\",\"legendImgFilename\":\"35_ptlgnd_1478023491332.jpg\",\"value\":\"bolt_screw\",\"magentoAttributeSet\":\"Bolt Screw\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff4\",\"description\":\"dddd4\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.BoltScrew\"},\"partNumbers\":[\"409043-0037\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0037\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof BoltScrew);
        assertEquals("409043-0037", part.getManufacturerPartNumber());
        assertEquals("ffff4", part.getName());
        assertEquals("dddd4", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(35L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateCarbonSeal() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":48,\"name\":\"Carbon Seal\",\"legendImgFilename\":\"48_ptlgnd_1473891874716.jpg\",\"value\":\"carbon_seal\",\"magentoAttributeSet\":\"Carbon Seal\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff5\",\"description\":\"dddd5\",\"class\":\"com.turbointernational.metadata.entity.part.types.CarbonSeal\"},\"partNumbers\":[\"409043-0038\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0038\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof CarbonSeal);
        assertEquals("409043-0038", part.getManufacturerPartNumber());
        assertEquals("ffff5", part.getName());
        assertEquals("dddd5", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(48L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateCartridge() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":2,\"name\":\"Cartridge\",\"value\":\"cartridge\",\"magentoAttributeSet\":\"Cartridge\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff6\",\"description\":\"dddd6\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.Cartridge\"},\"partNumbers\":[\"409043-0039\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0039\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Cartridge);
        assertEquals("409043-0039", part.getManufacturerPartNumber());
        assertEquals("ffff6", part.getName());
        assertEquals("dddd6", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(2L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateClamp() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":19,\"name\":\"Clamp\",\"legendImgFilename\":\"19_ptlgnd_1478100514784.jpg\",\"value\":\"clamp\",\"magentoAttributeSet\":\"Clamp\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff7\",\"description\":\"dddd7\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.Clamp\"},\"partNumbers\":[\"409043-0040\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0040\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Clamp);
        assertEquals("409043-0040", part.getManufacturerPartNumber());
        assertEquals("ffff7", part.getName());
        assertEquals("dddd7", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(19L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateCompressorCover() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":31,\"name\":\"Compressor Cover\",\"value\":\"compressor_cover\",\"magentoAttributeSet\":\"Compressor Cover\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff8\",\"description\":\"dddd8\",\"class\":\"com.turbointernational.metadata.entity.part.types.CompressorCover\"},\"partNumbers\":[\"409043-0041\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0041\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof CompressorCover);
        assertEquals("409043-0041", part.getManufacturerPartNumber());
        assertEquals("ffff8", part.getName());
        assertEquals("dddd8", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(31L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateCompressorWheel() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":11,\"name\":\"Compressor Wheel\",\"legendImgFilename\":\"11_ptlgnd_1478122999026.jpg\",\"value\":\"compressor_wheel\",\"magentoAttributeSet\":\"Compressor Wheel\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff9\",\"description\":\"dddd9\",\"class\":\"com.turbointernational.metadata.entity.part.types.CompressorWheel\"},\"partNumbers\":[\"409043-0042\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0042\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof CompressorWheel);
        assertEquals("409043-0042", part.getManufacturerPartNumber());
        assertEquals("ffff9", part.getName());
        assertEquals("dddd9", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(11L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateFastWearingComponent() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":8,\"name\":\"Fast Wearing Component\",\"value\":\"fast_wearing_component\",\"magentoAttributeSet\":\"Fast Wearing Component\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff10\",\"description\":\"dddd10\",\"class\":\"com.turbointernational.metadata.entity.part.types.FastWearingComponent\"},\"partNumbers\":[\"409043-0043\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0043\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof FastWearingComponent);
        assertEquals("409043-0043", part.getManufacturerPartNumber());
        assertEquals("ffff10", part.getName());
        assertEquals("dddd10", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(8L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateFitting() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":36,\"name\":\"Fitting\",\"legendImgFilename\":\"36_ptlgnd_1478186467689.jpg\",\"value\":\"fitting\",\"magentoAttributeSet\":\"Fitting\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff11\",\"description\":\"dddd11\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.Fitting\"},\"partNumbers\":[\"409043-0044\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0044\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Fitting);
        assertEquals("409043-0044", part.getManufacturerPartNumber());
        assertEquals("ffff11", part.getName());
        assertEquals("dddd11", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(36L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateGasket() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":6,\"name\":\"Gasket\",\"legendImgFilename\":\"6_ptlgnd_1478120255057.jpg\",\"value\":\"gasket\",\"magentoAttributeSet\":\"Gasket\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff12\",\"description\":\"dddd12\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.Gasket\"},\"partNumbers\":[\"409043-0045\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0045\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Gasket);
        assertEquals("409043-0045", part.getManufacturerPartNumber());
        assertEquals("ffff12", part.getName());
        assertEquals("dddd12", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(6L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateGasketKit() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":49,\"name\":\"Gasket Kit\",\"value\":\"gasket_kit\",\"magentoAttributeSet\":\"Gasket Kit\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff13\",\"description\":\"dddd13\",\"class\":\"com.turbointernational.metadata.entity.part.types.GasketKit\"},\"partNumbers\":[\"409043-0046\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0046\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof GasketKit);
        assertEquals("409043-0046", part.getManufacturerPartNumber());
        assertEquals("ffff13", part.getName());
        assertEquals("dddd13", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(49L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateHeatshieldShroud() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":15,\"name\":\"Heatshield / Shroud\",\"legendImgFilename\":\"15_ptlgnd_1478121891821.jpg\",\"value\":\"heatshield\",\"magentoAttributeSet\":\"Heatshield\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff14\",\"description\":\"dddd14\",\"class\":\"com.turbointernational.metadata.entity.part.types.HeatshieldShroud\"},\"partNumbers\":[\"409043-0047\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0047\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof HeatshieldShroud);
        assertEquals("409043-0047", part.getManufacturerPartNumber());
        assertEquals("ffff14", part.getName());
        assertEquals("dddd14", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(15L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateJournalBearing() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":5,\"name\":\"Journal Bearing\",\"value\":\"journal_bearing\",\"magentoAttributeSet\":\"Journal Bearing\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff15\",\"description\":\"dddd15\",\"class\":\"com.turbointernational.metadata.entity.part.types.JournalBearing\"},\"partNumbers\":[\"409043-0048\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0048\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof JournalBearing);
        assertEquals("409043-0048", part.getManufacturerPartNumber());
        assertEquals("ffff15", part.getName());
        assertEquals("dddd15", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(5L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateJournalBearingSpacer() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":37,\"name\":\"Journal Bearing Spacer\",\"legendImgFilename\":\"37_ptlgnd_1478100831313.jpg\",\"value\":\"journal_bearing_spacer\",\"magentoAttributeSet\":\"Journal Bearing Spacer\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff16\",\"description\":\"dddd16\",\"class\":\"com.turbointernational.metadata.entity.part.types.JournalBearingSpacer\"},\"partNumbers\":[\"409043-0049\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0049\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof JournalBearingSpacer);
        assertEquals("409043-0049", part.getManufacturerPartNumber());
        assertEquals("ffff16", part.getName());
        assertEquals("dddd16", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(37L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateKit() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":3,\"name\":\"Kit\",\"value\":\"kit\",\"magentoAttributeSet\":\"Kit\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff17\",\"description\":\"dddd17\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"kitType\":{\"id\":2,\"name\":\"Full\",\"route\":\"kittype/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.Kit\"},\"partNumbers\":[\"409043-0050\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0050\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Kit);
        assertEquals("409043-0050", part.getManufacturerPartNumber());
        assertEquals("ffff17", part.getName());
        assertEquals("dddd17", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(3L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateMajorComponent() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":9,\"name\":\"Major Component\",\"value\":\"major_component\",\"magentoAttributeSet\":\"Major Component\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff18\",\"description\":\"dddd18\",\"class\":\"com.turbointernational.metadata.entity.part.types.MajorComponent\"},\"partNumbers\":[\"409043-0051\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0051\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof MajorComponent);
        assertEquals("409043-0051", part.getManufacturerPartNumber());
        assertEquals("ffff18", part.getName());
        assertEquals("dddd18", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(9L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateMinorComponent() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":10,\"name\":\"Minor Component\",\"value\":\"minor_component\",\"magentoAttributeSet\":\"Minor Component\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff19\",\"description\":\"dddd19\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.MinorComponent\"},\"partNumbers\":[\"409043-0052\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0052\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof MinorComponent);
        assertEquals("409043-0052", part.getManufacturerPartNumber());
        assertEquals("ffff19", part.getName());
        assertEquals("dddd19", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(10L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateMisc() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":50,\"name\":\"Misc\",\"value\":\"misc\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff20\",\"description\":\"dddd20\",\"class\":\"com.turbointernational.metadata.entity.part.types.Misc\"},\"partNumbers\":[\"409043-0053\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0053\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Misc);
        assertEquals("409043-0053", part.getManufacturerPartNumber());
        assertEquals("ffff20", part.getName());
        assertEquals("dddd20", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(50L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateMiscMinorComponent() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":21,\"name\":\"Miscellaneous Minor Components\",\"value\":\"misc_minor_components\",\"magentoAttributeSet\":\"Miscellaneous Minor Components\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff21\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"description\":\"dddd21\",\"class\":\"com.turbointernational.metadata.entity.part.types.MiscMinorComponent\"},\"partNumbers\":[\"409043-0054\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0054\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof MiscMinorComponent);
        assertEquals("409043-0054", part.getManufacturerPartNumber());
        assertEquals("ffff21", part.getName());
        assertEquals("dddd21", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(21L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateNozzleRing() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":16,\"name\":\"Nozzle Ring\",\"legendImgFilename\":\"16_ptlgnd_1473891647008.jpg\",\"value\":\"nozzle_ring\",\"magentoAttributeSet\":\"Nozzle Ring\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff22\",\"description\":\"dddd22\",\"class\":\"com.turbointernational.metadata.entity.part.types.NozzleRing\"},\"partNumbers\":[\"409043-0055\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0055\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof NozzleRing);
        assertEquals("409043-0055", part.getManufacturerPartNumber());
        assertEquals("ffff22", part.getName());
        assertEquals("dddd22", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(16L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateNut() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":38,\"name\":\"Nut\",\"legendImgFilename\":\"38_ptlgnd_1479256221517.jpg\",\"value\":\"nut\",\"magentoAttributeSet\":\"Nut\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff23\",\"description\":\"dddd23\",\"class\":\"com.turbointernational.metadata.entity.part.types.Nut\"},\"partNumbers\":[\"409043-0056\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0056\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Nut);
        assertEquals("409043-0056", part.getManufacturerPartNumber());
        assertEquals("ffff23", part.getName());
        assertEquals("dddd23", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(38L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateORing() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":17,\"name\":\"O Ring\",\"legendImgFilename\":\"17_ptlgnd_1473892024291.jpg\",\"value\":\"o_ring\",\"magentoAttributeSet\":\"O Ring\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff24\",\"description\":\"dddd24\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.ORing\"},\"partNumbers\":[\"409043-0057\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0057\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof ORing);
        assertEquals("409043-0057", part.getManufacturerPartNumber());
        assertEquals("ffff24", part.getName());
        assertEquals("dddd24", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(17L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateOilDeflector() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":18,\"name\":\"Oil Deflector\",\"legendImgFilename\":\"18_ptlgnd_1478115113229.jpg\",\"value\":\"oil_deflector\",\"magentoAttributeSet\":\"Oil Deflector\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff25\",\"description\":\"dddd25\",\"class\":\"com.turbointernational.metadata.entity.part.types.OilDeflector\"},\"partNumbers\":[\"409043-0058\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0058\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof OilDeflector);
        assertEquals("409043-0058", part.getManufacturerPartNumber());
        assertEquals("ffff25", part.getName());
        assertEquals("dddd25", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(18L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreatePart() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":51,\"name\":\"Part\",\"value\":\"p\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff26\",\"description\":\"dddd6\",\"class\":\"com.turbointernational.metadata.entity.part.types.P\"},\"partNumbers\":[\"409043-0059\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0059\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Part);
        assertEquals("409043-0059", part.getManufacturerPartNumber());
        assertEquals("ffff26", part.getName());
        assertEquals("dddd6", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(51L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreatePin() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":39,\"name\":\"Pin\",\"value\":\"pin\",\"magentoAttributeSet\":\"Pin\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff27\",\"description\":\"dddd27\",\"class\":\"com.turbointernational.metadata.entity.part.types.Pin\"},\"partNumbers\":[\"409043-0060\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0060\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Pin);
        assertEquals("409043-0060", part.getManufacturerPartNumber());
        assertEquals("ffff27", part.getName());
        assertEquals("dddd27", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(39L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreatePistonRing() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":4,\"name\":\"Piston Ring\",\"legendImgFilename\":\"4_ptlgnd_1478123650773.jpg\",\"value\":\"piston_ring\",\"magentoAttributeSet\":\"Piston Ring\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff28\",\"description\":\"dddd28\",\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.PistonRing\"},\"partNumbers\":[\"409043-0061\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0061\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof PistonRing);
        assertEquals("409043-0061", part.getManufacturerPartNumber());
        assertEquals("ffff28", part.getName());
        assertEquals("dddd28", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(4L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreatePlug() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":32,\"name\":\"Plug\",\"value\":\"plug\",\"magentoAttributeSet\":\"Plug\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff29\",\"description\":\"dddd29\",\"class\":\"com.turbointernational.metadata.entity.part.types.Plug\"},\"partNumbers\":[\"409043-0062\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0062\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Plug);
        assertEquals("409043-0062", part.getManufacturerPartNumber());
        assertEquals("ffff29", part.getName());
        assertEquals("dddd29", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(32L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateRetainingRing() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":40,\"name\":\"Retaining Ring\",\"legendImgFilename\":\"40_ptlgnd_1478124544053.jpg\",\"value\":\"retaining_ring\",\"magentoAttributeSet\":\"Retaining Ring\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff30\",\"description\":\"dddd30\",\"class\":\"com.turbointernational.metadata.entity.part.types.RetainingRing\"},\"partNumbers\":[\"409043-0063\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0063\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof RetainingRing);
        assertEquals("409043-0063", part.getManufacturerPartNumber());
        assertEquals("ffff30", part.getName());
        assertEquals("dddd30", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(40L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateSealPlate() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":41,\"name\":\"Seal Plate\",\"legendImgFilename\":\"41_ptlgnd_1478187205320.jpg\",\"value\":\"seal_plate\",\"magentoAttributeSet\":\"Seal Plate\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff31\",\"description\":\"dddd31\",\"class\":\"com.turbointernational.metadata.entity.part.types.SealPlate\"},\"partNumbers\":[\"409043-0064\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0064\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof SealPlate);
        assertEquals("409043-0064", part.getManufacturerPartNumber());
        assertEquals("ffff31", part.getName());
        assertEquals("dddd31", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(41L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateShroud() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":52,\"name\":\"Shroud\",\"value\":\"shroud\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff31\",\"description\":\"dddd31\",\"class\":\"com.turbointernational.metadata.entity.part.types.Shroud\"},\"partNumbers\":[\"409043-0065\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0065\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Shroud);
        assertEquals("409043-0065", part.getManufacturerPartNumber());
        assertEquals("ffff31", part.getName());
        assertEquals("dddd31", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(52L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateSpring() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":42,\"name\":\"Spring\",\"value\":\"spring\",\"magentoAttributeSet\":\"Spring\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff33\",\"description\":\"dddd33\",\"class\":\"com.turbointernational.metadata.entity.part.types.Spring\"},\"partNumbers\":[\"409043-0066\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0066\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Spring);
        assertEquals("409043-0066", part.getManufacturerPartNumber());
        assertEquals("ffff33", part.getName());
        assertEquals("dddd33", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(42L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateThrustBearing() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":43,\"name\":\"Thrust Bearing\",\"value\":\"thrust_bearing\",\"magentoAttributeSet\":\"Thrust Bearing\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff34\",\"description\":\"dddd34\",\"class\":\"com.turbointernational.metadata.entity.part.types.ThrustBearing\"},\"partNumbers\":[\"409043-0067\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0067\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof ThrustBearing);
        assertEquals("409043-0067", part.getManufacturerPartNumber());
        assertEquals("ffff34", part.getName());
        assertEquals("dddd34", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(43L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateThrustCollar() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":44,\"name\":\"Thrust Collar\",\"legendImgFilename\":\"44_ptlgnd_1478101728592.jpg\",\"value\":\"thrust_collar\",\"magentoAttributeSet\":\"Thrust Collar\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff35\",\"description\":\"dddd35\",\"class\":\"com.turbointernational.metadata.entity.part.types.ThrustCollar\"},\"partNumbers\":[\"409043-0068\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0068\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof ThrustCollar);
        assertEquals("409043-0068", part.getManufacturerPartNumber());
        assertEquals("ffff35", part.getName());
        assertEquals("dddd35", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(44L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateThrustPart() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":20,\"name\":\"Thrust Parts\",\"value\":\"thrust_parts\",\"magentoAttributeSet\":\"Thrust Parts\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff36\",\"description\":\"dddd36\",\"class\":\"com.turbointernational.metadata.entity.part.types.ThrustPart\"},\"partNumbers\":[\"409043-0069\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0069\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof ThrustPart);
        assertEquals("409043-0069", part.getManufacturerPartNumber());
        assertEquals("ffff36", part.getName());
        assertEquals("dddd36", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(20L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateThrustSpacer() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":45,\"name\":\"Thrust Spacer\",\"legendImgFilename\":\"45_ptlgnd_1478107227367.jpg\",\"value\":\"thrust_spacer\",\"magentoAttributeSet\":\"Thrust Spacer\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff37\",\"description\":\"dddd37\",\"class\":\"com.turbointernational.metadata.entity.part.types.ThrustSpacer\"},\"partNumbers\":[\"409043-0070\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0070\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof ThrustSpacer);
        assertEquals("409043-0070", part.getManufacturerPartNumber());
        assertEquals("ffff37", part.getName());
        assertEquals("dddd37", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(45L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateThrustWasher() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":46,\"name\":\"Thrust Washer\",\"legendImgFilename\":\"46_ptlgnd_1473892181232.jpg\",\"value\":\"thrust_washer\",\"magentoAttributeSet\":\"Thrust Washer\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"description\":\"dddd38\",\"name\":\"ffff38\",\"class\":\"com.turbointernational.metadata.entity.part.types.ThrustWasher\"},\"partNumbers\":[\"409043-0071\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0071\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof ThrustWasher);
        assertEquals("409043-0071", part.getManufacturerPartNumber());
        assertEquals("ffff38", part.getName());
        assertEquals("dddd38", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(46L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateTurbineHousing() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":33,\"name\":\"Turbine Housing\",\"value\":\"turbine_housing\",\"magentoAttributeSet\":\"Turbine Housing\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff39\",\"description\":\"dddd39\",\"class\":\"com.turbointernational.metadata.entity.part.types.TurbineHousing\"},\"partNumbers\":[\"409043-0072\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0072\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof TurbineHousing);
        assertEquals("409043-0072", part.getManufacturerPartNumber());
        assertEquals("ffff39", part.getName());
        assertEquals("dddd39", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(33L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateTurbineWheel() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":12,\"name\":\"Turbine Wheel\",\"legendImgFilename\":\"12_ptlgnd_1478123245962.jpg\",\"value\":\"turbine_wheel\",\"magentoAttributeSet\":\"Turbine Wheel\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff40\",\"description\":\"dddd40\",\"class\":\"com.turbointernational.metadata.entity.part.types.TurbineWheel\"},\"partNumbers\":[\"409043-0073\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0073\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof TurbineWheel);
        assertEquals("409043-0073", part.getManufacturerPartNumber());
        assertEquals("ffff40", part.getName());
        assertEquals("dddd40", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(12L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = BEFORE_TEST_METHOD, statements = "insert into turbo_type(id, name, manfr_id) values(952, 'K03', 11);"
            + "insert into turbo_model(id, name, turbo_type_id) values(7974, 'K03-1870EXA4.82CAXK', 952);")
    @Sql(executionPhase = AFTER_TEST_METHOD, statements = "delete from turbo_type where id=7974;"
            + "delete from turbo_model where id=952;")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateTurbo() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"id\":7974,\"name\":\"K03-1870EXA4.82CAXK\",\"turboType\":{\"id\":952,\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"type\":{\"id\":1,\"name\":\"turbo\"},\"importPK\":999},\"name\":\"K03\",\"route\":\"list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"route\":\"turboModel\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"partType\":{\"id\":1,\"name\":\"Turbo\",\"value\":\"turbo\",\"magentoAttributeSet\":\"Turbo\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"description\":\"dddd41\",\"name\":\"ffff41\",\"class\":\"com.turbointernational.metadata.entity.part.types.Turbo\"},\"partNumbers\":[\"409043-0074\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0074\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Turbo);
        assertEquals("409043-0074", part.getManufacturerPartNumber());
        assertEquals("ffff41", part.getName());
        assertEquals("dddd41", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(1L, partType.getId().longValue());
        Turbo turbo = (Turbo) part;
        TurboModel turboModel = turbo.getTurboModel();
        assertNotNull(turboModel);
        assertEquals("K03-1870EXA4.82CAXK", turboModel.getName());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreateWasher() throws Exception {
        String requestBody = "{\"origin\":{\"turboModel\":{\"turboType\":{}},\"partType\":{\"id\":47,\"name\":\"Washer\",\"legendImgFilename\":\"47_ptlgnd_1473892208174.jpg\",\"value\":\"washer\",\"magentoAttributeSet\":\"Washer\",\"route\":\"parttype/json\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"name\":\"ffff42\",\"description\":\"dddd42\",\"class\":\"com.turbointernational.metadata.entity.part.types.Washer\"},\"partNumbers\":[\"409043-0075\"]}";
        String responseBody = "{\"results\":[{\"partId\":1,\"manufacturerPartNumber\":\"409043-0075\",\"success\":true,\"errorMessage\":null}]}";
        mockMvc.perform(post("/metadata/part").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                .andExpect(content().json(responseBody));
        Part part = partDao.findOne(1L);
        assertNotNull(part);
        assertTrue(part instanceof Washer);
        assertEquals("409043-0075", part.getManufacturerPartNumber());
        assertEquals("ffff42", part.getName());
        assertEquals("dddd42", part.getDescription());
        Manufacturer manufacturer = part.getManufacturer();
        assertNotNull(manufacturer);
        assertEquals(11L, manufacturer.getId().longValue());
        PartType partType = part.getPartType();
        assertNotNull(partType);
        assertEquals(47L, partType.getId().longValue());
        long chlgcnt = changelogDao.count();
        assertEquals(1, chlgcnt);
    }

    /**
     * Test link a Gasket Kit with a Turbo when all constraints are correct.
     */
    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/part_controller/setgasketkitforpart_success.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testSetGasketKitForPartSuccess() throws Exception {
        String requestBody = "{\"id\":69079}";
        String responseBody = "{\"status\":\"OK\"}";
        mockMvc.perform(put("/metadata/part/25861/gasketkit/69079").content(requestBody).contentType(contentType))
                .andExpect(status().isOk()).andExpect(content().json(responseBody));
        Part part = partDao.findOne(25861L);
        assertNotNull(part);
        assertTrue(part instanceof Turbo);
        Turbo turbo = (Turbo) part;
        GasketKit gasketKit = turbo.getGasketKit();
        assertNotNull(gasketKit);
        assertEquals(69079L, gasketKit.getId().longValue());
    }

    /**
     * Test link a Gasket Kit with a Turbo when they are already linked.
     */
    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/part_controller/setgasketkitforpart_alreadylinked.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testSetGasketKitForPartFailureAlreadyLinked() throws Exception {
        String requestBody = "{\"id\":69079}";
        String responseBody = "{\"status\":\"ASSERTION_ERROR\", \"message\": \"Gasket Kit [69079] - pending285 already linked with the Turbo [25861] - 17201-0L020.\"}";
        mockMvc.perform(put("/metadata/part/25861/gasketkit/69079").content(requestBody).contentType(contentType))
                .andExpect(status().isOk()).andExpect(content().json(responseBody));
    }

    /**
     * Test link a Gasket Kit with a Turbo when 'Turbo' has wrong a part type.
     */
    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/part_controller/setgasketkitforpart_failure_notturbo.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testSetGasketKitForPartFailureNotTurbo() throws Exception {
        String requestBody = "{\"id\":69079}";
        String responseBody = "{\"status\":\"ASSERTION_ERROR\", \"message\": \"Part [25861] - 17201-0L020 has unexpected part type: 2. Expected a Turbo.\"}";
        mockMvc.perform(put("/metadata/part/25861/gasketkit/69079").content(requestBody).contentType(contentType))
                .andExpect(status().isOk()).andExpect(content().json(responseBody));
    }

    /**
     * Test link a Gasket Kit with a Turbo when 'Gasket Kit' has wrong a part type.
     */
    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/part_controller/setgasketkitforpart_failure_notgasketkit.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testSetGasketKitForPartFailureNotGasketKit() throws Exception {
        String requestBody = "{\"id\":69079}";
        String responseBody = "{\"status\":\"ASSERTION_ERROR\", \"message\": \"Part [69079] - pending285 has unexpected part type: 2. Expected a Gasket Kit.\"}";
        mockMvc.perform(put("/metadata/part/25861/gasketkit/69079").content(requestBody).contentType(contentType))
                .andExpect(status().isOk()).andExpect(content().json(responseBody));
    }

    /**
     * Test link a Gasket Kit with a Turbo when they have different manufacturers.
     */
    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/part_controller/setgasketkitforpart_failure_manufacturer.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testSetGasketKitForPartFailureManufacturer() throws Exception {
        String requestBody = "{\"id\":69079}";
        String responseBody = "{\"status\":\"ASSERTION_ERROR\", \"message\": \"The Turbo and Gasket Kit have different manufacturers.\"}";
        mockMvc.perform(put("/metadata/part/25861/gasketkit/69079").content(requestBody).contentType(contentType))
                .andExpect(status().isOk()).andExpect(content().json(responseBody));
    }

    /**
     * Test link a Gasket Kit with a Turbo when they incompatible BOMs sets.
     */
    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/part_controller/setgasketkitforpart_failure_boms.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testSetGasketKitForPartFailureBOMs() throws Exception {
        String requestBody = "{\"id\":69079}";
        String responseBody = "{\"status\":\"ASSERTION_ERROR\", \"message\": \"Not all parts in BOM of the Gasket Kit exist in the BOM of associated Turbo.\"}";
        mockMvc.perform(put("/metadata/part/25861/gasketkit/69079").content(requestBody).contentType(contentType))
                .andExpect(status().isOk()).andExpect(content().json(responseBody));
    }

    /**
     * Test unlink a Gasket Kit with a Turbo.
     */
    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/part_controller/setgasketkitforpart_alreadylinked.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testClearGasketKitInPart() throws Exception {
        Turbo turbo = (Turbo) partDao.findOne(25861L);
        assertNotNull(turbo);
        assertNotNull(turbo.getGasketKit());
        assertEquals(69079L, turbo.getGasketKit().getId().longValue());
        String responseBody = "{\"class\":\"com.turbointernational.metadata.entity.part.types.Turbo\",\"id\":25861,\"manufacturer\":{\"id\":4,\"name\":\"Toyota\",\"type\":{\"id\":1,\"name\":\"turbo\"}},\"manufacturerPartNumber\":\"17201-0L020\",\"name\":null,\"description\":null,\"dimLength\":null,\"dimWidth\":null,\"dimHeight\":null,\"weight\":null,\"partType\":{\"id\":1,\"name\":\"Turbo\",\"value\":\"turbo\",\"magentoAttributeSet\":\"Turbo\"},\"inactive\":false,\"turboTypes\":[],\"productImages\":[],\"version\":1,\"legendImgFilename\":null,\"turboModel\":{\"id\":4013,\"name\":\"CT\",\"turboType\":{\"id\":138,\"manufacturer\":{\"id\":4,\"name\":\"Toyota\",\"type\":{\"id\":1,\"name\":\"turbo\"}},\"name\":\"CT\"}},\"coolType\":null,\"gasketKit\":null}";
        mockMvc.perform(delete("/metadata/part/25861/gasketkit").content("{}").contentType(contentType))
                .andExpect(status().isOk()).andExpect(content().json(responseBody));
        turbo = (Turbo) partDao.findOne(25861L);
        assertNotNull(turbo);
        assertNull(turbo.getGasketKit());
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/part_controller/createxrefpart_turbo.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    // The test is not actual after migration to ArangoDb.
    // Broken after migration code snippets are commented out with label 'ARANGODB'.
    @Ignore("This test is not real because real database uses triggers which are absent in a test database.")
    public void testCreateXRefPart_Turbo() throws Exception {
        String requestBody = "{\"originalPartId\":14510,\"part\":{\"partType\":{\"id\":1,\"name\":\"Turbo\",\"value\":\"turbo\",\"magentoAttributeSet\":\"Turbo\"},\"name\":\"nnn\",\"manufacturer\":{\"id\":2,\"name\":\"Holset\",\"route\":\"other/manufacturer/list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":null,\"restangularCollection\":false},\"manufacturerPartNumber\":\"3534378-test\",\"description\":\"ddd\",\"turboModel\":{\"id\":339,\"name\":\"H1E\",\"turboType\":{\"id\":260,\"manufacturer\":{\"id\":2,\"name\":\"Holset\",\"type\":{\"id\":1,\"name\":\"turbo\"},\"importPK\":3},\"name\":\"H1E\"},\"route\":\"turboModel\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":{\"route\":\"other\",\"parentResource\":null},\"restangularCollection\":false},\"turboType\":{\"id\":260,\"manufacturer\":{\"id\":2,\"name\":\"Holset\",\"type\":{\"id\":1,\"name\":\"turbo\"},\"importPK\":3},\"name\":\"H1E\",\"route\":\"list\",\"reqParams\":null,\"restangularized\":true,\"fromServer\":true,\"parentResource\":{\"route\":\"other/turboType\",\"parentResource\":null},\"restangularCollection\":false},\"class\":\"com.turbointernational.metadata.entity.part.types.Turbo\"}}";
        String responseBody = "{\"class\":\"com.turbointernational.metadata.entity.part.types.Turbo\",\"id\":14511,\"manufacturer\":{\"id\":2,\"name\":\"Holset\"},\"manufacturerPartNumber\":\"3534378-test\",\"name\":\"nnn\",\"description\":\"ddd\",\"dimLength\":null,\"dimWidth\":null,\"dimHeight\":null,\"weight\":null,\"partType\":{\"id\":1,\"name\":\"Turbo\",\"value\":\"turbo\",\"magentoAttributeSet\":\"Turbo\"},\"inactive\":false,\"turboTypes\":[],\"interchange\":{\"id\":1,\"alone\":true},\"productImages\":[],\"version\":0,\"legendImgFilename\":null,\"turboModel\":{\"id\":339,\"name\":\"H1E\",\"turboType\":{\"id\":260,\"manufacturer\":{\"id\":2,\"name\":\"Holset\",\"type\":{\"id\":1,\"name\":\"turbo\"}},\"name\":\"H1E\"}},\"coolType\":null,\"gasketKit\":null}";
        mockMvc.perform(post("/metadata/xrefpart").content(requestBody).contentType(contentType))
                .andExpect(status().isOk()).andExpect(content().json(responseBody));
        // .andDo(MockMvcResultHandlers.print());
        List<Part> resultList = em.createQuery("from Part p where p.manufacturerPartNumber=?", Part.class)
                .setParameter(1, "3534378").getResultList();
        assertEquals(1, resultList.size());
        Part p = resultList.get(0);
        assertNotNull(p);
        // ARANGODB: Interchange interchange = p.getInterchange();
        // ARANGODB: assertNotNull("The interchange was not saved.", interchange);
        Part originalPart = partDao.findOne(14510L);
        // ARANGODB: boolean contains = interchange.getParts().contains(originalPart);
        // ARANGODB: assertTrue("The part not found in the interchangeables.", contains);
    }

}