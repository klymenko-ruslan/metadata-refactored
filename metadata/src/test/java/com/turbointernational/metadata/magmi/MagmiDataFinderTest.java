
package com.turbointernational.metadata.magmi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 *
 * @author jrodriguez
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:WEB-INF/applicationContext.xml")
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
