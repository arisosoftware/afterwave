package com.afterwave.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.util.StringUtils;
 
public class DateUtil {

	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

	public static String formatDate(Date date) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(date);
	}

	public static String formatDateTime(Date date) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(date);
	}

	public static String formatDateTime(Date date, String style) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(style);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(date);
	}

	public static Date getDateAfter(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	public static Date getDateBefore(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -day);
		return calendar.getTime();
	}


	public static Date getHourAfter(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);
		return calendar.getTime();
	}


	public static Date getHourBefore(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
		return calendar.getTime();
	}

	public static Date getMinuteAfter(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	public static Date getMinuteBefore(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, -minute);
		return calendar.getTime();
	}

	/**
	 * 判断传入的时间是否在当前时间之后，返回boolean值 true: 过期 false: 还没过期
	 *
	 * @param date
	 * @return
	 */
	public static boolean isExpire(Date date) {
		if (date.before(new Date()))
			return true;
		return false;
	}

	/**
	 * 字符串转时间
	 *
	 * @param dateString
	 * @param style
	 * @return
	 */
	public static Date string2Date(String dateString, String style) {
		if (StringUtils.isEmpty(dateString))
			return null;
		Date date = new Date();
		SimpleDateFormat strToDate = new SimpleDateFormat(style);
		try {
			date = strToDate.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	private static final long MINUTE = 60 * 1000;
	private static final long HOUR = 60 * MINUTE;
	private static final long DAY = 24 * HOUR;

	private static final long MONTH = 31 * DAY;
	private static final long WEEK = 7 * DAY;
	private static final long YEAR = 12 * MONTH;

	public static String formatDateCn(Date date) {
		if (date == null)
			return "";

		long offset = System.currentTimeMillis() - date.getTime();
		if (offset > YEAR) {
			return (offset / YEAR) + "年前";
		} else if (offset > MONTH) {
			return (offset / MONTH) + "个月前";
		} else if (offset > WEEK) {
			return (offset / WEEK) + "周前";
		} else if (offset > DAY) {
			return (offset / DAY) + "天前";
		} else if (offset > HOUR) {
			return (offset / HOUR) + "小时前";
		} else if (offset > MINUTE) {
			return (offset / MINUTE) + "分钟前";
		} else {
			return "刚刚";
		}
	}

}