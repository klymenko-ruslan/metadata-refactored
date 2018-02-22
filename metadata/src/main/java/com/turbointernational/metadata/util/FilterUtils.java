package com.turbointernational.metadata.util;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.SUNDAY;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.data.util.Pair;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class FilterUtils {

    public static enum DateRange {

        TODAY, THISWEEK, LASTWEEK, THISMONTH, LASTMONTH, THISYEAR, LASTYEAR;

        public static Pair<Date, Date> rangeFor(DateRange dateRange) {
            Date now = new Date();
            return rangeFor(now, dateRange);
        }

        public static Pair<Date, Date> rangeFor(Date now, DateRange dateRange) {
            if (dateRange == null) {
                throw new NullPointerException("Input paramter dateRange can't be null.");
            }
            Calendar c = GregorianCalendar.getInstance();
            c.setTime(now);
            Calendar c0 = normalizeDate(c, false);
            Calendar c1 = normalizeDate(c, true);
            switch (dateRange) {
            case TODAY:
                break;
            case THISWEEK:
                c0.set(DAY_OF_WEEK, MONDAY);
                c1.set(DAY_OF_WEEK, MONDAY);
                c1.add(DAY_OF_MONTH, 6);
                break;
            /*
            case LASTWEEK:
                break;
            case THISMONTH:
                break;
            case LASTMONTH:
                break;
            case THISYEAR:
                break;
            case LASTYEAR:
                break;
            */
            default:
                throw new AssertionError("Unsupported date range: " + dateRange);
            }
            return Pair.of(c0.getTime(), c1.getTime());
        }

        private static Calendar normalizeDate(Calendar original, boolean dayEdge) {
            Calendar c = (Calendar) original.clone();
            if (dayEdge) { // end of a day
                c.set(HOUR_OF_DAY, 23);
                c.set(MINUTE, 59);
                c.set(SECOND, 59);
                c.set(MILLISECOND, 999);
            } else { // begin of a day
                c.set(HOUR_OF_DAY, 0);
                c.set(MINUTE, 0);
                c.set(SECOND, 0);
                c.set(MILLISECOND, 0);
            }
            return c;
        }
    }

}
