package com.turbointernational.metadata.util;

import static com.turbointernational.metadata.util.FilterUtils.DateRange.LASTMONTH;
import static com.turbointernational.metadata.util.FilterUtils.DateRange.LASTWEEK;
import static com.turbointernational.metadata.util.FilterUtils.DateRange.LASTYEAR;
import static com.turbointernational.metadata.util.FilterUtils.DateRange.THISMONTH;
import static com.turbointernational.metadata.util.FilterUtils.DateRange.THISWEEK;
import static com.turbointernational.metadata.util.FilterUtils.DateRange.THISYEAR;
import static com.turbointernational.metadata.util.FilterUtils.DateRange.TODAY;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.FEBRUARY;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.util.Pair;

public class FilterUtilsTest {

    // 2018-02-22T14:16:00 [Europe/Brussels]
    private Date now;

    @Before
    public void before() {
        Calendar c = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/Brussels"));
        c.set(2018, FEBRUARY, 22, 14, 16);
        now = c.getTime();
    }

    @Test
    public void testDateRangeForToday() {
        Pair<Date, Date> rangeFor = FilterUtils.DateRange.rangeFor(now, TODAY);
        assertNotNull(rangeFor);
        Date d0 = rangeFor.getFirst();
        assertDate(d0, 2018, FEBRUARY, 22, false);
        Date d1 = rangeFor.getSecond();
        assertDate(d1, 2018, FEBRUARY, 22, true);
    }

    @Test
    public void testDateRangeForThisWeek() {
        Pair<Date, Date> rangeFor = FilterUtils.DateRange.rangeFor(now, THISWEEK);
        assertNotNull(rangeFor);
        Date d0 = rangeFor.getFirst();
        assertDate(d0, 2018, FEBRUARY, 19, false);
        Date d1 = rangeFor.getSecond();
        assertDate(d1, 2018, FEBRUARY, 25, true);
    }

    @Test
    public void testDateRangeForLastWeek() {
        Pair<Date, Date> rangeFor = FilterUtils.DateRange.rangeFor(now, LASTWEEK);
        assertNotNull(rangeFor);
        Date d0 = rangeFor.getFirst();
        assertDate(d0, 2018, FEBRUARY, 12, false);
        Date d1 = rangeFor.getSecond();
        assertDate(d1, 2018, FEBRUARY, 18, true);
    }

    @Test
    public void testDateRangeForThisMonth() {
        Pair<Date, Date> rangeFor = FilterUtils.DateRange.rangeFor(now, THISMONTH);
        assertNotNull(rangeFor);
        Date d0 = rangeFor.getFirst();
        assertDate(d0, 2018, FEBRUARY, 1, false);
        Date d1 = rangeFor.getSecond();
        assertDate(d1, 2018, FEBRUARY, 28, true);
    }

    @Test
    public void testDateRangeForLastMonth() {
        Pair<Date, Date> rangeFor = FilterUtils.DateRange.rangeFor(now, LASTMONTH);
        assertNotNull(rangeFor);
        Date d0 = rangeFor.getFirst();
        assertDate(d0, 2018, JANUARY, 1, false);
        Date d1 = rangeFor.getSecond();
        assertDate(d1, 2018, JANUARY, 31, true);
    }

    @Test
    public void testDateRangeForThisYear() {
        Pair<Date, Date> rangeFor = FilterUtils.DateRange.rangeFor(now, THISYEAR);
        assertNotNull(rangeFor);
        Date d0 = rangeFor.getFirst();
        assertDate(d0, 2018, JANUARY, 1, false);
        Date d1 = rangeFor.getSecond();
        assertDate(d1, 2018, DECEMBER, 31, true);
    }

    @Test
    public void testDateRangeForLastYear() {
        Pair<Date, Date> rangeFor = FilterUtils.DateRange.rangeFor(now, LASTYEAR);
        assertNotNull(rangeFor);
        Date d0 = rangeFor.getFirst();
        assertDate(d0, 2017, JANUARY, 1, false);
        Date d1 = rangeFor.getSecond();
        assertDate(d1, 2017, DECEMBER, 31, true);
    }

    /**
     * @param d
     * @param year
     * @param month
     * @param day
     * @param dayEdge
     *            false - begin of a day, true - end of a day
     */
    private void assertDate(Date d, int year, int month, int day, boolean dayEdge) {
        assertNotNull(d);
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(d);
        assertEquals(year, c.get(YEAR));
        assertEquals(month, c.get(MONTH));
        assertEquals(day, c.get(DAY_OF_MONTH));
        if (dayEdge) { // end of a day
            assertEquals(23, c.get(HOUR_OF_DAY));
            assertEquals(59, c.get(MINUTE));
            assertEquals(59, c.get(SECOND));
            assertEquals(999, c.get(MILLISECOND));
        } else { // begin of a day
            assertEquals(0, c.get(HOUR_OF_DAY));
            assertEquals(0, c.get(MINUTE));
            assertEquals(0, c.get(SECOND));
            assertEquals(0, c.get(MILLISECOND));
        }
    }

}
