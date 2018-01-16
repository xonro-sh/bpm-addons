package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.sdk.local.SDK;
import com.xonro.finance.util.BoDataUtil;
import com.xonro.finance.util.FlagUtil;

import java.util.List;

/**
 * 部门预算编制流程结束后,将有效的流程数据插入部门预算中间表中
 * @author hjj
 * @date 2018-1-15
 */
public class DeptBudgetAExeListener extends ExecuteListener {
    @Override
    public String getDescription() {
        return "流程结束后,将有效的流程数据插入部门预算中间表中";
    }
    @Override
    public void execute(ProcessExecutionContext cont) throws Exception {
        //获取流程Id
        String bindId=cont.getProcessInstance().getId();
        //获取表名
        String boName= (String) cont.getVariable("boName");
        //根据bindId判断当前流程是否正常结束
        String CONTROLSTATE = FlagUtil.getControlState(bindId);
        if(CONTROLSTATE.equals("end")){
            //更改流程状态为1(正常结束)
            FlagUtil.updateFlag(boName,bindId,1);
            //获取部门预算编制流程主表数据
            List<BO> deptList=SDK.getBOAPI().query("BO_XR_FM_DEPT_BUDGET").addQuery("BINDID=",bindId).list();
            //获取部门预算编制流程子表数据
            List<BO> deptSonList=SDK.getBOAPI().query("BO_XR_FM_DEPT_BUDGET_S").addQuery("BINDID=",bindId).list();
            //获取中间表数据
            List<BO> boList= BoDataUtil.getDeptMiddleData(deptList,deptSonList);
            //将数据插入中间表中
            SDK.getBOAPI().create("BO_XR_DEPT_MIDDLE",boList,bindId,"admin");

        }else{
            //更改流程状态为2(非正常结束)
            FlagUtil.updateFlag(boName,bindId,2);
        }
    }
}
