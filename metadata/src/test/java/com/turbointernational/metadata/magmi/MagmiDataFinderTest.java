
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
@ContextConfiguration
public class MagmiDataFinderTest extends AbstractTransactionalJUnit4SpringContextTests {
    
    MagmiDataFinder instance;
    
    @Before
    public void setUp() {
        instance = new MagmiDataFinder();
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
    }
    
}
