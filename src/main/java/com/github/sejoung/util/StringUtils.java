package com.github.sejoung.util;

/**
 * @author kim se joung
 *
 */
public abstract class StringUtils {

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

}
