package com.xonro.ehr.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期公共类
 * @author haolh
 * @date 2018-2-1
 */
public class DateUtil {

    /**
     * 获取某年某月的天数
     * @param date
     * @return
     */
    public static Date formatDateString(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date bdate = format.parse(date);
        return bdate;
    }
    /**
     * 获取某年某月的天数
     * @param year
     * @param month
     * @return
     */
    public static int getDayNum(int year,int month){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        //year年
        calendar.set(Calendar.YEAR,year);
        //Calendar对象默认一月为0,month月
        calendar.set(Calendar.MONTH,month-1);
        //本月份的天数
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取系统年份
     * @return
     */
    public static String getSysYear() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return year;
    }
    /**
     * 获取系统月份
     * @return
     */
    public static String getSysMonth() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.MONTH));
        return year;
    }
    /**
     * 获取系统日期
     * @return
     */
    public static String getSysDay() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.DATE));
        return year;
    }
    /**
     * 获取下一个月.
     * @return
     */
    public static String getPreMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMM");
        String preMonth = dft.format(calendar.getTime());
        return preMonth;
    }


    /**
     * 判断是日期是否为周六周日
     * @param date
     * @return
     * @throws ParseException
     */
    public static boolean isWeekend(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date bdate = format.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bdate);
        if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            return true;
        }
        return false;
    }

    /**
     * 获取两个日期之间的所有日期
     * @param minDate
     * @param maxDate
     * @return
     * @throws ParseException
     */
    public static List<String> getDateBetweenDateStr(String minDate, String maxDate) throws ParseException{
        List<String> listDate = new ArrayList<String>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = df.parse(minDate);
        startCalendar.setTime(startDate);
        Date endDate = df.parse(maxDate);
        endCalendar.setTime(endDate);
        while(true){
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            if(startCalendar.getTimeInMillis() < endCalendar.getTimeInMillis()){
                listDate.add(df.format(startCalendar.getTime()));
            }else{
                break;
            }
        }
        return listDate;
    }
}
