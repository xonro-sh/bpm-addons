package com.xonro.ehr.attendance.web.server;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.sdk.local.SDK;
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
     * @param bindId
     * @param year
     * @param month
     * @param departmentId
     * @param category
     * @return
     */
    public String autoScheduling(UserContext me, String bindId, String year, String month, String departmentId, String category){
        //获取部门信息
        List<BO> department = SDK.getBOAPI().query("ORGUSER").addQuery("DEPARTMENTID=",departmentId).list();
        //获取部门人员
        List<BO> userList = SDK.getBOAPI().query("ORGUSER").addQuery("DEPARTMENTID=",departmentId).list();
        //遍历生成排班信息
        for (int i = 0; i < userList.size(); i++){
            //获取单个人员
            BO user = userList.get(i);
            //排班信息
            BO schedulingBo = new BO();
            schedulingBo.set("USERID",user.getString("USERID"));
            schedulingBo.set("USERNAME",user.getString("USERNAME"));
            schedulingBo.set("DEPARTMENTNAME",user.getString(""));
            schedulingBo.set("DEPARTMENTID",departmentId);
            schedulingBo.set("POSITIONNAME",user.getString(""));
            schedulingBo.set("COMPANYNAME",user.getString(""));
            schedulingBo.set("COMPANYID",user.getString(""));

        }
        //获取当月节假日期
        List<BO> holidayDate = AttendanceUtil.getHolidayDate(year,month);

        return "";
    }
}
