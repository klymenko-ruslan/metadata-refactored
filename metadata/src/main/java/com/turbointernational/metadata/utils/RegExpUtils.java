package com.turbointernational.metadata.utils;

import javax.validation.constraints.Null;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dmytro.trunykov@zorallabs.com on 11.05.16.
 */
public class RegExpUtils {


    private final static String PTRN_MANUFACTURER_NUMBER_STR_0 = "[0-9]-[a-z|A-Z]-[0-6][0-9][0-9][0-9]";
    private final static String PTRN_MANUFACTURER_NUMBER_STR_1 = "[0-9][0-9]-[a-z|A-Z]-[0-6][0-9][0-9][0-9]";

    public final static Pattern PTRN_MANUFACTURER_NUMBER = Pattern.compile(PTRN_MANUFACTURER_NUMBER_STR_0 +
            "|" + PTRN_MANUFACTURER_NUMBER_STR_1);

    public final static Pattern PTRN_INTEGER = Pattern.compile("^\\s*([-+]?\\d+)\\s*$");
    public final static Pattern PTRN_DOUBLE = Pattern.compile("^\\s*([-+]?[0-9]{1,13}(\\.[0-9]*)?)\\s*$");

    public final static Pattern PTRN_DOUBLE_LIMIT = Pattern.compile("^\\s*((<|<=|=|>=|>)\\s*)?([-+]?[0-9]{1,13}(\\.[0-9]*)?)\\s*$");

    public static Integer parseInt(String s) throws NumberFormatException {
        if (s == null) {
            return null;
        }
        Matcher matcher = PTRN_INTEGER.matcher(s);
        if (matcher.matches()) {
            String g = matcher.group(1);
            return Integer.parseInt(g);
        } else {
            throw new NumberFormatException(s);
        }
    }

    public static Double parseDouble(String s) throws NumberFormatException {
        if (s == null) {
            return null;
        }
        Matcher matcher = PTRN_DOUBLE.matcher(s);
        if (matcher.matches()) {
            String g = matcher.group(1);
            return Double.parseDouble(g);
        } else {
            throw new NumberFormatException(s);
        }
     }

}
