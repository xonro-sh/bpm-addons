package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;
import com.xonro.finance.util.BoDataUtil;

import java.util.List;

/**
 * 部门预算编制变更流程，选择年份以及预算部门保存后，代入之前数据
 * @author  hjj
 * @date  2018-1-16
 */
public class DeptChangeSaveExeListener extends ExecuteListener{
    @Override
    public String getDescription() {
        return "表单保存后,根据预算部门以及年份，代入之前预算数据";
    }
    @Override
    public void execute(ProcessExecutionContext cont) throws Exception {
        //获取流程Id
        String bindId=cont.getProcessInstance().getId();
        //获取表名
        String boName= (String) cont.getVariable("boName");
        //获取预算部门Id
        String deptId= DBSql.getString("SELECT BUDGET_DEPTID FROM "+boName+" WHERE BINDID='"+bindId+"'");
        //获取年份
        String year=DBSql.getString("SELECT YEAR FROM "+boName+" WHERE BINDID='"+bindId+"'");
        if(null!=deptId && !"".equals(deptId) && null!=year && !"".equals(year)){
            //获取中间表中对应主表数据
            BO masterData=BoDataUtil.getMasterData(deptId,year);
            //向当前流程中插入主表数据
            SDK.getBOAPI().create(boName,masterData,bindId,"admin");
            //获取中间表中对应子表数据
            List<BO> sonList=BoDataUtil.getSonData(deptId,year);
            //想当前流程中插入子表数据
            SDK.getBOAPI().create("BO_XR_FM_DEPT_BUDGET_CHANGE_S",sonList,bindId,"admin");

        }

    }
}
