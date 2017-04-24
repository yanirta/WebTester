package com.applitools.Utils;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.EnumSet;


public class Utils {

    public static <T extends Enum<T>> T parseEnum(Class<T> c, String string) {
        return Enum.valueOf(c, string.trim().toUpperCase());
    }

    public static String getEnumValues(Class type) {
        StringBuilder sb = new StringBuilder();
        for (Object val : EnumSet.allOf(type)) {
            String str = val.toString();
            str = (str.length() == 2) ? str.toUpperCase() : StringUtils.capitalize(str.toLowerCase());
            sb.append(str);
            sb.append('|');
        }
        return sb.substring(0, sb.length() - 1);
    }
}
