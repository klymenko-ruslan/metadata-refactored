package com.turbointernational.metadata.web.controller;

import com.turbointernational.metadata.dao.CarModelEngineYearDao;
import com.turbointernational.metadata.entity.CarEngine;
import com.turbointernational.metadata.entity.CarModel;
import com.turbointernational.metadata.entity.CarModelEngineYear;
import com.turbointernational.metadata.entity.CarYear;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/22/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
@Transactional
@SqlConfig(
        dataSource = "dataSource",
        transactionManager = "transactionManagerMetadata"
)
public class ApplicationControllerTest {

    @Autowired
    private CarModelEngineYearDao carModelEngineYearDao;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MediaType contentType = new MediaType(APPLICATION_JSON.getType(),
            APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/application/controller/create_application.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    @WithUserDetails("Admin")
    public void testCreate() throws Exception {
        String requestBody = "{\"model\":{\"id\":992},\"engine\":{\"id\":306},\"year\":{\"name\":1983}}";
        String responseBody = "1";
        mockMvc.perform(post("/metadata/application/carmodelengineyear")
                .content(requestBody).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
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