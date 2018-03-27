package com.turbointernational.metadata.entity.part;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.service.CriticalDimensionService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by trunikov on 5/16/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
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