package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.AbstractFunctionalIT;
import com.turbointernational.metadata.entity.part.Part;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-17.
 */
public class StandardOversizePartDaoIT extends AbstractFunctionalIT {

    @Autowired
    private StandardOversizePartDao standardOversizePartDao;

    /**
     * #912
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
            scripts = "classpath:integration_tests/standardoversizepart_dao/findoversizeparts.sql"
    )
    @Sql(
            executionPhase = AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    public void findOversizeParts() throws Exception {
        List<Part> oversizeParts = standardOversizePartDao.findOversizeParts(46718L);
        assertEquals(2, oversizeParts.size());
    }

    /**
     * #912
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
            scripts = "classpath:integration_tests/standardoversizepart_dao/findoversizeparts.sql"
    )
    @Sql(
            executionPhase = AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    public void findStandardParts() throws Exception {
        List<Part> oversizeParts = standardOversizePartDao.findStandardParts(46719L);
        assertEquals(1, oversizeParts.size());
    }

}