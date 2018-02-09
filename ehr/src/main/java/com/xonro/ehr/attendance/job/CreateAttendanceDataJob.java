package com.xonro.ehr.attendance.job;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.org.model.CompanyModel;
import com.actionsoft.bpms.org.model.DepartmentModel;
import com.actionsoft.bpms.org.model.UserModel;
import com.actionsoft.bpms.schedule.IJob;
import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;
import com.xonro.ehr.util.DateUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时器生成下月考勤数据
 * @author haolh
 * @date 2018-2-5
 */
public class CreateAttendanceDataJob implements IJob {
    @Override
    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        try{
            //获取生成考勤的配置信息
            BO config = SDK.getBOAPI().query("BO_XR_HR_TC_CONFIG").list().get(0);
            //生成考勤日期
            String day = config.getString("DAY");
            //不参与考勤人员列表
            String UserIdList = config.getString("USERIDLIST");
            UserIdList = UserIdList.replace(",","','");
            //获取系统日期
            String sysDay = DateUtil.getSysDay();
            //如果符合生成下月考勤的日期
            if(day.equals(sysDay)){
                //获取考勤人员数据
                String userIds = DBSql.getString("SELECT GROUP_CONCAT(USERID) AS userIds FROM orguser " +
                        "where USERID not in ('"+UserIdList+"')","userIds");
                //将考勤人员转化为数组
                String[] users = userIds.split(",");
                //初始化考勤数据
                for(int i = 0;i <= users.length; i++){
                    //获取用户对象
                    UserModel userModel = SDK.getORGAPI().getUser(users[i]);
                    //获取部门对象
                    DepartmentModel departmentModel = SDK.getORGAPI().getDepartmentByUser(users[i]);
                    //获取公司对象
                    CompanyModel companyModel = SDK.getORGAPI().getCompanyByUser(users[i]);
                    //获取下个月份
                    String preMonth = DateUtil.getPreMonth();
                    //下月年份
                    int nextYear = Integer.parseInt(preMonth.substring(0,4));
                    //下个月份
                    int nextMonth = Integer.parseInt(preMonth.substring(4,6));
                    //获取该月天数
                    int dayNum = DateUtil.getDayNum(nextYear,nextMonth);
                    for(int j = 1;j <= dayNum; j++){
                        //考勤数据
                        BO attendanceBo = new BO();
                        attendanceBo.set("USERID",userModel.getUID());
                        attendanceBo.set("USERNAME",userModel.getUserName());
                        attendanceBo.set("DEPARTMENTNAME",departmentModel.getId());
                        attendanceBo.set("DEPARTMENTID",departmentModel.getName());
                        attendanceBo.set("POSITIONNAME",userModel.getPositionName());
                        attendanceBo.set("COMPANYNAME",companyModel.getName());
                        attendanceBo.set("COMPANYID",companyModel.getId());
                        attendanceBo.set("MONTH",nextMonth);
                        attendanceBo.set("YEAR",nextYear);
                        attendanceBo.set("RECORD_DATE",j);
                        //DateUtil.formatDateString(nextYear+"-"+nextMonth+"-"+j)
                        attendanceBo.set("CATEGORY",this.getCategory(userModel.getUID(),nextYear,nextMonth,j));
                        //将考勤数据插入考勤明细表
                        SDK.getBOAPI().createDataBO("BO_XR_HR_TC_DETAIL",attendanceBo,UserContext.fromUID("admin"));
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 获取员工某天的排班信息
     * @param uid
     * @param year
     * @param month
     * @param day
     * @return
     */
    public String getCategory(String uid,int year,int month,int day){
        return DBSql.getString("select DAY"+day+" category from BO_XR_HR_TC_SCHEDULE where " +
                "USERID='"+uid+"' and year='"+year+"' and month='"+month+"'","category");
    }

}
