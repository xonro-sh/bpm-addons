package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.sdk.local.SDK;

import java.util.List;

/**
 * 部门预算编制变更，第一节任务完成后事件
 * @author hjj
 * @date 2018-1-19
 */
public class DeptChangeTaskAfterEvent extends ExecuteListener {
    @Override
    public String getDescription() {
        return "任务完成后事件，将数据写入到部门预算编制变更显示表中!";
    }
    @Override
    public void execute(ProcessExecutionContext context) throws Exception {
        //当前流程Id
        String bindId=context.getProcessInstance().getId();
        //判断显示表中是否存在该bindId数据
        List<BO> boList=SDK.getBOAPI().query("BO_XR_FM_DEPT_CHANGE_SHOW").addQuery("BINDID=",bindId).list();
        //如果存在删除对应的显示主子表数据
        if(boList!=null && boList.size()>0){
            SDK.getBOAPI().removeByBindId("BO_XR_FM_DEPT_CHANGE_SHOW",bindId);
            SDK.getBOAPI().removeByBindId("BO_XR_FM_DEPT_CHANGE_S_SHOW",bindId);
        }
        //根据bindId获取主表数据
        List<BO> masterList= SDK.getBOAPI().query("BO_XR_FM_DEPT_BUDGET_CHANGE").addQuery("BINDID=",bindId).list();
        //将主表数据写入到显示主表中
        SDK.getBOAPI().create("BO_XR_FM_DEPT_CHANGE_SHOW",masterList,context.getProcessInstance(),context.getUserContext());
        //更加bindId获取子表数据
        List<BO> sonList=SDK.getBOAPI().query("BO_XR_FM_DEPT_BUDGET_CHANGE_S").addQuery("BINDID=",bindId).list();
        //将子表数据写入到显示子表中
        SDK.getBOAPI().create("BO_XR_FM_DEPT_CHANGE_S_SHOW",sonList,context.getProcessInstance(),context.getUserContext());

    }
}
