package com.github.sejoung.util;

/**
 * @author kim se joung
 *
 */
public abstract class StringUtils {
    /**
     * 비어 있는 값을 체크
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

}
