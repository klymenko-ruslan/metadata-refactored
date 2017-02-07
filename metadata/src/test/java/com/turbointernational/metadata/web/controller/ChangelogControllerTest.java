package com.turbointernational.metadata.web.controller;

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

import javax.persistence.EntityManager;
import java.nio.charset.Charset;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-03.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
@Transactional
@SqlConfig(
        dataSource = "dataSource",
        transactionManager = "transactionManagerMetadata"
)
public class ChangelogControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MediaType contentType = new MediaType(APPLICATION_JSON.getType(),
            APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = asList(converters).stream()
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
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/changelog_controller/changelog_for_part.sql"
    )
    @Sql(
            executionPhase = AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    @WithUserDetails("Admin")
    public void findChangelogsForPart() throws Exception {
        mockMvc.perform(get("/metadata/changelog/part/25861")
                .contentType(contentType))
                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
                .andExpect(content().json("[{\"id\":2,\"service\":\"BOM\",\"changeDate\":1485087883000,\"user\":{\"id\":1,\"name\":\"Admin\",\"email\":\"admin@gmail.com\",\"username\":\"Admin\"},\"description\":\"test\"},{\"id\":3,\"service\":\"BOM\",\"changeDate\":1485087884000,\"user\":{\"id\":1,\"name\":\"Admin\",\"email\":\"admin@gmail.com\",\"username\":\"Admin\"},\"description\":\"test\"},{\"id\":4,\"service\":\"BOM\",\"changeDate\":1485087885000,\"user\":{\"id\":1,\"name\":\"Admin\",\"email\":\"admin@gmail.com\",\"username\":\"Admin\"},\"description\":\"test\"}]"));
    }

}