package com.turbointernational.metadata.dao;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.Optional;

import javax.persistence.Tuple;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.turbointernational.metadata.AbstractFunctionalTest;
import com.turbointernational.metadata.web.dto.Page;

public class GroupDaoTest extends AbstractFunctionalTest {

    @Autowired
    private GroupDao groupDao;

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/group_dao/filter.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testFilter() {
        Page<Tuple> page = groupDao.filter(1L, Optional.empty(), Optional.empty(), Optional.of(Boolean.TRUE) /*Optional.empty()*/,
                Optional.of("name"), Optional.of("asc"), Optional.of(0), Optional.of(5));
        System.out.println(page);
    }

}
