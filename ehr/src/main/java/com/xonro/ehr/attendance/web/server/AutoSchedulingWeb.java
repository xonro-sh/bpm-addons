package com.xonro.ehr.attendance.web.server;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.org.model.DepartmentModel;
import com.actionsoft.bpms.org.model.UserModel;
import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.sdk.local.SDK;
import com.xonro.ehr.attendance.util.AttendanceUtil;
import com.xonro.ehr.util.DateUtil;

import java.util.ArrayList;
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
        try{
            //获取部门信息
            DepartmentModel department = SDK.getORGAPI().getDepartmentById(departmentId);
            //获取部门人员
            List<UserModel> userList = SDK.getORGAPI().getUsersByDepartment(departmentId);
            //排班信息List
            List<BO> schedulingList = new ArrayList<BO>();
            //遍历生成排班信息
            for (int i = 0; i < userList.size(); i++){
                //获取单个人员
                UserModel user = userList.get(i);
                //排班信息
                BO schedulingBo = new BO();
                schedulingBo.set("USERID",user.getUID());
                schedulingBo.set("USERNAME",user.getUserName());
                schedulingBo.set("DEPARTMENTNAME",department.getName());
                schedulingBo.set("DEPARTMENTID",departmentId);
                schedulingBo.set("POSITIONNAME","");
                schedulingBo.set("COMPANYNAME","");
                schedulingBo.set("COMPANYID","");
                schedulingBo.set("YEAR",year);
                schedulingBo.set("MONTH",month);
                //获取该月天数
                int dayNum = DateUtil.getDayNum(Integer.parseInt(year),Integer.parseInt(year));
                for(int j = 1;j <= dayNum; j++){
                    //如果周末不排班
                    if(1 == 1){
                        //获取日期
                        String date = year+"-"+month+"-"+j;
                        if(DateUtil.isWeekend(date)){
                            schedulingBo.set("DAY"+j,AttendanceUtil.WEEKENDDAY);
                        }else {
                            schedulingBo.set("DAY"+j,category);
                        }
                    }
                    //周末算为工作日
                    else{
                        schedulingBo.set("DAY"+j,category);
                    }
                }
                schedulingList.add(schedulingBo);
            }
            //将排班信息删除
            SDK.getBOAPI().removeByBindId("BO_XR_HR_TC_SCHEDULE",bindId);
            //将排班信息插入子表
            SDK.getBOAPI().create("BO_XR_HR_TC_SCHEDULE",schedulingList,bindId,me.getUID());
            //获取当月节假日期
            List<BO> holidayDate = AttendanceUtil.getHolidayDate(year,month);
            for (int i = 0; i < holidayDate.size(); i++){
                BO holiday = holidayDate.get(i);
                String date = holiday.getString("HOLIDAYDATE");
                SDK.getBOAPI().updateByBindId("BO_XR_HR_TC_SCHEDULE",bindId,"DAY"+date,AttendanceUtil.HOLIDAY);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return "";
    }
}
