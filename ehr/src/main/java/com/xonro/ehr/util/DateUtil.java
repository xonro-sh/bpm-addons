package com.xonro.ehr.util;

import java.util.Calendar;

/**
 * 日期公共类
 * @author haolh
 * @date 2018-2-1
 */
public class DateUtil {

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
}
