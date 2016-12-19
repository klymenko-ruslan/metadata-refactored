package com.turbointernational.metadata.web.controller;

import com.turbointernational.metadata.Application;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Created by dmytro.trunykov@zorallabs.com on 12/19/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("integration")
@Transactional
@SqlConfig(
        dataSource = "dataSource",
        transactionManager = "transactionManagerMetadata"
)
public class PartControllerTest {

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
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    @WithUserDetails("Admin")
    public void testCreatePart() throws Exception {
        String requestBody = "{" +
                "   \"origin\":{" +
                "       \"turboModel\":{" +
                "           \"turboType\":{}" +
                "       }," +
                "       \"partType\":{" +
                "           \"id\":30," +
                "           \"name\":\"Actuator\"," +
                "           \"value\":\"actuator\"," +
                "           \"magentoAttributeSet\":\"Actuator\"," +
                "           \"route\":\"parttype/json\"," +
                "           \"reqParams\":null," +
                "           \"restangularized\":true," +
                "           \"fromServer\":true," +
                "           \"parentResource\":null," +
                "           \"restangularCollection\":false" +
                "       }," +
                "       \"name\":\"fff\"," +
                "       \"description\":\"dddd\"," +
                "       \"manufacturer\":{" +
                "           \"id\":11," +
                "           \"name\":\"Turbo International\"," +
                "           \"route\":\"other/manufacturer/list\"," +
                "           \"reqParams\":null," +
                "           \"restangularized\":true," +
                "           \"fromServer\":true," +
                "           \"parentResource\":null," +
                "           \"restangularCollection\":false" +
                "       }," +
                "       \"class\":\"com.turbointernational.metadata.entity.part.types.Actuator\"" +
                "    }," +
                "    \"partNumbers\":[\"409043-0033\"]}";
        String responseBody = "{" +
                "    \"results\":[{" +
                "       \"partId\":69449," +
                "       \"manufacturerPartNumber\":\"409043-0033\"," +
                "       \"success\":true," +
                "       \"errorMessage\":null" +
                "    }]}";
        mockMvc.perform(post("/metadata/part")
                .content(requestBody).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody));
    }

}