package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.services.CriticalDimensionService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by trunikov on 5/16/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
//@ActiveProfiles("integration")
@Transactional
public class PartTest {

    @Autowired
    private PartDao partDao;

    @Autowired
    private CriticalDimensionService criticalDimensionService;

    @Test
    @Ignore
    public void testToJson() throws Exception {
        Part p = partDao.findOne(44024L);
        String json = p.toJson(criticalDimensionService.getCriticalDimensionForPartType(p.getPartType().getId()));
        System.out.println(json);
    }

}