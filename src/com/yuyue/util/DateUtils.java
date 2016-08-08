package com.yuyue.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.yuyue.exception.CheckedException;

public class DateUtils {
	private static final Format f1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static final Format f2 = new SimpleDateFormat("MM/dd/yyyy");
	public static String formatToLongString(Date date){
		if(date != null){
			return f1.format(date);
		}else{
			return "";
		}
	}
	public static String formatToShortString(Date date){
		if(date != null){
			return f2.format(date);
		}else{
			return "";
		}
	}
	public static String formatDate(Date date, String format){
		if(date != null){
			Format f = new SimpleDateFormat(format);
			return f.format(date);
		}else{
			return "";
		}
	}
	public static Date paraseDate(String dateStr) throws CheckedException {
		if(dateStr != null){
			try {
				return (Date) f2.parseObject(dateStr);
			} catch (ParseException e) {
				throw new CheckedException(e,"Cannot conver to date.", dateStr);
			}
		}else{
			return null;
		}
	}
	
	public static Date paraseDate(String dateStr, String format) throws CheckedException {
		//FIXME in case encoding is wrong with Chinese system
		if(dateStr != null){
			if(dateStr.contains("?")){
				dateStr = dateStr.replaceAll("\\?", "");
				format = format.replaceAll("年", "");
				format = format.replaceAll("月", "");
				format = format.replaceAll("日", "");
			}
			Format f = new SimpleDateFormat(format);
			try {
				return (Date) f.parseObject(dateStr);
			} catch (ParseException e) {
				throw new CheckedException(e,"Cannot conver to date.", dateStr, format);
			} finally{
				f = null;
			}
		}else{
			return null;
		}
	}
	
	public static Date getStartOfDay (Date date) {
		if(date != null){
			Calendar calender = Calendar.getInstance();
			calender.setTime(date);
			clearHours(calender);
			return calender.getTime();
		}else{
			return null;
		}
	}

	
	public static Date getStartOfToDay () {
		Calendar calender = Calendar.getInstance();
		clearHours(calender);
		return calender.getTime();
	}
	
	public static Date addDays (Date date, int i) {
		if(date != null){
			Calendar calender = Calendar.getInstance();
			calender.setTime(date);
			calender.add(Calendar.DAY_OF_WEEK, i);
			return calender.getTime();
		}else{
			return null;
		}
	}
	private static void clearHours(Calendar calender) {
		if(calender != null){
			calender.clear(Calendar.AM_PM);
			calender.clear(Calendar.HOUR_OF_DAY);
			calender.clear(Calendar.HOUR);
			calender.clear(Calendar.MINUTE);
			calender.clear(Calendar.SECOND);
			calender.clear(Calendar.MILLISECOND);
		}
	}
	
}
