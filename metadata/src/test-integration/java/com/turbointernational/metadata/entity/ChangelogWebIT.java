package com.turbointernational.metadata.entity;

import com.turbointernational.metadata.AbstractFunctionalIT;
import com.turbointernational.metadata.dao.ChangelogDao;
import com.turbointernational.metadata.dao.ChangelogPartDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-10.
 */
public class ChangelogWebIT extends AbstractFunctionalIT {

    @Autowired
    private ChangelogDao changelogDao;

    @Autowired
    private ChangelogPartDao changelogPartDao;

    /**
     * Test option <code>cascade</code> in a relation changelog <-> changelog_part.
     *
     * It is expected that removing a part in the changelog_part table does not remove parent record
     * in the changelog table.
     *
     * Test case:
     * <ul>
     *     <li>
     *         Prepare table <code>changelog</code> with one record.
     *     </li>
     *     <li>
     *         Prepare table <code>changelog_part</code> with two records corresponding
     *         to the record in the <code>changelog</code> table.</li>
     *     <li>
     *         Delete one record in the <code>changelog_part</code> table.
     *     </li>
     *     <li>
     *         Check that record in the <code>changelog</code> table was not removed
     *         by <code>cascade</code> propagation.
     *     </li>
     * </ul>
     */
    @Test
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/entity/changelogpart-delete.sql"
    )
    @Sql(
            executionPhase = AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    public void testJpaMappingChangelogPart() {
        ChangelogPart changelogPart = changelogPartDao.findOne(2L);
        changelogPartDao.remove(changelogPart);
        assertEquals(1, changelogDao.count());
        assertEquals(1, changelogPartDao.count());
    }

}