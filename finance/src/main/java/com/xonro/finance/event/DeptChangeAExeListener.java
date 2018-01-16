package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;
import com.xonro.finance.util.BoDataUtil;
import com.xonro.finance.util.FlagUtil;

import java.util.List;

/**
 * 部门预算编制变更流程结束后，将中间表对应年份的部门数据删除
 * 并且插入变更后数据
 * @author hjj
 * @date 2018-1-15
 */
public class DeptChangeAExeListener extends ExecuteListener {
    @Override
    public String getDescription() {
        return "流程结束后,删除中间表对应年份该部门数据，并且将变更后数据重新插入中间表中";
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
            //获取年份
            String year= DBSql.getString("SELECT YEAR FROM BO_XR_FM_DEPT_BUDGET_CHANGE WHERE BINDID='"+bindId+"'");
            //获取预算部门ID
            String deptId=DBSql.getString("SELECT BUDGET_DEPTID FROM BO_XR_FM_DEPT_BUDGET_CHANGE WHERE BINDID='"+bindId+"'");
            //根据年份和预算部门ID，删除中间表对应数据
            String deleteSql="DELETE BO_XR_DEPT_MIDDLE WHERE YEAR='"+year+"' AND BUDGET_DEPTID='"+deptId+"'";
            DBSql.update(deleteSql);
            //获取部门预算编制变更主表数据
            List<BO> deptList= SDK.getBOAPI().query("BO_XR_FM_DEPT_BUDGET_CHANGE").addQuery("BINDID=",bindId).list();
            //获取部门预算编制变更子表数据
            List<BO> deptSonList=SDK.getBOAPI().query("BO_XR_FM_DEPT_BUDGET_CHANGE_S").addQuery("BINDID=",bindId).list();
            //获取需要插入的中间表数据
            List<BO> boList= BoDataUtil.getDeptMiddleData(deptList,deptSonList);
            //将数据插入中间表中
            SDK.getBOAPI().create("BO_XR_DEPT_MIDDLE",boList,bindId,"admin");
        }else{
            //更改流程状态为2(非正常结束)
            FlagUtil.updateFlag(boName,bindId,2);
        }
    }
}
