package com.zouhx.crawler.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class DateUtil {

	public static SimpleDateFormat sdfYYYY_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdfYYYYMMdd = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat sdfYYYYMM = new SimpleDateFormat("yyyyMM");
	public static SimpleDateFormat sdfYYYYIMMIdd = new SimpleDateFormat("yyyy/MM/dd");
	
	public static SimpleDateFormat sdfYYYY_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdfYYYYMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat sdfYYYYMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	 
	public DateUtil() {

	}

	public static String GetDate() {
		String sDate = sdfYYYY_MM_dd.format(new Date());
		return sDate;
	}

	public static String GetDateTime() {
		String sDate = sdfYYYY_MM_dd_HH_mm_ss.format(new Date());
		return sDate;
	}
    
    /**
     * <p>系统时间的取�?"yyyyMMddHHmmss"</p>
     *
     * @return "yyyyMMddHHmmss"格式的系统时间返�?
     */
    public static String getSysDateAll() {
        String dateString = sdfYYYYMMddHHmmss.format(new Date());
        return dateString;
    }
    
    /**
     * <p>系统时间的取�?"yyyyMMdd"</p>
     *
     * @return "yyyyMMddHHmmss"格式的系统时间返�?
     */
    public static String getSysDate() {       
        String dateString = sdfYYYYMMdd.format(new Date());
        return dateString;
    }
    
    public static String getSysDate(int days) {

    	Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);    //得到前一�?
		Date date = calendar.getTime();
        String dateString = sdfYYYYMMdd.format(date);
        return dateString;
    }

     
    public String GetTimeFormat(String strFormat)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
        String sDate = sdf.format(new Date());
        return sDate;
    }

    
    public String SetDateFormat(String myDate,String strFormat) throws ParseException
    {

        SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
        String sDate = sdf.format(sdf.parse(myDate));

        return sDate;
    }

    public String FormatDateTime(String strDateTime, String strFormat)
    {
        String sDateTime = strDateTime;
        try {
            Calendar Cal = parseDateTime(strDateTime);
            SimpleDateFormat sdf = null;
            sdf = new SimpleDateFormat(strFormat);
            sDateTime = sdf.format(Cal.getTime());
        } catch (Exception e) {

        }
        return sDateTime;
    }

    public static Calendar parseDateTime(String baseDate)
    {
        Calendar cal = null;
        cal = new GregorianCalendar();
        int yy = Integer.parseInt(baseDate.substring(0, 4));
        int mm = Integer.parseInt(baseDate.substring(5, 7)) - 1;
        int dd = Integer.parseInt(baseDate.substring(8, 10));
        int hh = 0;
        int mi = 0;
        int ss = 0;
        if(baseDate.length() > 12)
        {
            hh = Integer.parseInt(baseDate.substring(11, 13));
            mi = Integer.parseInt(baseDate.substring(14, 16));
            ss = Integer.parseInt(baseDate.substring(17, 19));
        }
        cal.set(yy, mm, dd, hh, mi, ss);
        return cal;
    }
    
    public static int getDay(String strDate)
    {
        Calendar cal = parseDateTime(strDate);
        return cal.get(Calendar.DATE);
    }

    public  static int getMonth(String strDate)
    {
        Calendar cal = parseDateTime(strDate);

        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getWeekDay(String strDate)
    {
        Calendar cal = parseDateTime(strDate);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

     
    public static int getYear(String strDate)
    {
        Calendar cal = parseDateTime(strDate);
        return cal.get(Calendar.YEAR) + 1900;
    }

    public static String DateAdd(String strDate, int iCount, int iType)
    {
        Calendar Cal = parseDateTime(strDate);
        int pType = 0;
        if(iType == 0)
        {
            pType = 1; //Calendar.YEAR;
        } else
        if(iType == 1)
        {
            pType = 2;
        } else
        if(iType == 2)
        {
            pType = 5;
        } else
        if(iType == 3)
        {
            pType = 10;
        } else
        if(iType == 4)
        {
            pType = 12;
        } else
        if(iType == 5)
        {
            pType = 13;
        }
        Cal.add(pType, iCount);
        SimpleDateFormat sdf = null;
        if(iType <= 2)
            sdf = sdfYYYY_MM_dd;
        else
            sdf = sdfYYYY_MM_dd_HH_mm_ss;
        String sDate = sdf.format(Cal.getTime());
        return sDate;
    }

    public static String DateAdd(String strOption, int iDays, String strStartDate)
    {
        if(!strOption.equals("d"));
        return strStartDate;
    }

    public int DateDiff(String strDateBegin, String strDateEnd, int iType)
    {
        Calendar calBegin = parseDateTime(strDateBegin);
        Calendar calEnd = parseDateTime(strDateEnd);
        long lBegin = calBegin.getTimeInMillis();
        long lEnd = calEnd.getTimeInMillis();
        int ss = (int)((lBegin - lEnd) / 1000L);
        int min = ss / 60;
        int hour = min / 60;
        int day = hour / 24;
        if(iType == 0)
            return hour;
        if(iType == 1)
            return min;
        if(iType == 2)
            return day;
        else
            return -1;
    }
    
    public static int DateDiff(Date d1, Date d2, int iType)
    {
    	Calendar beginCal = Calendar.getInstance();
		beginCal.setTime(d1);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(d2);
        long lBegin = beginCal.getTimeInMillis();
        long lEnd = endCal.getTimeInMillis();
        int ss = (int)((lBegin - lEnd) / 1000L);
        int min = ss / 60;
        int hour = min / 60;
        int day = hour / 24;
        if(iType == 0)
            return hour;
        if(iType == 1)
            return min;
        if(iType == 2)
            return day;
        else
            return -1;
    }
      
    /**
     * <p>日期类型转换为字符串类型，指定格式（java.util.Date�?</p>
     * @param currentTime
     * @return
     */
    public static String toString (String format,java.util.Date currentTime) {
        if( currentTime==null) return null ;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.getCalendar().setLenient(false);
        sdf.setLenient(false);
        return sdf.format(currentTime);
    }    

    /**
     * <p>日期类型转换为字符串类型，格式为yyyyMMdd（java.util.Date�?</p>
     * @param currentTime
     * @return
     */
	public static String toString(java.util.Date currentTime) {
		if (currentTime == null)
			return null;
		return sdfYYYYMMdd.format(currentTime);
	}
	
	public static String toStringyymm(java.util.Date currentTime) {
		if (currentTime == null)
			return null;
		return sdfYYYYMM.format(currentTime);
	}
    
    /**
     * <p>系统时间的取得（java.util.Date�?</p>
     *
     * @return java.util.Date格式的系统时间返�?
     */
    public static Date getNowDate() {
        return new java.util.Date();
    }   
    
    /***
     * 把一个日期型的变量转化为带时分秒的日期型
     * @param date
     * @return
     */
    public static Date getTime(Date date) {
        String dateString = DateUtil.toString("yyyy-MM-dd", date);      
        try {
 			date = sdfYYYY_MM_dd.parse(dateString+" 00:00:00");
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			return date;
 		}
        return date;
    }
    
    /***
     * 把Long类型日期(yyyyMMddHHmmss)转化为指定格式的字符型日�?(yyyy-MM-dd HH:mm:ss)
     * @param longDate
     * @return
     */
    public static String formatTime(Long longDate) {
		String strTime = longDate.toString();
		try {
			Date date = sdfYYYYMMddHHmmss.parse(strTime);
			String strDate2 = sdfYYYY_MM_dd_HH_mm_ss.format(date);
			
			return strDate2;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /***
     * 把Long类型日期(yyyyMMddHHmmss)转化为指定格式的字符型日�?(yyyy/MM/dd)
     * @param longDate
     * @return
     */
    public static String formatTime2(Long longDate) {
		String strTime = longDate.toString();
		try {
			Date date = sdfYYYYMMddHHmmss.parse(strTime);
			String strDate2 = sdfYYYYIMMIdd.format(date);
			
			return strDate2;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /***
     * 把Long类型日期(yy-MM-dd HH:mm:ss)转化为指定格式的字符型日�?(yyyy年MM年dd�? HH时mm分ss�?)
     * @param longDate
     * @return
     */
    public static String formatTime3(String strDate) {
		try {
			
			if(strDate==null||strDate.trim().equals("")){
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			}
			Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDate);	
			String strDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			
			return strDate2;
		} catch (Exception e) {
			System.out.println("strDate:["+strDate+"]");
			e.printStackTrace();
		}
		return null;
	}
    
   
    
    /***
     * 把一个字符型的变量转化为日期型变�?
     * @param strDate
     * @return
     */
    public static Date toDate(String strDate) {    	
        Date date = null;
		try {
			date = sdfYYYY_MM_dd.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
    	
    }
    
    /***
     * 把一个日期型变量转化为指定格式的字符型变�?(yyyy-MM-dd HH:mm)
     * @param date		日期型变�?
     * @return			指定格式的字符型变量
     */
    public static String toDateString(Date date, String format) {
    	SimpleDateFormat sdf = new SimpleDateFormat(format); 
    	return sdf.format(date);
    }
    
    /***
     * 把一个字符型的变量转化为指定形式的日期型变量
     * @param strDate
     * @return
     */
    public static Date toDate(String strDate, String format) {
    	
        SimpleDateFormat dateformatter = new SimpleDateFormat(format);
        Date date = null;
		try {
			date = dateformatter.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
    	
    }
    public static String getSysDay(String format, int field,int amount) {
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	Calendar cal = Calendar.getInstance();
    	cal.add(field, amount);
    	Date date = cal.getTime();
    	String time = sdf.format(date);
    	return time; 
    }
    
    /**
     * 计算时间�?,精确到豪�?
     * 
     * @param begin  
     * @param end  
     * @return
     */
    public static long countTime(String begin,String end, int type){
    	long rs = 0; 
    	long total_second = 0;
    	long total_millsecond = 0;
    	//StringBuffer sb = new StringBuffer();

    	try {
    		Date begin_date = sdfYYYYMMddHHmmssSSS.parse(begin);
    		Date end_date = sdfYYYYMMddHHmmssSSS.parse(end);
    		total_millsecond = (end_date.getTime() - begin_date.getTime());
    		total_second = (end_date.getTime() - begin_date.getTime())/1000;    		 
    		
    		switch(type) {
    			case 1: 
    				rs = total_millsecond;
    				break;
    			case 2:
    				rs = total_second;
    				break;
    		}
    	} catch (ParseException e) {
    		System.out.println("传入的时间格式不符合规定");
    	}    	 
    	 
    	return rs;
    }
    /**
     * 获取某天为一年中的第几个星期
     * @param strDate
     * @return
     */
    
    public static int getYearWeek(String strDate)
    {
        Calendar cal = parseDateTime(strDate);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    /**
     * 当前时间加天
     * @param days
     * @return
     */
    public static String getAddDay(int days) {

    	Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);    //得到前几�?
		Date date = calendar.getTime();
        String dateString = sdfYYYY_MM_dd_HH_mm_ss.format(date);
        return dateString;
    }
    
    public static void main(String args[]) {
    	
    	System.out.println(toStringyymm(new Date()));
    }

}
