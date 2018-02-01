package com.xonro.ehr.attendance.web.server;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.server.UserContext;
import com.xonro.ehr.attendance.util.AttendanceUtil;

import java.util.List;

/**
 * 自动生成排班信息
 * @author haolh
 * @date 2018-1-31
 */
public class AutoSchedulingWeb {
    /**
     * 自动生成排班信息
     * @param me
     * @param month
     * @param departmentId
     * @param category
     * @return
     */
    public String autoScheduling(UserContext me, String month, String departmentId, String category){

        //获取当月节假日期
        List<BO> holidayDate = AttendanceUtil.getHolidayDate(month);

        return "";
    }
}
