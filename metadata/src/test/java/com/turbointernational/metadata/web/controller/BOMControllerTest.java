package com.turbointernational.metadata.web.controller;

import com.turbointernational.metadata.entity.chlogsrc.ChangelogSource;
import com.turbointernational.metadata.entity.chlogsrc.ChangelogSourceLink;
import com.turbointernational.metadata.entity.chlogsrc.Source;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.nio.charset.Charset;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by dmytro.trunykov@zorallabs.com on 22-01-2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
@Transactional
@SqlConfig(
        dataSource = "dataSource",
        transactionManager = "transactionManagerMetadata"
)
public class BOMControllerTest {

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
            scripts = "classpath:integration_tests/bom_controller/create_changelog_source.sql"
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
    public void testCreateChangelogSource() throws Exception {
        String requestBody = "{\"parentPartId\":\"14510\",\"rows\":[{\"childPartId\":17754,\"quantity\":1}],\"sourceIds\":[1,2],\"chlogSrcRatings\":[2,0],\"chlogSrcLnkDescription\":\"Hello world!\"}";
        String responseBody = "{\"failures\":[],\"boms\":[{\"class\":\"com.turbointernational.metadata.entity.BOMItem\",\"id\":1,\"parent\":{\"class\":\"com.turbointernational.metadata.entity.part.types.Turbo\",\"id\":14510,\"manufacturer\":{\"id\":2,\"name\":\"Holset\"},\"manufacturerPartNumber\":\"3534378\",\"name\":null,\"dimLength\":null,\"dimWidth\":null,\"dimHeight\":null,\"weight\":null,\"partType\":{\"id\":1,\"name\":\"Turbo\",\"value\":\"turbo\",\"magentoAttributeSet\":\"Turbo\"},\"version\":1,\"legendImgFilename\":null},\"child\":{\"class\":\"com.turbointernational.metadata.entity.part.types.Turbo\",\"id\":17754,\"manufacturer\":{\"id\":2,\"name\":\"Holset\"},\"manufacturerPartNumber\":\"3768655\",\"name\":null,\"dimLength\":null,\"dimWidth\":null,\"dimHeight\":null,\"weight\":null,\"partType\":{\"id\":1,\"name\":\"Turbo\",\"value\":\"turbo\",\"magentoAttributeSet\":\"Turbo\"},\"version\":1,\"legendImgFilename\":null},\"quantity\":1}]}";
        mockMvc.perform(post("/metadata/bom")
                .content(requestBody).contentType(contentType))
                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(responseBody));;
        // Check that description for the link exists.
        ChangelogSourceLink link = em.find(ChangelogSourceLink.class, 1L);
        assertNotNull(link);
        assertEquals("Hello world!", link.getDescription());
        // Check that 'source(s)' exist (actually it is created by *.sql script before the test).
        Source source = em.find(Source.class, 1L);
        assertNotNull(source);
        source = em.find(Source.class, 2L);
        assertNotNull(source);
        // Check that two links between two sources and a record in the changelog have been created.
        List<ChangelogSource> chlgSrcs = em.createQuery("from ChangelogSource", ChangelogSource.class)
                .getResultList();
        assertNotNull(chlgSrcs);
        assertEquals(2, chlgSrcs.size());
    }

}