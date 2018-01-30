package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;
import com.xonro.finance.dao.BudgetDataDao;
import com.xonro.finance.dao.PersonalDataDao;
import com.xonro.finance.util.FlagUtil;

import java.util.List;

/**
 * 普通费用报销流程结束后事件
 * @author hjj
 * @date   2018-1-24
 */
public class OrdinaryRBmentProcessAfter extends ExecuteListener {
    @Override
    public String getDescription() {
        return "流程结束后,将普通费用报销金额数据回写到汇总中间表中";
    }
    @Override
    public void execute(ProcessExecutionContext context) throws Exception {
        //获取流程Id
        String bindId=context.getProcessInstance().getId();
        //获取表名
        String boName= (String) context.getVariable("boName");
        //获取部门Id
        String deptId=context.getUserContext().getDepartmentModel().getId();
        //获取年份
        String year= DBSql.getString("SELECT YEAR FROM "+boName+" WHERE BINDID='"+bindId+"'");
        //获取月份
        String month=DBSql.getString("SELECT MONTH FROM "+boName+" WHERE BINDID='"+bindId+"'");
        //根据bindId判断当前流程是否正常结束
        String CONTROLSTATE = FlagUtil.getControlState(bindId);
        if(CONTROLSTATE.equals("end")) {
            //更改流程状态为1(正常结束)
            FlagUtil.updateFlag(boName, bindId, 1);
            //根据bindId获取，普通费用报销子表数据
            List<BO> dataList= SDK.getBOAPI().query("BO_XR_FM_ORDINARY_EXPENSE_S").addQuery("BINDID=",bindId).list();
            if(dataList!=null && dataList.size()>0){
                BudgetDataDao dao=new BudgetDataDao();
                for(int i=0;i<dataList.size();i++){
                    //根据boId判断，是否有报销
                    List<BO> shareList=SDK.getBOAPI().query("BO_XR_FM_ORDINARY_SHARE").addQuery("BINDID=",dataList.get(i).getString("ID")).list();
                    String secNo=dataList.get(i).getString("SEC_NO");
                    Double amount=Double.valueOf(dataList.get(i).getString("TOTAL_AMOUNT"));
                    //如果有报销
                    if(shareList!=null && shareList.size()>0){
                        for(int j=0;j<shareList.size();j++){
                            //获取报销数据
                            String shareDeptId=shareList.get(j).getString("DEPARTMENTID");
                            double shareAmount=Double.valueOf(shareList.get(j).getString("TOTAL_AMOUNT"));
                            //根据分摊数据更新汇总表
                            dao.updateActualSum(year,month,shareAmount,shareDeptId,secNo);
                            //获取申请实际报销金额
                            amount=amount-shareAmount;
                        }
                        //更新汇汇总表数据
                        dao.updateActualSum(year,month,amount,deptId,secNo);
                    }else{
                        //如果没有报销,更新汇总表数据
                        dao.updateActualSum(year,month,amount,deptId,secNo);
                    }
                }
            }
            //根据bindid获取，普通费用分摊显示表数据
            List<BO> shareShowList=SDK.getBOAPI().query("BO_XR_FM_ORDINARY_SHARE_SHOW").addQuery("BINDID=",bindId).list();
            if(shareShowList!=null && shareShowList.size()>0){
                for(int i=0;i<shareShowList.size();i++){
                    //根据二级科目编号，传入对应字段
                    String secNo=shareShowList.get(i).getString("SEC_NO");
                    PersonalDataDao dao=new PersonalDataDao();
                    dao.createPersonalData(shareShowList.get(i),bindId,year,secNo);
                }
            }
        }else{
            //更改流程状态为2(非正常结束)
            FlagUtil.updateFlag(boName,bindId,2);
        }

    }
}
