package com.turbointernational.metadata.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.turbointernational.metadata.AbstractFunctionalTest;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class PartDaoTest extends AbstractFunctionalTest {
    
    @Autowired
    private PartDao partDao;

    @Test
    public void testFilterAlsoBough() {
        partDao.filterAlsoBough(null, "8-F-0431", null, null, null, null, null, null, null);
    }

}
