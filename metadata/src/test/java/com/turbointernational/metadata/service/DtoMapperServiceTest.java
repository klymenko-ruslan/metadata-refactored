package com.turbointernational.metadata.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.spi.MappingContext;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.part.types.Actuator;
import com.turbointernational.metadata.entity.part.types.Backplate;
import com.turbointernational.metadata.entity.part.types.BackplateSealplate;
import com.turbointernational.metadata.entity.part.types.BearingHousing;
import com.turbointernational.metadata.entity.part.types.BoltScrew;
import com.turbointernational.metadata.entity.part.types.CarbonSeal;
import com.turbointernational.metadata.entity.part.types.Cartridge;
import com.turbointernational.metadata.entity.part.types.Clamp;
import com.turbointernational.metadata.web.dto.Manufacturer;
import com.turbointernational.metadata.web.dto.Part;
import com.turbointernational.metadata.web.dto.PartType;

@RunWith(MockitoJUnitRunner.class)
public class DtoMapperServiceTest {

    @Mock
    private PartDao partDao;

    @Mock
    private MappingContext<Long, Part> mappingContextLong2Part;

    @InjectMocks
    private DtoMapperService dtoMapperService;

    private com.turbointernational.metadata.entity.PartType entityPartType;

    private com.turbointernational.metadata.entity.Manufacturer entityManufacturer;

    private com.turbointernational.metadata.entity.part.Part entityPart;

    @Before
    public void beforeAll() {
        dtoMapperService.init();
        entityPartType = new com.turbointernational.metadata.entity.PartType();
        entityPartType.setId(34L);
        entityPartType.setName("Backplate");
        entityManufacturer = new com.turbointernational.metadata.entity.Manufacturer();
        entityManufacturer.setId(11L);
        entityManufacturer.setName("Turbo International");
        entityPart = new Backplate();
        entityPart.setId(49576L);
        entityPart.setName("test name");
        entityPart.setDescription("test description");
        entityPart.setManufacturerPartNumber("5-A-4915");
        entityPart.setManufacturer(entityManufacturer);
        entityPart.setPartType(entityPartType);
    }

    /**
     * Test mapping of a JPA entity Part to a DTO part.
     */
    @Test
    public void testMapOfPartType() {
        PartType dtoPartType = dtoMapperService.map(entityPartType, PartType.class);
        assertNotNull(dtoPartType);
        assertEquals((Long) 34L, dtoPartType.getId());
        assertEquals("Backplate", dtoPartType.getName());
    }

    /**
     * Test mapping of a JPA entity Maufacturer to a DTO Manufacturer.
     */
    @Test
    public void testMapOfManufacturer() {
        Manufacturer dtoManufacturer = dtoMapperService.map(entityManufacturer, Manufacturer.class);
        assertNotNull(dtoManufacturer);
        assertEquals((Long) 11L, dtoManufacturer.getId());
        assertEquals("Turbo International", dtoManufacturer.getName());
    }

    @Test
    public void testMapOfActuator() {
        com.turbointernational.metadata.entity.PartType entityPartTypeActuator = new com.turbointernational.metadata.entity.PartType();
        entityPartTypeActuator.setId(30L);
        entityPartTypeActuator.setName("Actuator");
        com.turbointernational.metadata.entity.Manufacturer manufacturerTurboInternational = new com.turbointernational.metadata.entity.Manufacturer();
        manufacturerTurboInternational.setId(11L);
        manufacturerTurboInternational.setName("Turbo International");
        Actuator actuator = new Actuator();
        actuator.setId(63497L);
        actuator.setName(null);
        actuator.setDescription("*ND* ACTUATOR, K03");
        actuator.setManufacturerPartNumber("9-D-6079");
        actuator.setManufacturer(manufacturerTurboInternational);
        actuator.setPartType(entityPartTypeActuator);
        Part dtoPart = dtoMapperService.map(actuator, Part.class);
        assertNotNull(dtoPart);
        assertEquals((Long) 63497L, dtoPart.getPartId());
        assertEquals("9-D-6079", dtoPart.getPartNumber());
        assertNull(dtoPart.getName());
        assertEquals("*ND* ACTUATOR, K03", dtoPart.getDescription());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 30L, dtoPart.getPartType().getId());
        assertEquals("Actuator", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 11L, dtoPart.getManufacturer().getId());
        assertEquals("Turbo International", dtoPart.getManufacturer().getName());
    }

    @Test
    public void testMapOfBackplate() {
        com.turbointernational.metadata.entity.PartType entityPartTypeBackplate = new com.turbointernational.metadata.entity.PartType();
        entityPartTypeBackplate.setId(34L);
        entityPartTypeBackplate.setName("Backplate");
        com.turbointernational.metadata.entity.Manufacturer manufacturerTurboInternational = new com.turbointernational.metadata.entity.Manufacturer();
        manufacturerTurboInternational.setId(11L);
        manufacturerTurboInternational.setName("Turbo International");
        Backplate backplate = new Backplate();
        backplate.setId(49576L);
        backplate.setName(null);
        backplate.setDescription("*DL2* BP, GT2259");
        backplate.setManufacturerPartNumber("5-A-4915");
        backplate.setManufacturer(manufacturerTurboInternational);
        backplate.setPartType(entityPartTypeBackplate);
        Part dtoPart = dtoMapperService.map(backplate, Part.class);
        assertNotNull(dtoPart);
        assertEquals((Long) 49576L, dtoPart.getPartId());
        assertEquals("5-A-4915", dtoPart.getPartNumber());
        assertNull(dtoPart.getName());
        assertEquals("*DL2* BP, GT2259", dtoPart.getDescription());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 34L, dtoPart.getPartType().getId());
        assertEquals("Backplate", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 11L, dtoPart.getManufacturer().getId());
        assertEquals("Turbo International", dtoPart.getManufacturer().getName());
    }

    @Test
    public void testMapOfBackplateSealplate() {
        com.turbointernational.metadata.entity.PartType entityPartTypeBackplateSealplate = new com.turbointernational.metadata.entity.PartType();
        entityPartTypeBackplateSealplate.setId(14L);
        entityPartTypeBackplateSealplate.setName("Backplate / Sealplate");
        com.turbointernational.metadata.entity.Manufacturer manufacturerGarrett = new com.turbointernational.metadata.entity.Manufacturer();
        manufacturerGarrett.setId(1L);
        manufacturerGarrett.setName("Garrett");
        BackplateSealplate backplateSealplate = new BackplateSealplate();
        backplateSealplate.setId(44767L);
        backplateSealplate.setName("Backplate");
        backplateSealplate.setDescription("removed interchangeability from KKK part 5304-151-5703");
        backplateSealplate.setManufacturerPartNumber("5304-151-5703");
        backplateSealplate.setManufacturer(manufacturerGarrett);
        backplateSealplate.setPartType(entityPartTypeBackplateSealplate);
        Part dtoPart = dtoMapperService.map(backplateSealplate, Part.class);
        assertNotNull(dtoPart);
        assertEquals((Long) 44767L, dtoPart.getPartId());
        assertEquals("5304-151-5703", dtoPart.getPartNumber());
        assertEquals("Backplate", dtoPart.getName());
        assertEquals("removed interchangeability from KKK part 5304-151-5703", dtoPart.getDescription());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 14L, dtoPart.getPartType().getId());
        assertEquals("Backplate / Sealplate", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 1L, dtoPart.getManufacturer().getId());
        assertEquals("Garrett", dtoPart.getManufacturer().getName());
    }

    @Test
    public void testMapOfBearingHousing() {
        com.turbointernational.metadata.entity.PartType entityPartTypeBearingHousing = new com.turbointernational.metadata.entity.PartType();
        entityPartTypeBearingHousing.setId(13L);
        entityPartTypeBearingHousing.setName("Bearing Housing");
        com.turbointernational.metadata.entity.Manufacturer manufacturerGarrett = new com.turbointernational.metadata.entity.Manufacturer();
        manufacturerGarrett.setId(1L);
        manufacturerGarrett.setName("Garrett");
        BearingHousing bearingHousing = new BearingHousing();
        bearingHousing.setId(43891L);
        bearingHousing.setName("Bearing Housing");
        bearingHousing.setDescription(null);
        bearingHousing.setManufacturerPartNumber("430027-0021");
        bearingHousing.setManufacturer(manufacturerGarrett);
        bearingHousing.setPartType(entityPartTypeBearingHousing);
        Part dtoPart = dtoMapperService.map(bearingHousing, Part.class);
        assertNotNull(dtoPart);
        assertEquals((Long) 43891L, dtoPart.getPartId());
        assertEquals("430027-0021", dtoPart.getPartNumber());
        assertEquals("Bearing Housing", dtoPart.getName());
        assertNull(dtoPart.getDescription());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 13L, dtoPart.getPartType().getId());
        assertEquals("Bearing Housing", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 1L, dtoPart.getManufacturer().getId());
        assertEquals("Garrett", dtoPart.getManufacturer().getName());
    }

    @Test
    public void testMapOfBoltScrew() {
        com.turbointernational.metadata.entity.PartType entityPartTypeBoltScrew = new com.turbointernational.metadata.entity.PartType();
        entityPartTypeBoltScrew.setId(35L);
        entityPartTypeBoltScrew.setName("Bolt - Screw");
        com.turbointernational.metadata.entity.Manufacturer manufacturerTurboInternational = new com.turbointernational.metadata.entity.Manufacturer();
        manufacturerTurboInternational.setId(11L);
        manufacturerTurboInternational.setName("Turbo International");
        BoltScrew boltScrew = new BoltScrew();
        boltScrew.setId(47751L);
        boltScrew.setName("BOLT, T18, 5/16-18, C/END");
        boltScrew.setDescription("BOLT, T18, 5/16-18, C/END");
        boltScrew.setManufacturerPartNumber("8-A-0152");
        boltScrew.setManufacturer(manufacturerTurboInternational);
        boltScrew.setPartType(entityPartTypeBoltScrew);
        Part dtoPart = dtoMapperService.map(boltScrew, Part.class);
        assertNotNull(dtoPart);
        assertEquals((Long) 47751L, dtoPart.getPartId());
        assertEquals("8-A-0152", dtoPart.getPartNumber());
        assertEquals("BOLT, T18, 5/16-18, C/END", dtoPart.getName());
        assertEquals("BOLT, T18, 5/16-18, C/END", dtoPart.getDescription());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 35L, dtoPart.getPartType().getId());
        assertEquals("Bolt - Screw", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 11L, dtoPart.getManufacturer().getId());
        assertEquals("Turbo International", dtoPart.getManufacturer().getName());
    }

    @Test
    public void testMapOfCarbonSeal() {
        com.turbointernational.metadata.entity.PartType entityPartTypeCarbonSeal = new com.turbointernational.metadata.entity.PartType();
        entityPartTypeCarbonSeal.setId(48L);
        entityPartTypeCarbonSeal.setName("Carbon Seal");
        com.turbointernational.metadata.entity.Manufacturer manufacturerGarrett = new com.turbointernational.metadata.entity.Manufacturer();
        manufacturerGarrett.setId(1L);
        manufacturerGarrett.setName("Garrett");
        CarbonSeal carbonSeal = new CarbonSeal();
        carbonSeal.setId(44765L);
        carbonSeal.setName("ENCAPSULATED C/SEAL");
        carbonSeal.setDescription(null);
        carbonSeal.setManufacturerPartNumber("409695-0000");
        carbonSeal.setManufacturer(manufacturerGarrett);
        carbonSeal.setPartType(entityPartTypeCarbonSeal);
        Part dtoPart = dtoMapperService.map(carbonSeal, Part.class);
        assertNotNull(dtoPart);
        assertEquals((Long) 44765L, dtoPart.getPartId());
        assertEquals("409695-0000", dtoPart.getPartNumber());
        assertEquals("ENCAPSULATED C/SEAL", dtoPart.getName());
        assertNull(dtoPart.getDescription());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 48L, dtoPart.getPartType().getId());
        assertEquals("Carbon Seal", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 1L, dtoPart.getManufacturer().getId());
        assertEquals("Garrett", dtoPart.getManufacturer().getName());
    }

    @Test
    public void testMapOfCartridge() {
        com.turbointernational.metadata.entity.PartType entityPartTypeCartridge = new com.turbointernational.metadata.entity.PartType();
        entityPartTypeCartridge.setId(2L);
        entityPartTypeCartridge.setName("Cartridge");
        com.turbointernational.metadata.entity.Manufacturer manufacturerGarrett = new com.turbointernational.metadata.entity.Manufacturer();
        manufacturerGarrett.setId(1L);
        manufacturerGarrett.setName("Garrett");
        Cartridge cartridge = new Cartridge();
        cartridge.setId(501L);
        cartridge.setName(null);
        cartridge.setDescription(null);
        cartridge.setManufacturerPartNumber("712371-0051");
        cartridge.setManufacturer(manufacturerGarrett);
        cartridge.setPartType(entityPartTypeCartridge);
        Part dtoPart = dtoMapperService.map(cartridge, Part.class);
        assertNotNull(dtoPart);
        assertEquals((Long) 501L, dtoPart.getPartId());
        assertEquals("712371-0051", dtoPart.getPartNumber());
        assertNull(dtoPart.getName());
        assertNull(dtoPart.getDescription());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 2L, dtoPart.getPartType().getId());
        assertEquals("Cartridge", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 1L, dtoPart.getManufacturer().getId());
        assertEquals("Garrett", dtoPart.getManufacturer().getName());
    }

    @Test
    public void testMapOfClamp() {
        com.turbointernational.metadata.entity.PartType entityPartTypeClamp = new com.turbointernational.metadata.entity.PartType();
        entityPartTypeClamp.setId(19L);
        entityPartTypeClamp.setName("Clamp");
        com.turbointernational.metadata.entity.Manufacturer manufacturerTurboInternational = new com.turbointernational.metadata.entity.Manufacturer();
        manufacturerTurboInternational.setId(11L);
        manufacturerTurboInternational.setName("Turbo International");
        Clamp clamp = new Clamp();
        clamp.setId(47775L);
        clamp.setName("LOCKPLATE, T18A, T/END");
        clamp.setDescription("LOCKPLATE, T18A, T/END");
        clamp.setManufacturerPartNumber("8-A-0190");
        clamp.setManufacturer(manufacturerTurboInternational);
        clamp.setPartType(entityPartTypeClamp);
        Part dtoPart = dtoMapperService.map(clamp, Part.class);
        assertNotNull(dtoPart);
        assertEquals((Long) 47775L, dtoPart.getPartId());
        assertEquals("8-A-0190", dtoPart.getPartNumber());
        assertEquals("LOCKPLATE, T18A, T/END", dtoPart.getName());
        assertEquals("LOCKPLATE, T18A, T/END", dtoPart.getDescription());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 19L, dtoPart.getPartType().getId());
        assertEquals("Clamp", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 11L, dtoPart.getManufacturer().getId());
        assertEquals("Turbo International", dtoPart.getManufacturer().getName());
    }

    /**
     * Test conversion of a part ID to a Part (DTO).
     */
    @Test
    public void testPartId2PartConvertor() {
        // Stubbing.
        when(mappingContextLong2Part.getSource()).thenReturn( /* part ID */ 49576L);
        when(partDao.findOne(49576L)).thenReturn(entityPart);
        // Run.
        Part dtoPart = dtoMapperService.partId2Part.convert(mappingContextLong2Part);
        assertNotNull(dtoPart);
        assertEquals((Long) 49576L, dtoPart.getPartId());
        assertEquals("5-A-4915", dtoPart.getPartNumber());
        assertEquals("test name", dtoPart.getName());
        assertEquals("test description", dtoPart.getDescription());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 34L, dtoPart.getPartType().getId());
        assertEquals("Backplate", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 11L, dtoPart.getManufacturer().getId());
        assertEquals("Turbo International", dtoPart.getManufacturer().getName());
    }

}
