package com.turbointernational.metadata.dao;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.turbointernational.metadata.AbstractFunctionalIT;
import com.turbointernational.metadata.web.dto.AlsoBought;
import com.turbointernational.metadata.web.dto.Page;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class PartDaoIT extends AbstractFunctionalIT {

    @Autowired
    private PartDao partDao;

    @Test
    @Ignore  // This test is used for development only.
    public void testFilterAlsoBough() {
        Page<AlsoBought> page = partDao.filterAlsoBough("8-F-0431", null, null, null, null, null, null);    // 535 recs
        // Page<AlsoBought> page = partDao.filterAlsoBough("8-F-0431", "4", "CHR", null, null, null, null); // 147 recs
        Assert.assertNotNull("Returned 'page' is null.", page);
        Assert.assertNotNull("Records in the 'page' was not initialized (null).", page.getRecs());
        Assert.assertEquals("Number records in the 'page' and member 'total' are different.", page.getRecs().size(),
                page.getTotal());
//System.out.println("TOTAL: " + page.getTotal());
    }

}
