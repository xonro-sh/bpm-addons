package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.InterruptListener;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;

import java.util.List;

/**
 * 业务招待费用报销，第一节点办理前事件
 * @author hjj
 * @date 2018-1-29
 */
public class BusinessRBmentTaskBefore extends InterruptListener {
    @Override
    public String getDescription() {
        return "办理前，将分摊数据写到分摊显示子表中";
    }
    @Override
    public boolean execute(ProcessExecutionContext context) throws Exception {
        //获取流程Id
        String bindId=context.getProcessInstance().getId();
        //判断是否含有当前bindId的分摊显示数据
        List<BO> shareShowList=SDK.getBOAPI().query("BO_XR_FM_BUSINESS_SHARE_SHOW").addQuery("BINDID=",bindId).list();
        if(shareShowList!=null && shareShowList.size()>0){
            //删除对应数据
            String deleteSql="DELETE FROM BO_XR_FM_BUSINESS_SHARE_SHOW WHERE BINDID='"+bindId+"'";
            DBSql.update(deleteSql);
        }
        //根据bindId获取业务招待费用子表数据
        List<BO> boList= SDK.getBOAPI().query("BO_XR_FM_BUSINESS_EXPENSE_S").addQuery("BINDID=",bindId).list();
        if(boList!=null && boList.size()>0){
            for(int i=0;i<boList.size();i++){
                //BOID
                String boId=boList.get(i).getString("ID");
                //申请人
                String userName=context.getUserContext().getUserName();
                //申请人部门
                String department=context.getUserContext().getDepartmentModel().getName();
                //请客吃饭费金额
                double mealCost=Double.valueOf(boList.get(i).getString("MEAL_COST"));
                //分摊请客吃饭费金额合计
                double shareMealSum=0;
                //答谢礼物费金额
                double giftCost=Double.valueOf(boList.get(i).getString("GIFT_COST"));
                //分摊答谢礼物费金额合计
                double shareGiftSum=0;
                //其它费用金额
                double otherCost=Double.valueOf(boList.get(i).getString("OTHER_COST"));
                //分摊其它费用合计
                double shareOtherSum=0;
                //备注
                String remark=boList.get(i).getString("REMARK");
                //报销金额
                double amount=Double.valueOf(boList.get(i).getString("TOTAL_AMOUNT"));
                //分摊金额合计
                double shareSum=0;
                //根据boId获取,对应分摊子表数据
                List<BO> shareList=SDK.getBOAPI().query("BO_XR_FM_BUSINESS_SHARE").addQuery("BINDID=",boId).list();
                //判断是否有分摊
                if(shareList!=null && shareList.size()>0){
                    for(int j=0;j<shareList.size();j++){
                        BO data=new BO();
                        //分摊人姓名
                        String shareName=shareList.get(j).getString("USERNAME");
                        //分摊人部门
                        String shareDepartment=shareList.get(j).getString("DEPARTMENTNAME");
                        //分摊请客吃饭金额
                        double shareMeal=Double.valueOf(shareList.get(j).getString("MEAL_COST"));
                        //分摊请客吃饭金额合计
                        shareMealSum=shareMealSum+shareMeal;
                        //分摊答谢礼物费金额
                        double shareGift=Double.valueOf(shareList.get(j).getString("GIFT_COST"));
                        //分摊答谢礼物费金额合计
                        shareGiftSum=shareGiftSum+shareGift;
                        //分摊其它费用金额
                        double shareOther=Double.valueOf(shareList.get(j).getString("OTHER_COST"));
                        //分摊其它费用金额合计
                        shareOtherSum=shareOtherSum+shareOther;
                        //分摊金额
                        double shareAmount=Double.valueOf(shareList.get(j).getString("TOTAL_AMOUNT"));
                        //分摊金额合计
                        shareSum=shareSum+shareAmount;
                        //分摊比例
                        double shareRatio=(shareAmount/amount)*100;
                        //备注
                        String shareRemark=shareList.get(j).getString("REMARK");
                        data.set("EXPENSEMAN",userName);
                        data.set("USERNAME",shareName);
                        data.set("DEPARTMENTNAME",shareDepartment);
                        data.set("MEAL_COST",shareMeal);
                        data.set("GIFT_COST",shareGift);
                        data.set("OTHER_COST",shareOther);
                        data.set("TOTAL_AMOUNT",shareAmount);
                        data.set("REMARK",shareRemark);
                        data.set("SHARE_RATIO",shareRatio);
                        SDK.getBOAPI().create("BO_XR_FM_BUSINESS_SHARE_SHOW",data,bindId,"admin");
                    }
                    BO applyData=new BO();
                    applyData.set("EXPENSEMAN",userName);
                    applyData.set("USERNAME",userName);
                    applyData.set("DEPARTMENTNAME",department);
                    applyData.set("MEAL_COST",mealCost-shareMealSum);
                    applyData.set("GIFT_COST",giftCost-shareGiftSum);
                    applyData.set("OTHER_COST",otherCost-shareOtherSum);
                    applyData.set("TOTAL_AMOUNT",amount-shareSum);
                    applyData.set("REMARK",remark);
                    applyData.set("SHARE_RATIO",((amount-shareSum)/amount)*100);
                    SDK.getBOAPI().create("BO_XR_FM_BUSINESS_SHARE_SHOW",applyData,bindId,"admin");
                }else{
                    //没有分摊
                    BO applyData=new BO();
                    applyData.set("EXPENSEMAN",userName);
                    applyData.set("USERNAME",userName);
                    applyData.set("DEPARTMENTNAME",department);
                    applyData.set("MEAL_COST",mealCost);
                    applyData.set("GIFT_COST",giftCost);
                    applyData.set("OTHER_COST",otherCost);
                    applyData.set("TOTAL_AMOUNT",amount);
                    applyData.set("REMARK",remark);
                    applyData.set("SHARE_RATIO",100);
                    SDK.getBOAPI().create("BO_XR_FM_BUSINESS_SHARE_SHOW",applyData,bindId,"admin");

                }
            }
        }
        return true;
    }
}
