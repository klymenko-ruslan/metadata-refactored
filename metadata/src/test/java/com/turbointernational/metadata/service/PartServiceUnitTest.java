package com.turbointernational.metadata.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.turbointernational.metadata.entity.Manufacturer;
import com.turbointernational.metadata.entity.ManufacturerType;
import com.turbointernational.metadata.entity.PartType;
import com.turbointernational.metadata.entity.TurboType;
import com.turbointernational.metadata.entity.part.types.Kit;
import com.turbointernational.metadata.entity.part.types.kit.KitType;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class PartServiceUnitTest {

    private PartService partService;

    @Before
    public void before() {
        this.partService = new PartService();
    }

    @Test
    public void testPartJsonSerializer() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(11L);
        manufacturer.setName("Turbo International");
        manufacturer.setNotExternal(false);
        ManufacturerType manufacturerType = new ManufacturerType();
        manufacturerType.setId(1L);
        manufacturerType.setName("turbo");
        manufacturer.setType(manufacturerType);
        PartType partType = new PartType();
        partType.setId(3L);
        partType.setName("Kit");
        partType.setValue("kit");
        partType.setMagentoAttributeSet("Kit");
        KitType kitType = new KitType();
        kitType.setId(12L);
        kitType.setName("Service");
        Manufacturer ttManufacturer = new Manufacturer();
        ttManufacturer.setId(1L);
        ttManufacturer.setName("Garret");
        ttManufacturer.setNotExternal(false);
        ManufacturerType ttManufacturerType = new ManufacturerType();
        ttManufacturerType.setId(1L);
        ttManufacturerType.setName("turbo");
        ttManufacturer.setType(ttManufacturerType);
        TurboType turboType = new TurboType();
        turboType.setId(717L);
        turboType.setName("TE27");
        turboType.setManufacturer(ttManufacturer);
        Kit part = new Kit();
        part.setId(41407L);
        part.setManufacturer(manufacturer);
        part.setManufacturerPartNumber("7-A-0007");
        part.setName("dynamic seal");
        part.setDescription("KIT, SERVICE, T25, BOLTED HSG.");
        part.setPartType(partType);
        part.setVersion(3);
        part.getTurboTypes().add(turboType);
        part.setKitType(kitType);
        String json = partService.partJsonSerializer.serialize(part);
        assertEquals("{\"description\":\"KIT, SERVICE, T25, BOLTED HSG.\",\"id\":41407,\"inactive\":false," +
        "\"manufacturer\":{\"id\":11,\"name\":\"Turbo International\"},\"manufacturerPartNumber\":\"7-A-0007\"," +
                "\"name\":\"dynamic seal\",\"partType\":{\"id\":3,\"name\":\"Kit\"}}", json);
    }

}
