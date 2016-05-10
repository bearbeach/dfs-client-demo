package com.baofoo.dfs.client.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import java.util.Date;

/**
 * 日期操作帮助类 (基于 joda DateTime)
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 */
public class DateUtil {

    /** 时间格式：HHmmss */
    public static final String timePattern = "HHmmss";

    /** 时间格式：yyyy/MM/ddHH:mm:ss */
    public static final String timesPattern = "yyyy/MM/ddHH:mm:ss";

    /** 日期格式：yyyyMMdd */
    public static final String datePattern = "yyyyMMdd";

    /** 日期格式：yyMMdd */
    public static final String shortDatePattern = "yyMMdd";

    /** 日期时间格式：yyyyMMddHHmmss */
    public static final String fullPattern = "yyyyMMddHHmmss";

    /** 日期时间格式：yyyyMMddHHmmssSS */
    public static final String fullPatterns = "yyyyMMddHHmmssSS";

    /** 日期时间格式：yyMMddHHmmss */
    public static final String partPattern = "yyMMddHHmmss";

    /** 日期时间格式：yyyy.MM.dd HH:mm:ss */
    public static final String settlePattern = "yyyy-MM-dd HH:mm:ss";

    /** 时间式：HHmm */
    public static final String hour_of_minute = "HHmm";

    /** 时间式：yyyyMM */
    public static final String yyyyMM = "yyyyMM";

    /** 日期时间格式：yyyyMMdd HH:mm:ss */
    public static final String datefullPattern = "yyyyMMdd HH:mm:ss";

    /** 日期时间格式：yyyyMMddHHmm */
    public static final String year_of_minute = "yyyyMMddHHmm";

    /** 日期时间格式：yyyy-MM-dd HH:mm */
    public static final String ymdhm = "yyyy-MM-dd HH:mm";

    /** 日期时间格式：yyyy-MM-dd */
    public static final String yyMMdd = "yyyy-MM-dd";

    /**
     * 获取当前时间 格式：yyyyMMddHHmmss
     *
     * @return String       当前时间
     */
    public static String getCurrent() {
        return getCurrent(fullPatterns);
    }

    /**
     * 根据格式规范返回当前时间 指定格式
     *
     * @param pattern       格式规范
     * @return String       当前时间
     */
    public static String getCurrent(String pattern) {
        return DateTime.now().toString(pattern);
    }

    /**
     * 格式转换，yyyyMMddHHmiss to Date
     *
     * @param date          格式：yyyyMMddHHmiss
     * @return Date         Date
     */
    public static Date parse(String date) {
        if (!StringUtils.isBlank(date) && date.length() > 14) {
            return parse(date, fullPatterns);
        }
        return parse(date, fullPattern);
    }

    /**
     * 将String 【yyyyMMddHHmiss】类型转换成Date类型
     *
     * @param date          格式：yyyyMMdd
     * @param pattern       格式：yyyyMMdd
     * @return Date         Date
     */
    public static Date parse(String date, String pattern) {
        DateTime dateTime = parseTime(date, pattern);
        if (dateTime == null) return null;
        return dateTime.toDate();
    }

    /**
     * String To Date
     *
     * @param date          日期，格式：yyyyMMddHHmiss
     * @param pattern       格式
     * @return DateTime
     */
    public static DateTime parseTime(String date, String pattern) {
        if (StringUtils.isBlank(date)) return null;
        if (fullPattern.equals(pattern)) { // 这段逻辑整合自 gateway-bank 中的 DateUtil
            date = StringUtils.rightPad(date, 14, '0');
        }
        return DateTimeFormat.forPattern(pattern).parseDateTime(date);
    }

    /**
     * Date To String 格式：yyyyMMddHHmiss
     *
     * @param date          时间
     * @return String       时间格式：yyyyMMddHHmiss
     */
    public static String format(Date date) {
        return format(date, fullPattern);
    }

    /**
     * Date To String
     *
     * @param date          时间
     * @param pattern       指定格式
     * @return String       时间
     */
    public static String format(Date date, String pattern) {
        if (date == null) return null;
        return new DateTime(date).toString(pattern);
    }

    /**
     * 日期计算
     *
     * @param date          需要计算的日期
     * @param day           需要往前(负数)或往后(正数)的天数
     * @param inPattern     输入时间的格式
     * @param outPattern    输出时间的格式
     * @return 按照输出时间格式进行格式化后的时间
     */
    public static String computeDate(String date, int day, String inPattern, String outPattern) {
        try {
            DateTime dateTime = parseTime(date, inPattern);
            dateTime = dateTime.minusDays(0 - day);
            return format(dateTime.toDate(), outPattern);
            //输入时间转换错误时返回空值 避免因时间转换错误而导致原流程无法继续执行
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期计算
     *
     * @param date          需要计算的日期
     * @param day           需要往前(负数)或往后(正数)的天数
     * @return              按照输出时间格式进行格式化后的时间
     */
    public static Date computeDate(Date date, int day) {
        try {
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.minusDays(0 - day);
            return dateTime.toDate();
            //输入时间转换错误时返回空值 避免因时间转换错误而导致原流程无法继续执行
        } catch (Exception e) {
            return null;
        }
    }

}
