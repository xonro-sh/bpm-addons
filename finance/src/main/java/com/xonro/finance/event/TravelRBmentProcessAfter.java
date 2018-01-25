package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;
import com.xonro.finance.dao.BudgetDataDao;
import com.xonro.finance.util.FlagUtil;

import java.util.List;

/**
 * 差旅报销流程结束后事件
 * @author hjj
 * @date  2018-1-24
 */
public class TravelRBmentProcessAfter extends ExecuteListener {
    @Override
    public String getDescription() {
        return "流程结束后,将差旅报销金额数据回写到汇总中间表中";
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
        //获取差旅费科目Id
        String secNo="1009";
        //根据bindId判断当前流程是否正常结束
        String CONTROLSTATE = FlagUtil.getControlState(bindId);
        if(CONTROLSTATE.equals("end")) {
            //更改流程状态为1(正常结束)
            FlagUtil.updateFlag(boName, bindId, 1);
            //根据bindId获取差旅报销子表数据
            List<BO> dataList = SDK.getBOAPI().query("BO_XR_FM_TRAVEL_EXPENSE_S").addQuery("BINDID=", bindId).list();
            if (dataList != null && dataList.size() > 0) {
                BudgetDataDao dao=new BudgetDataDao();
                for (int i = 0; i < dataList.size(); i++) {
                    List<BO> shareList = SDK.getBOAPI().query("BO_XR_FM_TRAVEL_SHARE").addQuery("BINDID=", dataList.get(i).getString("ID")).list();
                    double sum = Double.valueOf(dataList.get(i).getString("TOTAL_AMOUNT"));
                    //判断是否分摊
                    if (shareList != null && shareList.size() > 0) {
                        //如果分摊，获取分摊数据
                        for (int j = 0; j < shareList.size(); j++) {
                            //分摊部门
                            String shareDeptId = shareList.get(j).getString("DEPARTMENTID");
                            //分摊金额
                            double totalAmount = Double.valueOf(shareList.get(j).getString("TOTAL_AMOUNT"));
                            //更新汇总数据表数据
                            dao.updateActualSum(year,month,totalAmount,shareDeptId,secNo);
                            //总金额中减去分摊金额
                            sum = sum - totalAmount;
                        }
                        //更新申请人实际报销数据
                        dao.updateActualSum(year,month,sum,deptId,secNo);

                    } else {
                        //更新汇总表对应数据
                        dao.updateActualSum(year,month,sum,deptId,secNo);
                    }
                }
            }
        }else{
            //更改流程状态为2(非正常结束)
            FlagUtil.updateFlag(boName,bindId,2);
        }
    }
}
