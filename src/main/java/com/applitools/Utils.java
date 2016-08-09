package com.applitools;

import com.sun.xml.internal.ws.util.StringUtils;
import org.apache.commons.cli.*;

import java.util.EnumSet;


public class Utils {

    private static CommandLineParser parser = new DefaultParser();

    public static CommandLine parse(Options options, String[] args) throws ParseException {
        return parser.parse(options, args, true);
    }

    public static <T extends Enum<T>> T parseEnum(Class<T> c, String string) throws ParseException {
        if (c != null && string != null) {
            try {
                return Enum.valueOf(c, StringUtils.capitalize(string.trim()));
            } catch (IllegalArgumentException ex) {
            }
        }
        throw new ParseException(String.format("Unable to parse value %s for enum %s", string, c.getName()));
    }

    public static String getEnumValues(Class type) {
        StringBuilder sb = new StringBuilder();
        for (Object val : EnumSet.allOf(type)) {
            sb.append(StringUtils.capitalize(val.toString().toLowerCase()));
            sb.append('|');
        }
        return sb.substring(0, sb.length() - 1);
    }
}
