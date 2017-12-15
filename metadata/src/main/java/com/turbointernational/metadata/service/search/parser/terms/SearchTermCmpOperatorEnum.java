package com.turbointernational.metadata.service.search.parser.terms;

/**
 * Created by dmytro.trunykov@zorallabs.com on 08.08.16.
 */
public enum SearchTermCmpOperatorEnum {

    LT("<"), LTE("<="), EQ("="), GTE(">="), GT(">");

    final String sign;

    SearchTermCmpOperatorEnum(String sign) {
        this.sign = sign;
    }

    static SearchTermCmpOperatorEnum fromSign(String s) throws IllegalArgumentException {
        if (s == null) {
            return null;
        }
        s = s.trim();
        if (s.equals("<")) {
            return LT;
        } else if (s.equals("<=")) {
            return LTE;
        } else if (s.equals("=")) {
            return EQ;
        } else if (s.equals(">=")) {
            return GTE;
        } else if (s.equals(">")) {
            return GT;
        }
        throw new IllegalArgumentException("Can't parse sing: " + s);
    }

}
