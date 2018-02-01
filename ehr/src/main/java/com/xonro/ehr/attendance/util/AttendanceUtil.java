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

    /**
     * 获取某月节假日期
     * @param month
     * @return
     */
    public static List<BO> getHolidayDate(String month){
        //获取某个月份节假日数据
        List<BO> holidayDate = SDK.getBOAPI().query("BO_XR_HR_TC_HOLIDAY").addQuery("MONTH=",month).list();
        return holidayDate;
    }

}
