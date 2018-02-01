package com.xonro.ehr.attendance.util;

import com.actionsoft.bpms.bo.engine.BO;
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
    public static List<BO> getHolidayDate(String year,String month){
        //获取年度某个月份节假日数据
        List<BO> holidayDate = SDK.getBOAPI().query("BO_XR_HR_TC_HOLIDAY").addQuery("YEAR=",year).addQuery("MONTH=",month).list();
        return holidayDate;
    }

}
