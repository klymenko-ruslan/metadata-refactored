package com.turbointernational.metadata.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.turbointernational.metadata.AbstractFunctionalWebIT;
import com.turbointernational.metadata.dao.CarModelEngineYearDao;
import com.turbointernational.metadata.entity.CarEngine;
import com.turbointernational.metadata.entity.CarModel;
import com.turbointernational.metadata.entity.CarModelEngineYear;
import com.turbointernational.metadata.entity.CarYear;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-12-22.
 */
public class ApplicationControllerIT extends AbstractFunctionalWebIT {

    @Autowired
    private CarModelEngineYearDao carModelEngineYearDao;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/application/controller/create_application.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    public void testCreate() throws Exception {
        String requestBody = "{\"model\":{\"id\":992},\"engine\":{\"id\":306},\"year\":{\"name\":1983}}";
        String responseBody = "1";
        mockMvc.perform(post("/metadata/application/carmodelengineyear").content(requestBody).contentType(contentType))
                .andExpect(status().isOk()).andExpect(content().json(responseBody));
        CarModelEngineYear cmey = carModelEngineYearDao.findOne(1L);
        assertNotNull(cmey);
        CarModel model = cmey.getModel();
        assertNotNull(model);
        assertEquals(new Long(992L), model.getId());
        CarEngine engine = cmey.getEngine();
        assertNotNull(engine);
        assertEquals(new Long(306), engine.getId());
        CarYear year = cmey.getYear();
        assertNotNull(year);
        assertEquals("1983", year.getName());
    }

}