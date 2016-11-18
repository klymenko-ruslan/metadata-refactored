package com.turbointernational.metadata.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dmytro.trunykov@zorallabs.com on 11.05.16.
 */
public class RegExpUtils {

    public final static String MANUFACTURER_NUMBER_STR_REGEX_0 = "[0-9]-[a-z|A-Z]-[0-6][0-9][0-9][0-9]";
    public final static String MANUFACTURER_NUMBER_STR_REGEX_1 = "[0-9]-[a-z|A-Z]-[0-6][0-9][0-9][0-9][a-z|A-Z|0-9]";
    public final static String MANUFACTURER_NUMBER_STR_REGEX_2 = "[0-9][0-9]-[a-z|A-Z]-[0-6][0-9][0-9][0-9]";
    public final static String MANUFACTURER_NUMBER_STR_REGEX_3 = "[0-9][0-9]-[a-z|A-Z]-[0-6][0-9][0-9][0-9][a-z|A-Z|0-9]";

    public final static Pattern PTRN_MANUFACTURER_NUMBER = Pattern.compile(MANUFACTURER_NUMBER_STR_REGEX_0 +
            "|" + MANUFACTURER_NUMBER_STR_REGEX_1 + "|" + MANUFACTURER_NUMBER_STR_REGEX_2 +
            "|" + MANUFACTURER_NUMBER_STR_REGEX_3);

    public final static Pattern PTRN_INTEGER = Pattern.compile("^\\s*([-+]?\\d+)\\s*$");
    public final static Pattern PTRN_DOUBLE = Pattern.compile("^\\s*([-+]?[0-9]{0,13}(\\.[0-9]*)?)\\s*$");

    public final static Pattern PTRN_DOUBLE_LIMIT = Pattern.compile("^\\s*((<|<=|=|>=|>)\\s*)?([-+]?[0-9]{0,13}(\\.[0-9]*)?)\\s*$");

     /**
     * This method unlike Integer.parse() parses only integers in plain format '+-?*.
      *
     * @param s
     * @return
     * @throws NumberFormatException
     */
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

    /**
     * This method unlike Double.parse() parses only doubles in plain format '+-?[?][.[?*]].
     *
     * @param s
     * @return
     * @throws NumberFormatException
     */
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
