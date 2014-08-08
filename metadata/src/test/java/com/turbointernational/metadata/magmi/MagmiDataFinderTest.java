
package com.turbointernational.metadata.magmi;

import com.google.common.collect.Lists;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.other.TurboType;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.domain.part.types.Cartridge;
import com.turbointernational.metadata.domain.part.types.Turbo;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.type.ManufacturerType;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.magmi.dto.MagmiBomItem;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 *
 * @author jrodriguez
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
    "file:src/main/webapp/WEB-INF/applicationContext.xml",
    "file:src/main/webapp/WEB-INF/metadata-servlet.xml"})
public class MagmiDataFinderTest extends AbstractTransactionalJUnit4SpringContextTests {
    
    MagmiDataFinder instance;
    
    ManufacturerType manfrType;
    
    Manufacturer manfrTi;
    
    Manufacturer manfrGarrett;
    
    TurboModel tm42;
    
    TurboType tt42;
    
    PartType partTypeTurbo;
    
    PartType partTypeCartridge;
    
    CoolType coolTypeOil;
    
    Turbo turboGt1;

    Cartridge cartridgeGt1C1;

    Cartridge cartridgeGt1C2;
    
    @Before
    public void setUp() {
        
        instance = new MagmiDataFinder();
        
        manfrType = new ManufacturerType();
        manfrType.setName("turbo");
        ManufacturerType.entityManager().persist(manfrType);
        
        manfrTi = new Manufacturer();
        manfrTi.setName("Turbo International");
        manfrTi.setType(manfrType);
        Manufacturer.entityManager().persist(manfrTi);
        
        manfrGarrett = new Manufacturer();
        manfrGarrett.setName("Garrett");
        manfrGarrett.setType(manfrType);
        Manufacturer.entityManager().persist(manfrGarrett);
        
        tt42 = new TurboType();
        tt42.setManufacturer(manfrGarrett);
        tt42.setName("Turbo Type 42");
        TurboType.entityManager().persist(tt42);
        
        tm42 = new TurboModel();
        tm42.setName("Turbo Model 42");
        tm42.setTurboType(tt42);
        TurboModel.entityManager().persist(tm42);
        
        partTypeTurbo = new PartType();
        partTypeTurbo.setMagentoAttributeSet("Turbo");
        partTypeTurbo.setName("Turbo");
        PartType.entityManager().persist(partTypeTurbo);
        
        partTypeCartridge = new PartType();
        partTypeCartridge.setMagentoAttributeSet("Cartridge");
        partTypeCartridge.setName("Cartridge");
        PartType.entityManager().persist(partTypeCartridge);
        
        coolTypeOil = new CoolType();
        coolTypeOil.setName("Oil");
        CoolType.entityManager().persist(coolTypeOil);
        
        turboGt1 = new Turbo();
        turboGt1.setManufacturer(manfrGarrett);
        turboGt1.setManufacturerPartNumber("GT-1");
        turboGt1.setTurboModel(tm42);
        turboGt1.setPartType(partTypeTurbo);
        turboGt1.setCoolType(coolTypeOil);
        Turbo.entityManager().persist(turboGt1);

        cartridgeGt1C1 = new Cartridge();
        cartridgeGt1C1.setManufacturer(manfrGarrett);
        cartridgeGt1C1.setManufacturerPartNumber("GT-1-C-1");
        cartridgeGt1C1.setPartType(partTypeCartridge);
        Cartridge.entityManager().persist(cartridgeGt1C1);

        cartridgeGt1C2 = new Cartridge();
        cartridgeGt1C2.setManufacturer(manfrGarrett);
        cartridgeGt1C2.setManufacturerPartNumber("GT-1-C-2");
        cartridgeGt1C2.setPartType(partTypeCartridge);
        Cartridge.entityManager().persist(cartridgeGt1C2);
    }

    @Test
    public void testFindMagmiProducts() {
    }

    @Test
    public void testFindProductImages() {
    }

    @Test
    public void testFindMagmiApplications() {
    }

    @Test
    public void testFindMagmiTurbos() {
    }

    @Test
    public void testFindMagmiServiceKits() {
    }

    @Test
    public void testFindMagmiInterchanges() {
    }

    @Test
    public void testFindMagmiUsages() {
    }

    @Test
    public void testFindMagmiBom() {
        BOMItem bomItem1 = new BOMItem();
        bomItem1.setQuantity(1);
        bomItem1.setChild(cartridgeGt1C1);
        BOMItem.entityManager().persist(bomItem1);
        turboGt1.getBom().add(bomItem1);
        
        BOMItem bomItem2 = new BOMItem();
        bomItem2.setQuantity(1);
        bomItem2.setChild(cartridgeGt1C1);
        BOMItem.entityManager().persist(bomItem2);
        turboGt1.getBom().add(bomItem2);
        
        Turbo.entityManager().merge(turboGt1);
        
        List<MagmiBomItem> actual = instance.findMagmiBom(Lists.newArrayList(
            turboGt1.getId(),
            cartridgeGt1C1.getId(),
            cartridgeGt1C2.getId()
        ));
        
        assertEquals(2, actual.size());
    }
    
}
