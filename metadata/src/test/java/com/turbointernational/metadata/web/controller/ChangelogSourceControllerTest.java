package com.turbointernational.metadata.web.controller;

import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.chlogsrc.*;
import org.junit.Before;
import org.junit.Ignore;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-01-24 12:59.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
@Transactional
@SqlConfig(
        dataSource = "dataSource",
        transactionManager = "transactionManagerMetadata"
)
public class ChangelogSourceControllerTest {

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

    /**
     * Test that source which is unlinked and without attachment(s) can be deleted.
     *
     * @throws Exception
     */
    @Test
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/changelogsource_controller/create_changelog_source-0.sql"
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
    public void testDeleteUnlinked() throws Exception {
        // Check that a source exists before test.
        em.find(Source.class, 1L); // it throws exception when not found
        mockMvc.perform(delete("/metadata/changelog/source/1")
                .contentType(contentType))
                .andExpect(status().isOk());
        List<Source> sources = em.createQuery("from Source", Source.class).getResultList();
        assertEquals(0, sources.size());
    }

    /**
     * Test that source which is unlinked and with attachment(s) can be deleted.
     *
     * @throws Exception
     */
    @Test
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/changelogsource_controller/create_changelog_source-1.sql"
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
    public void testDeleteUnlinkedWithAttachment() throws Exception {
        // Check that a source exists before test.
        em.find(Source.class, 1L); // it throws exception when not found
        // Check that a source attachment exists before test.
        em.find(SourceAttachment.class, 1L); // it throws exception when not found
        mockMvc.perform(delete("/metadata/changelog/source/1")
                .contentType(contentType))
                .andExpect(status().isOk());
        List<SourceAttachment> sourcesAttachments = em.createQuery("from SourceAttachment", SourceAttachment.class)
                .getResultList();
        assertEquals(0, sourcesAttachments.size());
        List<Source> sources = em.createQuery("from Source", Source.class).getResultList();
        assertEquals(0, sources.size());
    }

    /**
     * Test that source which is linked and with attachment(s) can be deleted.
     *
     * @throws Exception
     */
    @Test
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/changelogsource_controller/create_changelog_source-2.sql"
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
    public void testDeleteLinkedWithAttachment() throws Exception {
        // Check that a source exists before test.
        Source source = em.find(Source.class, 1L); // it throws exception when not found
        // Check that a source attachment exists before test.
        em.find(SourceAttachment.class, 1L); // it throws exception when not found
        // Check that a changelog exists before test.
        Changelog changelog = em.find(Changelog.class, 1L); // it throws exception when not found
        // Check that changelog and source are linked before test.
        ChangelogSourceLink link = em.find(ChangelogSourceLink.class, 1L); // it throws exception when not found
        em.find(ChangelogSource.class, new ChangelogSourceId(link, source)); // it throws exception when not found
        mockMvc.perform(delete("/metadata/changelog/source/1")
                .contentType(contentType))
                .andExpect(status().isOk());
        List<SourceAttachment> sourcesAttachments = em.createQuery("from SourceAttachment", SourceAttachment.class)
                .getResultList();
        assertEquals(0, sourcesAttachments.size());
        List sources = em.createQuery("from Source s", Source.class).getResultList();
        assertEquals(0, sources.size());
        List<ChangelogSourceLink> links = em.createQuery("from ChangelogSourceLink", ChangelogSourceLink.class)
                .getResultList();
        assertEquals(0, links.size());
        List<Changelog> changelogs = em.createQuery("from Changelog", Changelog.class).getResultList();
        assertEquals(1, changelogs.size()); // a changelog still exists
    }

    @Test
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/changelogsource_controller/get_links_count.sql"
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
    public void testGetLinksCount() throws Exception {
        mockMvc.perform(get("/metadata/changelog/source/2/links/count")
                .contentType(contentType))
                .andExpect(status().isOk())
  //              .andDo(MockMvcResultHandlers.print());
                .andExpect(content().json("3"));
    }

    @Test
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/changelogsource_controller/get_last_picked.sql"
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
    public void testGetLastPicked() throws Exception {
        String responseBody = "[{\"id\":1,\"sourceName\":{\"id\":2,\"name\":\"email\"},\"name\":\"name-0\",\"description\":\"name-0 email\",\"url\":null,\"created\":1485085344000,\"updated\":1485085344000,\"lastLinked\":1485087882000},{\"id\":2,\"sourceName\":{\"id\":2,\"name\":\"email\"},\"name\":\"name-1\",\"description\":\"name-1 email\",\"url\":null,\"created\":1485085344000,\"updated\":1485085344000,\"lastLinked\":1485087451000}]";
        mockMvc.perform(get("/metadata/changelog/source/lastpicked")
                .contentType(contentType))
                .andExpect(status().isOk())
 //               .andDo(MockMvcResultHandlers.print());
                .andExpect(content().json(responseBody));
    }

    @Test
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/changelogsource_controller/delete_attachment.sql"
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
    public void testRemoveAttachment() throws Exception {
        String responseBody = "{\"rows\":[{\"id\":1,\"name\":\"test-1\",\"description\":null},{\"id\":3,\"name\":\"test-3\",\"description\":null}]}";
        mockMvc.perform(delete("/metadata/changelog/source/attachment/2?begin=true")
                .contentType(contentType))
                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
                .andExpect(content().json(responseBody));
        List<SourceAttachment> attachments = em.createQuery("from SourceAttachment a order by a.id", SourceAttachment.class).getResultList();
        assertEquals(2, attachments.size());
        assertEquals(1L, attachments.get(0).getId().longValue());
        assertEquals(3L, attachments.get(1).getId().longValue());
    }

    @Test
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/changelogsource_controller/filter_changelogsource.sql"
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
    @Ignore
    public void testFilterChangelogSources() throws Exception {
        mockMvc.perform(get("/metadata/changelog/source/1/links")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}