package com.turbointernational.metadata.web.controller;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import com.turbointernational.metadata.AbstractFunctionalWebIT;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-03.
 */
@Ignore // TODO: migrate to ArangoDB
public class ChangelogControllerIT extends AbstractFunctionalWebIT {

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/changelog_controller/changelog_for_part.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @WithUserDetails("Admin")
    // TODO: check permissions
    public void findChangelogsForPart() throws Exception {
        mockMvc.perform(
                get("/metadata/changelog/list?partId=25861&sortProperty=id&sortOrder=asc").contentType(contentType))
                .andExpect(status().isOk())
                // .andDo(MockMvcResultHandlers.print());
                .andExpect(content().json(
                        "{\"total\":3,\"recs\":[{\"id\":2,\"service\":\"BOM\",\"changeDate\":1485087883000,\"user\":{\"id\":1,\"name\":\"Admin\",\"email\":\"admin@gmail.com\",\"username\":\"Admin\"},\"description\":\"test\"},{\"id\":3,\"service\":\"BOM\",\"changeDate\":1485087884000,\"user\":{\"id\":1,\"name\":\"Admin\",\"email\":\"admin@gmail.com\",\"username\":\"Admin\"},\"description\":\"test\"},{\"id\":4,\"service\":\"BOM\",\"changeDate\":1485087885000,\"user\":{\"id\":1,\"name\":\"Admin\",\"email\":\"admin@gmail.com\",\"username\":\"Admin\"},\"description\":\"test\"}]}"));
    }

}