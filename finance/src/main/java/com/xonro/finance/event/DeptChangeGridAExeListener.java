package com.xonro.finance.event;

import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.sdk.local.SDK;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 部门预算编制流程，表单构造前事件，限制小于当前月份的预算不能改动
 * @author hjj
 * @date 2018-1-17
 */
public class DeptChangeGridAExeListener extends ExecuteListener {
    @Override
    public String getDescription() {
        return "表单构造前事件，限制小于当前月份的预算不能改动!";
    }
    @Override
    public void execute(ProcessExecutionContext context) throws Exception {
        //获取应用配置参数，判断是否启用方法
        String flag=SDK.getAppAPI().getProperty("com.xonro.apps.finance","deptChange_readonly");
        if(flag.equals("true")){
            Calendar now = Calendar.getInstance();
            //获取当前月份
            int nowMonth=now.get(Calendar.MONTH);
            List<String> monthList=getMonthList();
            for(int i=0;i<nowMonth-1;i++){
                context.addFormReadOnlyPolicy("BO_XR_FM_DEPT_BUDGET_CHANGE_S",monthList.get(i));
            }
        }

    }
    public static List<String> getMonthList(){
        List<String> monthList=new ArrayList<String>();
        monthList.add("JANUARY");
        monthList.add("FEBRUARY");
        monthList.add("MARCH");
        monthList.add("APRIL");
        monthList.add("MAY");
        monthList.add("JUNE");
        monthList.add("JULY");
        monthList.add("AUGUST");
        monthList.add("SEPTEMBER");
        monthList.add("OCTOBER");
        monthList.add("NOVEMBER");
        monthList.add("DECEMBER");
        return  monthList;
    }
}
