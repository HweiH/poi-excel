package org.well.test.poiexcel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName:DateTimeUtil
 * @Description:类功能的详细描述XXX
 * @author well
 * @date:2020年4月10日
 *
 */
public class DateTimeUtil {
    
    public static final String DATE_PATTERN_EN = "yyyy-MM-dd";
    public static final String DATE_PATTERN_CH = "yyyy年MM月dd日";
    
    /**
     * 日期对象转换为指定格式的字符串
     *
     * @param date      日期对象
     * @param pattern   日期格式，取自于DateTimeUtil内部定义的日期格式常量，也可以自定义
     * @return
     */
    public static String dateFormat(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        if (StringUtils.isBlank(pattern)) {
            pattern = DateTimeUtil.DATE_PATTERN_EN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    /**
     * 从符合某种格式的日期字符串中解析出日期对象
     *
     * @param dateTimeString    符合某种格式的日期字符串
     * @param pattern           日期格式，取自于DateTimeUtil内部定义的日期格式常量，也可以自定义
     * @return
     */
    public static Date dateParse(String dateTimeString, String pattern) {
        if (StringUtils.isBlank(dateTimeString)) {
            return null;
        }
        if (StringUtils.isBlank(pattern)) {
            pattern = DateTimeUtil.DATE_PATTERN_EN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

}
