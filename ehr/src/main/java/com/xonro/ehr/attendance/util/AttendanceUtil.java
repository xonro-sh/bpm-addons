package com.xonro.ehr.attendance.util;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;

import java.util.List;

/**
 * 人事管理模块控制器类
 * @author haolh
 * @date 2018-1-31
 */
public class AttendanceUtil {

    //默认班次
    public static String WEEKENDDAY = "B02";
    public static String HOLIDAY = "B01";
    public static String DAY1 = "A01";
    public static String DAY2 = "A02";
    public static String DAY3 = "A03";

    /**
     * 获取某年某月节假日期
     * @param month
     * @return
     */
    public static List<BO> getHolidayDate1(String year,String month){
        //获取年度某个月份节假日数据
        List<BO> holidayDate = SDK.getBOAPI().query("BO_XR_HR_TC_HOLIDAY").addQuery("YEAR=",year).addQuery("MONTH=",month).list();
        return holidayDate;
    }
    /**
     * 查询某个日期是否为节假日
     * @param date
     * @return
     */
    public static boolean ifHoliday(String date){
        int count = DBSql.getInt("select count(*) count from BO_XR_HR_TC_HOLIDAY where " +
                "'"+date+"' between Date(STARTDATE) AND Date(ENDDATE)","count");
        if(count > 0){
            return true;
        }
        return false;
    }

    /**
     * 查询某个日期是否调整有排班
     * @param date
     * @return
     */
    public static boolean ifWeekendWork(String date){
        int count = DBSql.getInt("select count(*) count from BO_XR_HR_TC_HOLIDAY where " +
                "Date(EXTEND1)='"+date+"' or Date(EXTEND2)='"+date+"' or Date(EXTEND3)='"+date+"'","count");
        if(count > 0){
            return true;
        }
        return true;
    }
}
