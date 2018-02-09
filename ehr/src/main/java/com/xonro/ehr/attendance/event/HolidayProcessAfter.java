package com.xonro.ehr.attendance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.bpms.org.cache.DepartmentCache;
import com.actionsoft.bpms.org.cache.UserCache;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;
import com.xonro.ehr.util.FlagUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 年假导入流程结束后事件
 * @author  hjj
 * @date  2018-2-9
 */
public class HolidayProcessAfter extends ExecuteListener {
    @Override
    public String getDescription() {
        return "将导入数据，回写年假信息表中";
    }

    @Override
    public void execute(ProcessExecutionContext context) throws Exception {
        //获取当前流程Id
        String bindId=context.getProcessInstance().getId();
        //判断当前流程是否正常结束
        String CONTROLSTATE = FlagUtil.getControlState(bindId);
        if(CONTROLSTATE.equals("end")) {
            //获取年份
            String year= DBSql.getString("SELECT YEAR FROM BO_XR_HR_HOLIDAY_IMP WHERE BINDID='"+bindId+"'");
            //获取导入子表数据
            List<BO>  dataList= SDK.getBOAPI().query("BO_XR_HR_HOLIDAY_IMP_S").addQuery("BINDID=",bindId).list();
            List<BO>  boList=new ArrayList<>();
            if(dataList!=null && dataList.size()>0){
                for(int i=0;i<dataList.size();i++){
                    boList.add(getBoData(dataList.get(i),year));
                }
                //插入数据
                SDK.getBOAPI().create("BO_XR_HR_HOLIDAY",boList,bindId,context.getUserContext().getUID());
            }
        }
    }

    public BO getBoData(BO data,String year){
        BO bo=new BO();
        if(data!=null && !data.equals("")){
            //获取人员账号
            String userId=data.getString("USERID");
            //根据账号获取对应的公司Id,部门Id
            String departmentId= UserCache.getModel(userId).getDepartmentId();
            String companyId= DepartmentCache.getModel(departmentId).getCompanyId();

            bo.set("COMPANYNAME",data.get("COMPANYNAME"));
            bo.set("COMPANYID",companyId);
            bo.set("YEAR",year);
            bo.set("USERNAME",data.get("USERNAME"));
            bo.set("USERID",userId);
            bo.set("DEPARTMENTNAME",data.get("DEPARTMENTNAME"));
            bo.set("DEPARTMENTID",departmentId);
            bo.set("DAYS",data.get("DAYS"));
            bo.set("START_DATE",data.get("START_DATE"));
            bo.set("EXPIRY_DATE",data.get("EXPIRY_DATE"));

            return bo;
        }
      return null;
    }
}
