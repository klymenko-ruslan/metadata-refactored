package com.turbointernational.metadata.web.controller;

import static com.turbointernational.metadata.entity.ChangelogPart.Role.BOM_CHILD;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.BOM_PARENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.turbointernational.metadata.AbstractFunctionalWebIT;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.chlogsrc.ChangelogSource;
import com.turbointernational.metadata.entity.chlogsrc.ChangelogSourceLink;
import com.turbointernational.metadata.entity.chlogsrc.Source;

/**
 * Created by dmytro.trunykov@zorallabs.com on 22-01-2017.
 */
public class BOMControllerIT extends AbstractFunctionalWebIT {

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/bom_controller/create_changelog_source.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    @Ignore // TODO: migrate to ArangoDB
    public void testCreateChangelogSource() throws Exception {
        String requestBody = "{\"parentPartId\":\"14510\",\"rows\":[{\"childPartId\":17754,\"quantity\":1}],\"sourcesIds\":[1,2],\"chlogSrcRatings\":[2,0],\"chlogSrcLnkDescription\":\"Hello world!\"}";
        String responseBody = "{\"failures\":[],\"boms\":[{\"class\":\"com.turbointernational.metadata.entity.BOMItem\",\"id\":1,\"parent\":{\"class\":\"com.turbointernational.metadata.entity.part.types.Turbo\",\"id\":14510,\"manufacturer\":{\"id\":2,\"name\":\"Holset\"},\"manufacturerPartNumber\":\"3534378\",\"name\":null,\"dimLength\":null,\"dimWidth\":null,\"dimHeight\":null,\"weight\":null,\"partType\":{\"id\":1,\"name\":\"Turbo\",\"value\":\"turbo\",\"magentoAttributeSet\":\"Turbo\"},\"version\":1,\"legendImgFilename\":null},\"child\":{\"class\":\"com.turbointernational.metadata.entity.part.types.Turbo\",\"id\":17754,\"manufacturer\":{\"id\":2,\"name\":\"Holset\"},\"manufacturerPartNumber\":\"3768655\",\"name\":null,\"dimLength\":null,\"dimWidth\":null,\"dimHeight\":null,\"weight\":null,\"partType\":{\"id\":1,\"name\":\"Turbo\",\"value\":\"turbo\",\"magentoAttributeSet\":\"Turbo\"},\"version\":1,\"legendImgFilename\":null},\"quantity\":1}]}";
        mockMvc.perform(post("/metadata/bom").content(requestBody).contentType(contentType)).andExpect(status().isOk())
                // .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(responseBody));
        // Check that description for the link exists.
        ChangelogSourceLink link = em.find(ChangelogSourceLink.class, 1L);
        assertNotNull(link);
        assertEquals("Hello world!", link.getDescription());
        // Check that 'source(s)' exist (actually it is created by *.sql script
        // before the test).
        Source source = em.find(Source.class, 1L);
        assertNotNull(source);
        source = em.find(Source.class, 2L);
        assertNotNull(source);
        // Check that two links between two sources and a record in the
        // changelog have been created.
        List<ChangelogSource> chlgSrcs = em.createQuery("from ChangelogSource", ChangelogSource.class).getResultList();
        assertEquals(2, chlgSrcs.size());
        // Check that parts ids for this operation are saved (ticket #906).
        List<ChangelogPart> chlgPrts = em.createQuery("from ChangelogPart", ChangelogPart.class).getResultList();
        assertEquals(2, chlgPrts.size());
        Map<Long, ChangelogPart> mcp = new HashMap<>(2); // part id =>
                                                         // ChangelogPart
        chlgPrts.stream().forEach(cp -> mcp.put(cp.getPart().getId(), cp));
        ChangelogPart cpForParent = mcp.get(new Long(14510));
        assertNotNull(cpForParent);
        assertEquals(BOM_PARENT, cpForParent.getRole());
        ChangelogPart cpForChild = mcp.get(new Long(17754));
        assertNotNull(cpForChild);
        assertEquals(BOM_CHILD, cpForChild.getRole());
    }

}