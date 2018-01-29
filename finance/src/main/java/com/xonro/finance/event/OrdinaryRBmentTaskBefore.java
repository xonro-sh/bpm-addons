package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.InterruptListener;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;

import java.util.List;

/**
 * 普通费用报销，第一节点办理前事件
 * @author hjj
 * @date 2018-1-29
 */
public class OrdinaryRBmentTaskBefore extends InterruptListener {
    @Override
    public String getDescription() {
        return "办理前，将分摊数据写到分摊显示子表中";
    }
    @Override
    public boolean execute(ProcessExecutionContext context) throws Exception {
        //获取流程Id
        String bindId=context.getProcessInstance().getId();
        //判断是否含有当前bindId的分摊显示数据
        List<BO> shareShowList=SDK.getBOAPI().query("BO_XR_FM_ORDINARY_SHARE_SHOW").addQuery("BINDID=",bindId).list();
        if(shareShowList!=null && shareShowList.size()>0){
            //删除对应数据
            String deleteSql="DELETE FROM BO_XR_FM_ORDINARY_SHARE_SHOW WHERE BINDID='"+bindId+"'";
            DBSql.update(deleteSql);
        }
        //根据bindId获取普通费用子表数据
        List<BO> boList= SDK.getBOAPI().query("BO_XR_FM_ORDINARY_EXPENSE_S").addQuery("BINDID=",bindId).list();
        if(boList!=null && boList.size()>0){
            for(int i=0;i<boList.size();i++){
                //BOID
                String boId=boList.get(i).getString("ID");
                //科目名称
                String subjectName=boList.get(i).getString("THI_NAME");
                //申请人
                String userName=context.getUserContext().getUserName();
                //申请人部门
                String department=context.getUserContext().getDepartmentModel().getName();
                //备注
                String remark=boList.get(i).getString("REMARK");
                //报销金额
                double amount=Double.valueOf(boList.get(i).getString("AMOUNT"));
                //分摊金额合计
                double shareSum=0;
                //根据boId获取,对应分摊子表数据
                List<BO> shareList=SDK.getBOAPI().query("BO_XR_FM_ORDINARY_SHARE").addQuery("BINDID=",boId).list();
                //判断是否有分摊
                if(shareList!=null && shareList.size()>0){
                    for(int j=0;j<shareList.size();j++){
                        BO data=new BO();
                        //分摊人姓名
                        String shareName=shareList.get(j).getString("USERNAME");
                        //分摊人部门
                        String shareDepartment=shareList.get(j).getString("DEPARTMENTNAME");
                        //分摊金额
                        double shareAmount=Double.valueOf(shareList.get(j).getString("AMOUNT"));
                        //分摊金额合计
                        shareSum=shareSum+shareAmount;
                        //分摊比例
                        double shareRatio=(shareAmount/amount)*100;
                        //备注
                        String shareRemark=shareList.get(j).getString("REMARK");
                        data.set("THI_NAME",subjectName);
                        data.set("EXPENSEMAN",userName);
                        data.set("USERNAME",shareName);
                        data.set("DEPARTMENTNAME",shareDepartment);
                        data.set("AMOUNT",shareAmount);
                        data.set("REMARK",shareRemark);
                        data.set("SHARE_RATIO",shareRatio);
                        SDK.getBOAPI().create("BO_XR_FM_ORDINARY_SHARE_SHOW",data,bindId,"admin");
                    }
                    BO applyData=new BO();
                    applyData.set("THI_NAME",subjectName);
                    applyData.set("EXPENSEMAN",userName);
                    applyData.set("USERNAME",userName);
                    applyData.set("DEPARTMENTNAME",department);
                    applyData.set("AMOUNT",amount-shareSum);
                    applyData.set("REMARK",remark);
                    applyData.set("SHARE_RATIO",((amount-shareSum)/amount)*100);
                    SDK.getBOAPI().create("BO_XR_FM_ORDINARY_SHARE_SHOW",applyData,bindId,"admin");
                }else{
                    //没有分摊
                    BO applyData=new BO();
                    applyData.set("THI_NAME",subjectName);
                    applyData.set("EXPENSEMAN",userName);
                    applyData.set("USERNAME",userName);
                    applyData.set("DEPARTMENTNAME",department);
                    applyData.set("AMOUNT",amount);
                    applyData.set("REMARK",remark);
                    applyData.set("SHARE_RATIO",100);
                    SDK.getBOAPI().create("BO_XR_FM_ORDINARY_SHARE_SHOW",applyData,bindId,"admin");

                }
            }
        }
        return true;
    }
}
