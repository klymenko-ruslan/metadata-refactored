package com.turbointernational.metadata.services.search.parser;

import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.utils.RegExpUtils;

import java.util.regex.Matcher;

import static com.turbointernational.metadata.services.search.parser.SearchTermCmpOperatorEnum.*;
import static com.turbointernational.metadata.services.search.parser.SearchTermEnum.DECIMAL;
import static com.turbointernational.metadata.services.search.parser.SearchTermEnum.INTEGER;
import static com.turbointernational.metadata.utils.RegExpUtils.PTRN_DOUBLE_LIMIT;

/**
 * Created by dmytro.trunykov@zorallabs.com on 08.08.16.
 */
public class SearchTermFactory {

    public static TextSearchTerm newTextSearchTerm(String fieldName, String term) {
        return new TextSearchTerm(fieldName, term);
    }

    public static BooleanSearchTerm newBooleanSearchTerm(String fieldName, Boolean term) {
        return new BooleanSearchTerm(fieldName, term);
    }

    public static NumberSearchTerm newIntegerSearchTerm(String fieldName, SearchTermCmpOperatorEnum cmpOperator, Long term) {
        return new NumberSearchTerm(INTEGER, fieldName, cmpOperator, term);
    }

    public static AbstractSearchTerm newSearchTerm(CriticalDimension cd, String s) {
        AbstractSearchTerm retVal;
        Range pr;
        String idxName = cd.getIdxName();
        CriticalDimension.DataTypeEnum dataType = cd.getDataType();
        switch (dataType) {
            case DECIMAL:
                pr = parseSearchStr(s);
                if (pr.isBoundLimit()) {
                    retVal = new RangeSearchTerm(idxName, pr.limit1.val, pr.limit2.val);
                } else {
                    retVal = new NumberSearchTerm(DECIMAL, idxName, pr.limit1.operator, pr.limit1.val);
                }
                break;
            case ENUMERATION:
                Long n = Long.valueOf(s);
                retVal = new NumberSearchTerm(INTEGER, idxName, EQ, n);
                break;
            case INTEGER:
                pr = parseSearchStr(s);
                if (pr.isBoundLimit()) {
                    retVal = new RangeSearchTerm(idxName, pr.limit1.val, pr.limit2.val);
                } else {
                    retVal = new NumberSearchTerm(INTEGER, idxName, pr.limit1.operator, pr.limit1.val);
                }
                break;
            case TEXT:
                retVal = newTextSearchTerm(idxName, s);
                break;
            default:
                throw new AssertionError("Unsupported data type: " + dataType);
        }
        return retVal;
    }

    static Limit parseLimit(String s) throws IllegalArgumentException {
        if (s == null) {
            return null;
        }
        Matcher matcher = PTRN_DOUBLE_LIMIT.matcher(s);
        if (matcher.matches()) {
            SearchTermCmpOperatorEnum operator = SearchTermCmpOperatorEnum.fromSign(matcher.group(2));
            if (operator == null) {
                operator = EQ;
            }
            Double val = RegExpUtils.parseDouble(matcher.group(3));
            if (val == null) {
                throw new IllegalArgumentException("Invalid limit: " + s);
            }
            return new Limit(operator, val);
        } else {
            throw new NumberFormatException(s);
        }
    }

    static Range parseRange(String s) throws IllegalArgumentException {
        if (s == null) {
            return null;
        }
        int p0 = s.indexOf("..");
        if (p0 < 1) {
            throw new IllegalArgumentException("Invalid range: " + s);
        }
        // Check presence of the low limit.
        int n = s.length();
        if (n - 2 == p0) {
            throw new IllegalArgumentException("Invalid range: " + s);
        }
        int p1 = p0 + 2;
        while(s.charAt(p1) == '.' && p1 < n) {
            p1++;
        }
        // Find the high limit.
        String s0 = s.substring(0, p0);
        String s1 = s.substring(p1);
        Double r0 = RegExpUtils.parseDouble(s0);
        Double r1 = RegExpUtils.parseDouble(s1);
        Limit limit0 = new Limit(GTE, r0);
        Limit limit1 = new Limit(LTE, r1);
        return new Range(limit0, limit1);
    }

    static Range parseSearchStr(String s) throws IllegalArgumentException {
        if (s == null) {
            return null;
        }
        try {
            Limit iLimit = parseLimit(s);
            return new Range(iLimit, null);
        } catch (IllegalArgumentException e) {
            // ignore
        }
        try {
            return parseRange(s);
        } catch (IllegalArgumentException e) {
            // ignore
        }
        throw new IllegalArgumentException("Parsing failed: " + s);
    }

    static class Limit {

        final SearchTermCmpOperatorEnum operator;
        final Number val;

        Limit(SearchTermCmpOperatorEnum operator, Number val) {
            this.operator = operator;
            this.val = val;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Limit limit = (Limit) o;

            if (operator != limit.operator) return false;
            return val != null ? val.equals(limit.val) : limit.val == null;

        }

        @Override
        public String toString() {
            return "Limit{" +
                    "operator=" + operator +
                    ", val=" + val +
                    '}';
        }

    }

    static class Range {

        final Limit limit1;
        final Limit limit2;

        public Range(Limit limit1, Limit limit2) {
            this.limit1 = limit1;
            this.limit2 = limit2;
        }

        boolean isBoundLimit() {
            return limit1 != null && limit2 != null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Range range = (Range) o;

            if (limit1 != null ? !limit1.equals(range.limit1) : range.limit1 != null) return false;
            return limit2 != null ? limit2.equals(range.limit2) : range.limit2 == null;

        }

        @Override
        public String toString() {
            return "Range{" +
                    "limit1=" + limit1 +
                    ", limit2=" + limit2 +
                    '}';
        }

    }

}
