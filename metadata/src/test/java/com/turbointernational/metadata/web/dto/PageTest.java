package com.turbointernational.metadata.web.dto;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@RunWith(Parameterized.class)
public class PageTest {

    private Long fPageAtotal;
    private List<?> fPageArecs;
    private Long fPageBtotal;
    private List<?> fPageBrecs;
    private boolean fIsEqual;

    //@formatter:off
    @Parameters
    public static List<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            { 0L, null, 0L, null, true },
            { 0L, new ArrayList<Object>(), 0L, null, false },
            { 0L, null, 0L, new ArrayList<Object>(), false },
            { 3L, asList(1, 2, 3), 3L, asList(1, 2, 3), true },
            { 3L, asList(1, 2), 3L, asList(1, 2, 3), false },
            { 3L, asList(1, 2, 3), 3L, asList(3, 2, 1), false },
        });
    }
    //@formatter:on

    public PageTest(Long pageAtotal, List<?> pageArecs, Long pageBtotal, List<?> pageBrecs, boolean isEqual) {
        fPageAtotal = pageAtotal;
        fPageArecs = pageArecs;
        fPageBtotal = pageBtotal;
        fPageBrecs = pageBrecs;
        fIsEqual = isEqual;
    }

    @Test
    public void testEqualsObject() {
        Page<?> pageA = new Page<>(fPageAtotal, fPageArecs);
        Page<?> pageB = new Page<>(fPageBtotal, fPageBrecs);
        assertEquals(fIsEqual, pageA.equals(pageB));
    }

}
