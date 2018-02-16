package com.turbointernational.metadata.service;

import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
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
import com.turbointernational.metadata.entity.part.types.CompressorCover;
import com.turbointernational.metadata.entity.part.types.CompressorWheel;
import com.turbointernational.metadata.entity.part.types.Fitting;
import com.turbointernational.metadata.entity.part.types.Gasket;
import com.turbointernational.metadata.entity.part.types.GasketKit;
import com.turbointernational.metadata.entity.part.types.HeatshieldShroud;
import com.turbointernational.metadata.entity.part.types.JournalBearing;
import com.turbointernational.metadata.entity.part.types.JournalBearingSpacer;
import com.turbointernational.metadata.entity.part.types.Kit;
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
import com.turbointernational.metadata.entity.part.types.kit.KitComponent;
import com.turbointernational.metadata.service.GraphDbService.GetAncestorsResponse;
import com.turbointernational.metadata.service.GraphDbService.GetBomsResponse;
import com.turbointernational.metadata.service.GraphDbService.GetInterchangeResponse;
import com.turbointernational.metadata.web.dto.Ancestor;
import com.turbointernational.metadata.web.dto.Bom;
import com.turbointernational.metadata.web.dto.CommonComponent;
import com.turbointernational.metadata.web.dto.Interchange;
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

    private com.turbointernational.metadata.entity.PartType entityKitPartType;

    private com.turbointernational.metadata.entity.Manufacturer entityKitManufacturer;

    private com.turbointernational.metadata.entity.part.types.Kit entityKit;

    private com.turbointernational.metadata.entity.part.types.kit.KitType entityKitKitType;

    @Before
    public void before() {
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

        entityKitPartType = new com.turbointernational.metadata.entity.PartType();
        entityKitPartType.setId(3L);
        entityKitPartType.setName("Kit");
        entityKitManufacturer = new com.turbointernational.metadata.entity.Manufacturer();
        entityKitManufacturer.setId(1L);
        entityKitManufacturer.setName("Garret");
        entityKit = new Kit();
        entityKit.setId(40270L);
        entityKit.setName("KIT, REPAIR, TV/T18, 020-0177");
        entityKit.setManufacturerPartNumber("020-0177W");
        entityKit.setManufacturer(entityKitManufacturer);
        entityKit.setPartType(entityKitPartType);
        entityKitKitType = new com.turbointernational.metadata.entity.part.types.kit.KitType();
        entityKitKitType.setId(1L);
        entityKitKitType.setName("Floating Carbon Seal"); 
        entityKit.setKitType(entityKitKitType);
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

    @Test
    public void testMapInterchange() {
        Long headerId = 1233L;
        Long[] parts = new Long[] { 49576L };
        GetInterchangeResponse response = new GetInterchangeResponse(headerId, parts);
        // when(mappingContextLong2Part.getSource()).thenReturn( /* part ID */ 49576L);
        when(partDao.findOne(49576L)).thenReturn(entityPart);
        Interchange interchange = dtoMapperService.map(response, Interchange.class);
        assertNotNull(interchange);
        assertEquals(headerId, interchange.getId());
        assertNotNull(interchange.getParts());
        assertEquals(1, interchange.getParts().length);
        Part dtoPart = interchange.getParts()[0];
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

    @Test
    public void testMapBom() {
        GetBomsResponse.Row row = new GetBomsResponse.Row();
        com.turbointernational.metadata.entity.Manufacturer interchangePartManufacturer = new com.turbointernational.metadata.entity.Manufacturer();
        interchangePartManufacturer.setId(11L);
        interchangePartManufacturer.setName("Turbo International");
        com.turbointernational.metadata.entity.PartType interchangePartType = new com.turbointernational.metadata.entity.PartType();
        interchangePartType.setId(12L);
        interchangePartType.setName("Turbine Wheel");
        com.turbointernational.metadata.entity.part.Part interchagnePart = new Backplate();
        interchagnePart.setId(42768L);
        interchagnePart.setName("Turbo Wheel");
        interchagnePart.setDescription("S/W, T3, LH");
        interchagnePart.setManufacturerPartNumber("2-A-1894");
        interchagnePart.setManufacturer(interchangePartManufacturer);
        interchagnePart.setPartType(interchangePartType);
        row.setPartId(49576L);
        row.setQty(4);
        row.setInterchanges(new Long[] { 42768L });
        when(partDao.findOne(49576L)).thenReturn(entityPart); // BOM part
        when(partDao.findOne(42768L)).thenReturn(interchagnePart);
        Bom dtoBom = dtoMapperService.map(row, Bom.class);
        assertNotNull(dtoBom);
        Part dtoPart = dtoBom.getPart();
        assertNotNull(dtoPart);
        assertEquals((Long) 49576L, dtoPart.getPartId());
        assertEquals("5-A-4915", dtoPart.getPartNumber());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 34L, dtoPart.getPartType().getId());
        assertEquals("Backplate", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 11L, dtoPart.getManufacturer().getId());
        assertEquals("Turbo International", dtoPart.getManufacturer().getName());
        assertEquals((Integer) 4, dtoBom.getQty());
        assertNotNull(row.getInterchanges());
        assertEquals(1, row.getInterchanges().length);
        Part dtoInterchangePart = dtoBom.getInterchanges()[0];
        assertNotNull(dtoInterchangePart);
        assertEquals((Long) 42768L, dtoInterchangePart.getPartId());
        assertEquals("2-A-1894", dtoInterchangePart.getPartNumber());
        assertEquals("Turbo Wheel", dtoInterchangePart.getName());
        assertEquals("S/W, T3, LH", dtoInterchangePart.getDescription());
        assertNotNull(dtoInterchangePart.getPartType());
        assertEquals((Long) 12L, dtoInterchangePart.getPartType().getId());
        assertEquals("Turbine Wheel", dtoInterchangePart.getPartType().getName());
        assertNotNull(dtoInterchangePart.getManufacturer());
        assertEquals((Long) 11L, dtoInterchangePart.getManufacturer().getId());
        assertEquals("Turbo International", dtoInterchangePart.getManufacturer().getName());
    }

    @Test
    @Ignore
    public void testMapAncestors() {
        GetAncestorsResponse.Row row = new GetAncestorsResponse.Row(49576L, true, 1);
        when(partDao.findOne(49576L)).thenReturn(entityPart);
        Ancestor dtoAncestor = dtoMapperService.map(row, Ancestor.class);
        assertNotNull(dtoAncestor);
        Part dtoPart = dtoAncestor.getPart();
        assertNotNull(dtoPart);
        assertEquals((Long) 49576L, dtoPart.getPartId());
        assertEquals("5-A-4915", dtoPart.getPartNumber());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 34L, dtoPart.getPartType().getId());
        assertEquals("Backplate", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 11L, dtoPart.getManufacturer().getId());
        assertEquals("Turbo International", dtoPart.getManufacturer().getName());
        assertNotNull(dtoAncestor.getRelationType());
        assertEquals(true, dtoAncestor.getRelationType());
        assertEquals(1, dtoAncestor.getRelationDistance());
    }

    @Test
    public void testMapCommonComponent() {
        KitComponent kitComponent = new KitComponent();
        kitComponent.setId(14L);
        kitComponent.setKit(entityKit);
        kitComponent.setPart(entityPart);
        kitComponent.setExclude(TRUE);
        CommonComponent dtoCommonComponent = dtoMapperService.map(kitComponent, CommonComponent.class);
        assertEquals((Long) 14L, dtoCommonComponent.getId());
        assertTrue(dtoCommonComponent.isExclude());
        Part dtoPart = dtoCommonComponent.getPart();
        assertNotNull(dtoPart);
        assertEquals((Long) 49576L, dtoPart.getPartId());
        assertEquals("5-A-4915", dtoPart.getPartNumber());
        assertNotNull(dtoPart.getPartType());
        assertEquals((Long) 34L, dtoPart.getPartType().getId());
        assertEquals("Backplate", dtoPart.getPartType().getName());
        assertNotNull(dtoPart.getManufacturer());
        assertEquals((Long) 11L, dtoPart.getManufacturer().getId());
        assertEquals("Turbo International", dtoPart.getManufacturer().getName());
        com.turbointernational.metadata.web.dto.Kit dtoKit = dtoCommonComponent.getKit();
        assertNotNull(dtoKit);
        assertEquals((Long) 40270L, dtoKit.getPartId());
        assertEquals("020-0177W", dtoKit.getPartNumber());
        assertNotNull(dtoKit.getPartType());
        assertEquals((Long) 3L, dtoKit.getPartType().getId());
        assertEquals("Kit", dtoKit.getPartType().getName());
        assertNotNull(dtoKit.getManufacturer());
        assertEquals((Long) 1L, dtoKit.getManufacturer().getId());
        assertEquals("Garret", dtoKit.getManufacturer().getName());
        assertNotNull(dtoKit.getKitType());
        assertEquals((Long) 1L, dtoKit.getKitType().getId());
        assertEquals("Floating Carbon Seal", dtoKit.getKitType().getName());
    }

    /**
     * Test mapping of various Part descendants.
     */
    @RunWith(Parameterized.class)
    public static class PartMappingTests {

        private DtoMapperService dtoMapperService;

        private Long fPartTypeId;

        private String fPartTypeName;

        private Long fManufacturerId;

        private String fManufacturerName;

        private Class<? extends com.turbointernational.metadata.entity.part.Part> fPartClass;

        private Long fPartId;

        private String fPartName;

        private String fPartDescription;

        private String fPartManufacturerPartNumber;

        //@formatter:off
        @Parameters
        public static List<Object[]> parameters() {
            return Arrays.asList(new Object[][] { 
                { 30L, "Actuator", 11L, "Turbo International",
                  Actuator.class, 63497L, null, "*ND* ACTUATOR, K03", "9-D-6079" },
                { 34L, "Backplate", 11L, "Turbo International",
                  Backplate.class, 49576L, null, "*DL2* BP, GT2259", "5-A-4915" },
                { 14L, "Backplate / Sealplate", 1L, "Garrett",
                  BackplateSealplate.class, 44767L, "Backplate",
                  "removed interchangeability from KKK part 5304-151-5703", "5304-151-5703" },
                { 13L, "Bearing Housing",  1L, "Garrett",
                  BearingHousing.class, 43891L, "Bearing Housing", null, "430027-0021" },
                { 35L, "Bolt - Screw", 11L, "Turbo International",
                  BoltScrew.class, 47751L, "BOLT, T18, 5/16-18, C/END",
                  "BOLT, T18, 5/16-18, C/END", "8-A-0152" },
                { 48L, "Carbon Seal", 1L, "Garrett", 
                  CarbonSeal.class, 44765L, "ENCAPSULATED C/SEAL", null, "409695-0000" },
                { 2L, "Cartridge", 1L, "Garrett",
                  Cartridge.class, 501L, null, null, "712371-0051" },
                { 19L, "Clamp", 11L, "Turbo International",
                  Clamp.class, 47775L, "LOCKPLATE, T18A, T/END", "LOCKPLATE, T18A, T/END", "8-A-0190" },
                { 31L, "Compressor Cover", 11L, "Turbo International",
                  CompressorCover.class, 67924L, null, "*ND* COMP HSG, HX55", "13-B-5333" },
                { 11L, "Compressor Wheel", 2L, "Holset",
                  CompressorWheel.class, 43501L, "Compressor Wheel", null, "3521918" },
                // { 8L, "Fast Wearing Component"  },
                { 36L, "Fitting", 1L, "Garrett",
                  Fitting.class, 75306L, "Fitting oil inlet", null, "722531-0001" },
                { 6L, "Gasket", 1L, "Garrett",
                  Gasket.class, 45391L, "Gasket, oil inlet", null, "409036-0000" },
                { 49L, "Gasket Kit", 11L, "Turbo International",
                  GasketKit.class, 68896L, "GASKET KIT, GT1749V", "GASKET KIT, GT1749V", "7-A-6440" },
                { 15L, "Heatshield / Shroud", 8L, "Mitsubishi",
                  HeatshieldShroud.class, 45251L, "Heatshield / Shroud", null, "49166-11100" },
                { 5L, "Journal Bearing", 11L, "Turbo International",
                  JournalBearing.class, 45751L, "JNL BRG, TV75, 15/20", "JNL BRG, TV75, 15/20", "8-A-3406" },
                { 37L, "Journal Bearing Spacer", 11L, "Turbo International",
                  JournalBearingSpacer.class, 45487L, "BEARING SPACER, GT40, STD", "BEARING SPACER, GT40, STD", "8-A-2055" },
                { 3L, "Kit", 11L, "Turbo International",
                  Kit.class, 50052L, null, null, "7-A-2470" },
                // { 9L, "Major Component" },
                // { 10L, "Minor Component" },
                { 50L, "Misc", 1L, "Garrett", 
                  Misc.class, 75294L, "Hose", null, "400729-0053" },
                { 21L, "Miscellaneous Minor Components", 11L, "Turbo International",
                  MiscMinorComponent.class, 47814L, "RETAINER, SPRING", "RETAINER, SPRING", "8-A-0339" },
                { 16L, "Nozzle Ring", 1L, "Garrett",
                  NozzleRing.class, 45367L, "Nozzle Ring", null, "717505-0001" },
                { 38L, "Nut", 11L, "Turbo International",
                  Nut.class, 47760L, "LOCKNUT, T3/4, 1/4-28, RH", "SHAFT NUT, T3/4, 1/4-28, RH", "8-A-0163" },
                { 17L, "O Ring", 11L, "Turbo International",
                  ORing.class, 47757L, "O-RING, 41x1.8mm, S/PLT", "O-RING, 41x1.8mm, S/PLT", "8-A-0160" },
                { 18L, "Oil Deflector", 11L, "Turbo International",
                  OilDeflector.class, 47977L, "OIL DEFLECTOR", "OIL DEFLECTOR", "8-B-0115" },
                // { 51L, "Part" },
                { 39L, "Pin", 11L, "Turbo International",
                  Pin.class, 47863L, "ANTI-ROT PIN, JNL BRG, GT15/25", "ANTI-ROT PIN, JNL BRG, GT15/25", "8-A-1937" },
                { 4L, "Piston Ring", 3L, "I.H.I.",
                  PistonRing.class, 46495L, "Compressor End/Large", null, "070T-010" },
                { 32L, "Plug", 11L, "Turbo International",
                  Plug.class, 49833L, null, "test1", "8-F-2066" },
                { 40L, "Retaining Ring", 11L, "Turbo International",
                  RetainingRing.class, 47792L, "RET RING, JNL BRG", "RET RING, JNL BRG", "8-A-0251" },
                { 41L, "Seal Plate", 8L, "Mitsubishi",
                  SealPlate.class, 44984L, "Backplate / Sealplate", null, "49135-00044" },
                // { 52L, "Shroud" },
                { 42L, "Spring", 11L, "Turbo International",
                  Spring.class, 47785L, "THRUST SPRING, BACKPLATE", "THRUST SPRING, BACKPLATE", "8-A-0237" },
                { 43L, "Thrust Bearing", 11L, "Turbo International",
                  ThrustBearing.class, 47779L, "THRUST BRG, TE06", "THRUST BRG, TE06", "8-A-0198" },
                { 44L, "Thrust Collar", 11L, "Turbo International",
                  ThrustCollar.class, 47802L, "THRUST COLLAR, T3/4, C/SEAL", "THRUST COLLAR, T3/4, C/SEAL", "8-A-0290" },
                { 20L, "Thrust Parts", 11L, "Turbo International",
                  ThrustPart.class, 47888L, "*NLA* T/PLATE ASSY, T12", null, "8-A-2240" },
                { 45L, "Thrust Spacer", 11L, "Turbo International",
                  ThrustSpacer.class, 47781L, "THRUST COLLAR, TW94", "OIL FLINGER, TW94", "8-A-0209" },
                { 46L, "Thrust Washer", 11L, "Turbo International",
                  ThrustWasher.class, 47851L, "THRUST WSHR, T2, 360°", "THRUST WASHER, T2, 360°", "8-A-1825" }, 
                { 33L, "Turbine Housing", 11L, "Turbo International",
                  TurbineHousing.class, 67911L, null, "*ND* Turbine Housing, HX55", "12-B-5332" },
                { 12L, "Turbine Wheel", 9L, "Schwitzer",
                  TurbineWheel.class, 42751L, "Turbine Wheel", null, "315273" },
                { 1L, "Turbo", 1L, "Garrett",
                  Turbo.class, 6676L, null, null, "465354-0002" },
                { 47L, "Washer", 11L, "Turbo International",
                  Washer.class, 47752L, "WASHER, 8mm", "WASHER, 8mm", "8-A-0154" }
            });
        }
        //@formatter:on

        public PartMappingTests(Long partTypeId, String partTypeName, Long manufacturerId, String manufacturerName,
                Class<? extends com.turbointernational.metadata.entity.part.Part> partClass, Long partId,
                String partName, String partDescription, String partManufacturerPartNumber) {
            fPartTypeId = partTypeId;
            fPartTypeName = partTypeName;
            fManufacturerId = manufacturerId;
            fManufacturerName = manufacturerName;
            fPartClass = partClass;
            fPartId = partId;
            fPartName = partName;
            fPartDescription = partDescription;
            fPartManufacturerPartNumber = partManufacturerPartNumber;
        }

        @Before
        public void before() {
            dtoMapperService = new DtoMapperService();
            dtoMapperService.init();
        }

        @Test
        public void testMap() throws InstantiationException, IllegalAccessException {
            com.turbointernational.metadata.entity.PartType entityPartType = new com.turbointernational.metadata.entity.PartType();
            entityPartType.setId(fPartTypeId);
            entityPartType.setName(fPartTypeName);
            com.turbointernational.metadata.entity.Manufacturer entityManufacturer = new com.turbointernational.metadata.entity.Manufacturer();
            entityManufacturer.setId(fManufacturerId);
            entityManufacturer.setName(fManufacturerName);
            com.turbointernational.metadata.entity.part.Part entityPartDescendant = fPartClass.newInstance();
            entityPartDescendant.setId(fPartId);
            entityPartDescendant.setName(fPartName);
            entityPartDescendant.setDescription(fPartDescription);
            entityPartDescendant.setManufacturerPartNumber(fPartManufacturerPartNumber);
            entityPartDescendant.setManufacturer(entityManufacturer);
            entityPartDescendant.setPartType(entityPartType);
            Part dtoPart = dtoMapperService.map(entityPartDescendant, Part.class);
            assertNotNull(dtoPart);
            assertEquals(fPartId, dtoPart.getPartId());
            assertEquals(fPartManufacturerPartNumber, dtoPart.getPartNumber());
            assertEquals(fPartName, dtoPart.getName());
            assertEquals(fPartDescription, dtoPart.getDescription());
            assertNotNull(dtoPart.getPartType());
            assertEquals(fPartTypeId, dtoPart.getPartType().getId());
            assertEquals(fPartTypeName, dtoPart.getPartType().getName());
            assertNotNull(dtoPart.getManufacturer());
            assertEquals(fManufacturerId, dtoPart.getManufacturer().getId());
            assertEquals(fManufacturerName, dtoPart.getManufacturer().getName());
        }

    }
}
