package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.InterruptListener;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;

import java.util.List;

/**
 * 差旅费用报销，第一节点办理前事件
 * @author hjj
 * @date 2018-1-29
 */
public class TravelRBmentTaskBefore extends InterruptListener {
    @Override
    public String getDescription() {
        return "办理前，将分摊数据写到分摊显示子表中";
    }
    @Override
    public boolean execute(ProcessExecutionContext context) throws Exception {
        //获取流程Id
        String bindId=context.getProcessInstance().getId();
        //判断是否含有当前bindId的分摊显示数据
        List<BO> shareShowList=SDK.getBOAPI().query("BO_XR_FM_TRAVEL_SHARE_SHOW").addQuery("BINDID=",bindId).list();
        if(shareShowList!=null && shareShowList.size()>0){
            //删除对应数据
            String deleteSql="DELETE FROM BO_XR_FM_TRAVEL_SHARE_SHOW WHERE BINDID='"+bindId+"'";
            DBSql.update(deleteSql);
        }
        //根据bindId获取差旅费用子表数据
        List<BO> boList= SDK.getBOAPI().query("BO_XR_FM_TRAVEL_EXPENSE_S").addQuery("BINDID=",bindId).list();
        if(boList!=null && boList.size()>0){
            for(int i=0;i<boList.size();i++){
                //BOID
                String boId=boList.get(i).getString("ID");
                //申请人
                String userName=context.getUserContext().getUserName();
                //申请人Id
                String userId=context.getUserContext().getUID();
                //申请人部门
                String department=context.getUserContext().getDepartmentModel().getName();
                //申请人部门Id
                String departmentId=context.getUserContext().getDepartmentModel().getId();
                //火车表金额
                double trainCost=Double.valueOf(boList.get(i).getString("TRAIN_COST"));
                //分摊火车票金额合计
                double shareTrainSum=0;
                //飞机票金额
                double airCost=Double.valueOf(boList.get(i).getString("AIR_COST"));
                //分摊飞机票金额合计
                double shareAirSum=0;
                //大巴车金额
                double busCost=Double.valueOf(boList.get(i).getString("BUS_COST"));
                //分摊大巴车金额合计
                double shareBusSum=0;
                //住宿费金额
                double hotelCost=Double.valueOf(boList.get(i).getString("HOTEL_COST"));
                //分摊住宿费金额合计
                double shareHotelSum=0;
                //市内交通费金额
                double trafficCost=Double.valueOf(boList.get(i).getString("TRAFFIC_COST"));
                //分摊市内交通费金额合计
                double shareTrafficSum=0;
                //杂费金额
                double otherCost=Double.valueOf(boList.get(i).getString("OTHER_COST"));
                //分摊杂费金额合计
                double shareOtherSum=0;
                //服务费金额
                double serviceCost=Double.valueOf(boList.get(i).getString("SERVICE_COST"));
                //分摊服务费金额合计
                double shareServiceSum=0;
                //备注
                String remark=boList.get(i).getString("REMARK");
                //报销金额
                double amount=Double.valueOf(boList.get(i).getString("TOTAL_AMOUNT"));
                //分摊金额合计
                double shareSum=0;
                //根据boId获取,对应分摊子表数据
                List<BO> shareList=SDK.getBOAPI().query("BO_XR_FM_TRAVEL_SHARE").addQuery("BINDID=",boId).list();
                //判断是否有分摊
                if(shareList!=null && shareList.size()>0){
                    for(int j=0;j<shareList.size();j++){
                        BO data=new BO();
                        //分摊人姓名
                        String shareName=shareList.get(j).getString("USERNAME");
                        //分摊人账号
                        String shareUserId=shareList.get(j).getString("USERID");
                        //分摊人部门
                        String shareDepartment=shareList.get(j).getString("DEPARTMENTNAME");
                        //分摊人部门Id
                        String shareDepartmentId=shareList.get(j).getString("DEPARTMENTID");
                        //分摊火车票金额
                        double shareTrain=Double.valueOf(shareList.get(j).getString("TRAIN_COST"));
                        //分摊火车票金额合计
                        shareTrainSum=shareTrainSum+shareTrain;
                        //分摊飞机票金额
                        double shareAir=Double.valueOf(shareList.get(j).getString("AIR_COST"));
                        //分摊飞机票金额合计
                        shareAirSum=shareAirSum+shareAir;
                        //分摊大巴车费金额
                        double shareBus=Double.valueOf(shareList.get(j).getString("BUS_COST"));
                        //分摊大巴车金额合计
                        shareBusSum=shareBusSum+shareBus;
                        //分摊住宿费金额
                        double shareHotel=Double.valueOf(shareList.get(j).getString("HOTEL_COST"));
                        //分摊住宿费金额合计
                        shareHotelSum=shareHotelSum+shareHotel;
                        //分摊市内交通费金额
                        double shareTraffic=Double.valueOf(shareList.get(j).getString("TRAFFIC_COST"));
                        //分摊市内交通费金额合计
                        shareTrafficSum=shareTrafficSum+shareTraffic;
                        //分摊杂费金额
                        double shareOther=Double.valueOf(shareList.get(j).getString("OTHER_COST"));
                        //分摊杂费金额合计
                        shareOtherSum=shareOtherSum+shareOther;
                        //分摊服务费金额
                        double shareService=Double.valueOf(shareList.get(j).getString("SERVICE_COST"));
                        //分摊服务费金额合计
                        shareServiceSum=shareServiceSum+shareService;
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
                        data.set("USERID",shareUserId);
                        data.set("DEPARTMENTNAME",shareDepartment);
                        data.set("DEPARTMENTID",shareDepartmentId);
                        data.set("TRAIN_COST",shareTrain);
                        data.set("AIR_COST",shareAir);
                        data.set("BUS_COST",shareBus);
                        data.set("HOTEL_COST",shareHotel);
                        data.set("TRAFFIC_COST",shareTraffic);
                        data.set("OTHER_COST",shareOther);
                        data.set("SERVICE_COST",shareService);
                        data.set("TOTAL_AMOUNT",shareAmount);
                        data.set("REMARK",shareRemark);
                        data.set("SHARE_RATIO",shareRatio);
                        SDK.getBOAPI().create("BO_XR_FM_TRAVEL_SHARE_SHOW",data,bindId,"admin");
                    }
                    BO applyData=new BO();
                    applyData.set("EXPENSEMAN",userName);
                    applyData.set("USERNAME",userName);
                    applyData.set("USERID",userId);
                    applyData.set("DEPARTMENTNAME",department);
                    applyData.set("DEPARTMENTID",departmentId);
                    applyData.set("TRAIN_COST",trainCost-shareTrainSum);
                    applyData.set("AIR_COST",airCost-shareAirSum);
                    applyData.set("BUS_COST",busCost-shareBusSum);
                    applyData.set("HOTEL_COST",hotelCost-shareHotelSum);
                    applyData.set("TRAFFIC_COST",trafficCost-shareTrafficSum);
                    applyData.set("OTHER_COST",otherCost-shareOtherSum);
                    applyData.set("SERVICE_COST",serviceCost-shareServiceSum);
                    applyData.set("TOTAL_AMOUNT",amount-shareSum);
                    applyData.set("REMARK",remark);
                    applyData.set("SHARE_RATIO",((amount-shareSum)/amount)*100);
                    SDK.getBOAPI().create("BO_XR_FM_TRAVEL_SHARE_SHOW",applyData,bindId,"admin");
                }else{
                    //没有分摊
                    BO applyData=new BO();
                    applyData.set("EXPENSEMAN",userName);
                    applyData.set("USERNAME",userName);
                    applyData.set("USERID",userId);
                    applyData.set("DEPARTMENTNAME",department);
                    applyData.set("DEPARTMENTID",departmentId);
                    applyData.set("TRAIN_COST",trainCost);
                    applyData.set("AIR_COST",airCost);
                    applyData.set("BUS_COST",busCost);
                    applyData.set("HOTEL_COST",hotelCost);
                    applyData.set("TRAFFIC_COST",trafficCost);
                    applyData.set("OTHER_COST",otherCost);
                    applyData.set("SERVICE_COST",serviceCost);
                    applyData.set("TOTAL_AMOUNT",amount);
                    applyData.set("REMARK",remark);
                    applyData.set("SHARE_RATIO",100);
                    SDK.getBOAPI().create("BO_XR_FM_TRAVEL_SHARE_SHOW",applyData,bindId,"admin");

                }
            }
        }
        return true;
    }
}
