package com.zlst.data.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil3 {

    private static final int TIME_LENGTH = 3;

        /**
     * get first date of given month and year
     * @param year
     * @param month
     * @return
     */
        public static String getFirstDayOfMonth(int year,int month){
          String monthStr = month <10?"0"+ month :String.valueOf(month);
            return year +"-"+monthStr+"-"+"01";
        }
       /**
      * get the last date of given month and year
      * @param year
      * @param month
      * @return
      */
        public static String getLastDayOfMonth(int year,int month){
            Calendar calendar =Calendar.getInstance();
            calendar.set(Calendar.YEAR , year);
            calendar.set(Calendar.MONTH , month -1);
            calendar.set(Calendar.DATE ,1);
            calendar.add(Calendar.MONTH,1);
            calendar.add(Calendar.DAY_OF_YEAR ,-1);
            return calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+
            calendar.get(Calendar.DAY_OF_MONTH);
        }

        /**
      * get Calendar of given year
      * @param year
      * @return
      */
        public  static Calendar getCalendarFormYear(int year){
            Calendar cal =Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            cal.set(Calendar.YEAR, year);return cal;
        }

        /**
      * get start date of given week no of a year
      * @param year
      * @param weekNo
      * @return
      */
        public  static  String getStartDayOfWeekNo(int year,int weekNo){
            Calendar cal = getCalendarFormYear(year);
            cal.set(Calendar.WEEK_OF_YEAR, weekNo);
            return cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+
            cal.get(Calendar.DAY_OF_MONTH);

        }

       /**
      * get the end day of given week no of a year.
      * @param year
      * @param weekNo
      * @return
      */
      public static String getEndDayOfWeekNo(int year,int weekNo) {
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH);
    }

    public static String stringToDate(long time){
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
        return sd.format(date);
    }

    public static String timeToSecond(String time)
    {
        String result = "0";
        String[] splited = time.split(":");
        if(splited.length == TIME_LENGTH)
        {
            int hour = Integer.parseInt( splited[0]) * 3600;
            int minite = Integer.parseInt(splited[1]) * 60;
            int second = Integer.parseInt(splited[2]);
            result = String.valueOf(hour + minite + second);
        }
        return  result;
    }
}
