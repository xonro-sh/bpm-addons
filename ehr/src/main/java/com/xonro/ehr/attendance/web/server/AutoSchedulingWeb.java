package com.xonro.ehr.attendance.web.server;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.org.model.CompanyModel;
import com.actionsoft.bpms.org.model.DepartmentModel;
import com.actionsoft.bpms.org.model.UserModel;
import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.bpms.util.DBSql;
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
    public String autoScheduling(UserContext me,String bindId,String year,String month,String type,String departmentId,
                                 String userIdList,String category){
        try{
            //获取部门人员
            List<UserModel> userList = new ArrayList<UserModel>();

            if("按部门生成".equals(type)){
                userList = SDK.getORGAPI().getUsersByDepartment(departmentId);
            }else if("按人员生成".equals(type)){
                String[] userIds = userIdList.split(",");
                for(int i = 0; i < userIds.length; i++){
                    String userId = userIds[i];
                    UserModel userModel = SDK.getORGAPI().getUser(userId);
                    userList.add(userModel);
                }
            }
            //排班信息List
            List<BO> schedulingList = new ArrayList<BO>();
            //遍历生成排班信息
            for (int i = 0; i < userList.size(); i++){
                //获取单个人员
                UserModel user = userList.get(i);
                //获取部门信息
                DepartmentModel department = SDK.getORGAPI().getDepartmentByUser(user.getUID());
                //获取公司对象
                CompanyModel companyModel = SDK.getORGAPI().getCompanyByUser(user.getUID());
                if(!validateScheduling(user.getUID(),year,month)){
                    return "0:用户"+user.getUserName()+"的排班信息已存在,生成失败。";
                }
                //排班信息
                BO schedulingBo = new BO();
                schedulingBo.set("USERID",user.getUID());
                schedulingBo.set("USERNAME",user.getUserName());
                schedulingBo.set("DEPARTMENTNAME",department.getName());
                schedulingBo.set("DEPARTMENTID",department.getId());
                schedulingBo.set("POSITIONNAME",user.getPositionName());
                schedulingBo.set("COMPANYNAME",companyModel.getName());
                schedulingBo.set("COMPANYID",companyModel.getId());
                schedulingBo.set("YEAR",year);
                schedulingBo.set("MONTH",month);
                //获取该月天数
                int dayNum = DateUtil.getDayNum(Integer.parseInt(year),Integer.parseInt(month));
                for(int j = 1;j <= dayNum; j++){
                    //获取日期
                    String date = "";
                    if(month.length() < 2){
                        date = year+"-0"+month+"-"+j;
                    }else{
                        date = year+"-"+month+"-"+j;
                    }
                    //当前日期是假期
                    if(AttendanceUtil.ifHoliday(date)){
                        schedulingBo.set("DAY"+j,AttendanceUtil.HOLIDAY);
                        continue;
                    }
                    //如果周末不排班
                    if(1 == 1){
                        //如果是周末 且 没有调班信息
                        if(DateUtil.isWeekend(date) && !AttendanceUtil.ifWeekendWork(date)){
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
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return "";
    }

    /**
     * 校验用户排班是否已存在
     * @param userId
     * @param year
     * @param month
     * @return
     */
    public boolean validateScheduling(String userId,String year,String month){
        int count = DBSql.getInt("select count(*) count from BO_XR_HR_TC_SCHEDULE where " +
                "USERID='"+userId+"' and YEAR='"+year+"' and MONTH='"+month+"'","count");
        if(count > 0){
            return false;
        }
        return true;
    }
}
