package org.well.test.poiexcel.factory.standard.machine;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.well.test.poiexcel.factory.Matter;

/**
 * @ClassName:ConfigUtil
 * @Description:config配置文件操作相关的工具类
 * @author well
 * @date:2020年4月10日
 *
 */
public class ConfigUtil {

    /**
     * 英文字母模式
     */
    public static final String PATTERN_ALPHA_NUMBER_STR = "^[a-zA-Z]{1,}[a-zA-Z0-9]*";
    
    /**
     * 坐标点字符串模式
     */
    public static final String PATTERN_POINT_STR = "^\\([1-9]\\d*-?([1-9]\\d*)?,[1-9]\\d*-?([1-9]\\d*)?\\)$";
    
    /**
     * 判断是否英文或者数字
     *
     * @param str
     * @return
     */
    public static boolean isAlphaNumber(String str) {
        return isRegexMatch(str, PATTERN_ALPHA_NUMBER_STR);
    }
    
    /**
     * 判断当前的字符串是否符合坐标点的定义，正则：^\([1-9]\d*-?([1-9]\d*)?,[1-9]\d*-?([1-9]\d*)?\)$
     *
     * @param str
     * @return
     */
    public static boolean isPointStr(String str) {
        return isRegexMatch(str, PATTERN_POINT_STR);
    }

    /**
     * 判断字符串是否正则匹配
     *
     * @param str
     * @param pattern
     * @return
     */
    public static boolean isRegexMatch(String str, String pattern) {
        if (StringUtils.isBlank(str) 
                || StringUtils.isBlank(pattern)) {
            return false;
        }
        return str.matches(pattern);
    }
    
    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public static String captureName(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        char[] cs = str.toCharArray();
        char fcs = cs[0];
        if (fcs >= 65 && fcs <= 90) {
            fcs += 32;
        }
        cs[0] = fcs;
        return String.valueOf(cs);
    }

    /**
     * 从classpath:/factory/standard/config/目录下或者/org/well/test/poiexcel/factory/standard/config/目录下获取对应配置文件*.config
     *
     * @param matter
     * @return
     */
    public static InputStream getConfigStream(Matter matter) {
        String id = StringUtils.trimToEmpty(matter.getId());
        String specified = String.format("/factory/standard/config/%s.config", id);
        InputStream is = ConfigUtil.class.getResourceAsStream(specified);
        if (is == null) {
            specified = String.format("/org/well/test/poiexcel/factory/standard/config/%s.config", id);
            is = ConfigUtil.class.getResourceAsStream(specified);
        }
        if (is == null) {
            throw new RuntimeException("没有找到当前配置文件：" + id + ".config");
        }
        return is;
    }
}
