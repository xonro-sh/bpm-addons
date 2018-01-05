package com.xonro.seal.event;

import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.xonro.seal.util.FlagUtil;

/**
 * 流程结束通用事件,判断流程是否正常结束,并且修改对应流程状态字段
 * @author  hjj
 * @date  2018/1/5
 */
public class ProcessCurrencyAExeListener extends ExecuteListener {
    @Override
    public String getDescription() {
        return "流程结束通用事件,判断流程是否正常结束,并且修改对应流程状态字段";
    }

    @Override
    public void execute(ProcessExecutionContext cont) throws Exception {
        //获取流程Id
        String bindId=cont.getProcessInstance().getId();
        //获取流程表名
        String boName= (String) cont.getVariable("boName");
        // 根据bindId判断当前流程是否正常结束
        String CONTROLSTATE = FlagUtil.getControlState(bindId);
        if(CONTROLSTATE.equals("end")){
            //更改流程状态为1(正常结束)
            FlagUtil.updateSealFlag(boName,bindId,1);
        }else{
            //更改流程状态为2(非正常结束)
            FlagUtil.updateSealFlag(boName,bindId,2);
        }

    }
}
