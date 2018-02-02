package com.xonro.ehr.attendance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;
import com.xonro.ehr.attendance.dao.AttendanceDao;
import com.xonro.ehr.util.FlagUtil;

import java.util.List;

/**
 * 考勤录入流程结束后事件
 * @author hjj
 * @date  2018-2-1
 */
public class AttendanceEntryProcessAfter extends ExecuteListener {
    @Override
    public String getDescription() {
        return "将导入数据，回写到考勤流水表中";
    }

    @Override
    public void execute(ProcessExecutionContext context) throws Exception {
        //获取当前流程Id
        String bindId=context.getProcessInstance().getId();
        //判断当前流程是否正常结束
        String CONTROLSTATE = FlagUtil.getControlState(bindId);
        if(CONTROLSTATE.equals("end")) {
            //根据bindId获取当前年份、月份、录入部门Id等数据
            String year= DBSql.getString("SELECT YEAR FROM BO_XR_HR_TC_DATAENTRY WHERE BINDID='"+bindId+"'");
            String month=DBSql.getString("SELECT MONTH FROM BO_XR_HR_TC_DATAENTRY WHERE BINDID='"+bindId+"'");
            String inputDeptId=DBSql.getString("SELECT INPUT_DEPTID FROM BO_XR_HR_TC_DATAENTRY WHERE BINDID='"+bindId+"'");
            //根据bindId获取Excel导入数据
            List<BO> inputData=SDK.getBOAPI().query("BO_XR_HR_TC_DATAENTRY_S").addQuery("BINDID=",bindId).list();
            //根据年份、月份、录入部门Id等信息判断，考勤流水表中是否存在数据
                List<BO> dataList= SDK.getBOAPI().query("BO_XR_HR_TC_RECORD").addQuery("YEAR=",year).
                    addQuery("MONTH=",month).addQuery("DEPARTMENTID=",inputDeptId).list();

            AttendanceDao dao=new AttendanceDao();
            if(dataList!=null && dataList.size()>0){
                //如果有数据,删掉原来的数据，重新插入
                String deleteSql="DELETE FROM BO_XR_HR_TC_RECORD WHERE YEAR='"+year+"' AND MONTH='"+month+"' AND DEPARTMENTID='"+inputDeptId+"'";
                DBSql.update(deleteSql);
            }
            //向流水表中插入新数据
            dao.entryAttendancData(inputData,inputDeptId,year,month,bindId);
        }
    }
}
