package com.xonro.seal.event;

import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.bpms.util.DBSql;
import com.xonro.seal.util.FlagUtil;
import com.xonro.seal.util.SealUtil;


/**
 * 印章作废流程结束后，将对应印章的状态改为3(作废状态)
 *
 * @author hjj
 * @date 2018/1/2
 *
 */
public class SealCancelAExeListener extends ExecuteListener {
    @Override
    public String getDescription() {
        return "印章作废流程结束后，将对应印章的状态改为3(作废状态)";
    }

    @Override
    public void execute(ProcessExecutionContext cont) throws Exception {
        // 流程ID
        String bindId = cont.getProcessInstance().getId();
        //获取流程表名
        String boName= (String) cont.getVariable("boName");
        // 根据bindId判断当前流程是否正常结束
        String CONTROLSTATE = FlagUtil.getControlState(bindId);

        if (CONTROLSTATE.equals("end")) {
            // 获取印章编号
            String sealNo = DBSql
                    .getString("select  SEAL_NO from "+boName+" where BINDID='"
                            + bindId + "'");
            // 根据印章编号修改印章状态为3(作废状态)
            SealUtil.updateSealFlag(sealNo, 3);
            //更改流程状态为1(正常结束)
            FlagUtil.updateSealFlag(boName,bindId,1);

        }else{
            //更改流程状态为2(非正常结束)
            FlagUtil.updateSealFlag(boName,bindId,2);
        }

    }

}
