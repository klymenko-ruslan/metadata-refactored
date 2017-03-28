package com.turbointernational.metadata.dao;

import static com.turbointernational.metadata.dao.GroupDao.ALIAS_GROUP_ID;
import static com.turbointernational.metadata.dao.GroupDao.ALIAS_MEMBER;
import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Tuple;

import org.junit.Ignore;
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
    public void testFilterEmpty() {
        Page<Tuple> page = groupDao.filter(null, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertNotNull(page);
        assertNotNull(page.getRecs());
        assertEquals(11, page.getRecs().size());
        assertEquals(11L, page.getTotal());
        // Make map group_id => is_member. The statement below throws exception
        // if encountered duplicated keys.
        Map<Long, Boolean> groupMembership = page.getRecs().stream()
                .collect(toMap(t -> t.get(ALIAS_GROUP_ID, Long.class), t -> t.get(ALIAS_MEMBER, Boolean.class)));
        assertEquals(11, groupMembership.size());
        List<Long> memberOfGroup = page.getRecs().stream().filter(t -> t.get(ALIAS_MEMBER, Boolean.class) == TRUE)
                .map(t -> t.get(ALIAS_GROUP_ID, Long.class)).collect(toList());
        assertTrue(memberOfGroup.isEmpty());
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/group_dao/filter.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testFilterPagination() {
        Page<Tuple> page = groupDao.filter(null, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(0), Optional.of(5));
        assertNotNull(page);
        assertNotNull(page.getRecs());
        assertEquals(5, page.getRecs().size());
        assertEquals(11L, page.getTotal());
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/group_dao/filter.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testFilterUser() {
        Page<Tuple> page = groupDao.filter(1L, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
        assertNotNull(page);
        assertNotNull(page.getRecs());
        assertEquals(11, page.getRecs().size());
        assertEquals(11L, page.getTotal());
        // Make map group_id => is_member. The statement below throws exception
        // if encountered duplicated keys.
        Map<Long, Boolean> groupMembership = page.getRecs().stream()
                .collect(toMap(t -> t.get(ALIAS_GROUP_ID, Long.class), t -> t.get(ALIAS_MEMBER, Boolean.class)));
        assertEquals(11, groupMembership.size());
        List<Long> memberOfGroup = page.getRecs().stream().filter(t -> t.get(ALIAS_MEMBER, Boolean.class) == TRUE)
                .map(t -> t.get(ALIAS_GROUP_ID, Long.class)).collect(toList());
        assertEquals(2, memberOfGroup.size());
        memberOfGroup.sort((n0, n1) -> n0.compareTo(n1));
        assertEquals(Long.valueOf(1L), memberOfGroup.get(0)); // group 'Reader'
        assertEquals(Long.valueOf(3L), memberOfGroup.get(1)); // group 'Admin'
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/group_dao/filter.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testFilterUserOrderByNameAsc() {
        Page<Tuple> page = groupDao.filter(1L, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of("name"), Optional.of("asc"), Optional.empty(), Optional.empty());
        assertNotNull(page);
        assertNotNull(page.getRecs());
        assertEquals(11, page.getRecs().size());
        assertEquals(11L, page.getTotal());
        List<Long> groupIds = page.getRecs().stream().map(t -> t.get(ALIAS_GROUP_ID, Long.class)).collect(toList());
        assertArrayEquals(new Long[] { 3L /* Admin */, 5L, 6L, 9L, 1L, 4L, 7L, 8L, 11L, 10L, 2L /* Writer */ },
                groupIds.toArray(new Long[groupIds.size()]));
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/group_dao/filter.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testFilterUserOrderByNameDesc() {
        Page<Tuple> page = groupDao.filter(1L, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of("name"), Optional.of("desc"), Optional.empty(), Optional.empty());
        assertNotNull(page);
        assertNotNull(page.getRecs());
        assertEquals(11, page.getRecs().size());
        assertEquals(11L, page.getTotal());
        List<Long> groupIds = page.getRecs().stream().map(t -> t.get(ALIAS_GROUP_ID, Long.class)).collect(toList());
        assertArrayEquals(new Long[] {2L /* Writer */, 10L, 11L, 8L, 7L, 4L, 1L, 9L, 6L, 5L, 3L /* Admin */},
                groupIds.toArray(new Long[groupIds.size()]));
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/group_dao/filter.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testFilterUserOrderByIsMemberAsc() {
        Page<Tuple> page = groupDao.filter(1L, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of("isMember"), Optional.of("asc"), Optional.empty(), Optional.empty());
        assertNotNull(page);
        assertNotNull(page.getRecs());
        assertEquals(11, page.getRecs().size());
        assertEquals(11L, page.getTotal());
        // Last two elements must be members.
        assertTrue(page.getRecs().get(9).get(ALIAS_MEMBER, Boolean.class));
        assertTrue(page.getRecs().get(10).get(ALIAS_MEMBER, Boolean.class));
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/group_dao/filter.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testFilterUserOrderByIsMemberDesc() {
        Page<Tuple> page = groupDao.filter(1L, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of("isMember"), Optional.of("desc"), Optional.empty(), Optional.empty());
        assertNotNull(page);
        assertNotNull(page.getRecs());
        assertEquals(11, page.getRecs().size());
        assertEquals(11L, page.getTotal());
        // First two elements must be members.
        assertTrue(page.getRecs().get(0).get(ALIAS_MEMBER, Boolean.class));
        assertTrue(page.getRecs().get(1).get(ALIAS_MEMBER, Boolean.class));
    }

    @Test
    @Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/group_dao/filter.sql")
    @Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @Ignore
    public void testFilterSort() {
        Page<Tuple> page = groupDao.filter(null, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of("name"), Optional.of("asc"), Optional.of(0), Optional.of(50));
        System.out.println(page);
    }

}
