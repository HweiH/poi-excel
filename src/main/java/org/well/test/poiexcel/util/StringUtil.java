package org.well.test.poiexcel.util;

import org.apache.commons.lang3.RegExUtils;

/**
 * @ClassName:StringUtil
 * @Description:字符串操作相关的工具类
 * @author well
 * @date:2020年4月10日
 *
 */
public class StringUtil {
    
    /**
     * 空白字符模式，如：空格、制表符、换行符、回车
     */
    public static final String PATTERN_BLANK_CHAR = "\\s*|\\t|\\r|\\n";
    
    /**
     * 去除所有的空白字符
     *
     * @param str
     * @return
     */
    public static String trimBlankChar(String str) {
        return RegExUtils.replaceAll(str, PATTERN_BLANK_CHAR, "");
    }
    
    /**
     * 去除所有的空白字符，null 转 ""
     *
     * @param str
     * @return
     */
    public static String trimBlankCharToEmpty(String str) {
        String temp = trimBlankChar(str);
        return (temp == null ? "" : temp);
    }

    /**
     * 去除所有的空白字符，"" 转 null
     *
     * @param str
     * @return
     */
    public static String trimBlankCharToNull(String str) {
        String temp = trimBlankChar(str);
        return ("".equals(temp) ? null : temp);
    }
}
