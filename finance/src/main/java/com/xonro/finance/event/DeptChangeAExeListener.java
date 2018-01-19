package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;
import com.xonro.finance.util.FlagUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门预算编制变更流程结束后，根据年份和预算部门以及二级科目编号，
 * 修改汇总中间表中对应数据
 * @author hjj
 * @date 2018-1-15
 */
public class DeptChangeAExeListener extends ExecuteListener {
    @Override
    public String getDescription() {
        return "流程结束后,根据年份和预算部门以及二级科目编号，修改汇总中间表中对应数据";
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
            //获取预算部门名称
            String deptName=DBSql.getString("SELECT BUDGET_DEPT FROM BO_XR_FM_DEPT_BUDGET_CHANGE WHERE BINDID='"+bindId+"'");
            //根据年份以及预算部门Id，获取汇总中间表中对应数据
            List<BO> dataList=SDK.getBOAPI().query("BO_XR_FM_BUDGET_DATA").addQuery("YEAR=",year).addQuery("BUDGET_DEPTID=",deptId).list();
            //获取部门预算编制变更子表数据
            List<BO> dataChangenList=SDK.getBOAPI().query("BO_XR_FM_DEPT_BUDGET_CHANGE_S").addQuery("BINDID=",bindId).list();
            if(dataList.size()>0 && dataChangenList.size()>0){
                //新增情况

                List<String> insertsecNos=secNoRemoveAll(dataList,dataChangenList,"新增");
                if(insertsecNos!=null && insertsecNos.size()>0){
                    for(int i=0;i<insertsecNos.size();i++){
                        //根据bindId和二级科目Id查询新增的预算数据
                        List<BO> boList=SDK.getBOAPI().query("BO_XR_FM_DEPT_BUDGET_CHANGE_S").addQuery("BINDID=",bindId)
                                .addQuery("SEC_NO=",insertsecNos.get(i)).list();
                        //添加年份、预算部门Id、预算部门
                        boList.get(0).set("YEAR",year);
                        boList.get(0).set("BUDGET_DEPT",deptName);
                        boList.get(0).set("BUDGET_DEPTID",deptId);
                        //向汇总中间表插入数据
                        SDK.getBOAPI().create("BO_XR_FM_BUDGET_DATA",boList.get(0),bindId,"admin");

                    }
                }

                //删除情况
                List<String> deletesecNos=secNoRemoveAll(dataList,dataChangenList,"删除");
                if(deletesecNos!=null && deletesecNos.size()>0){
                    for(int i=0;i<deletesecNos.size();i++){
                        //根据年份和预算部门Id,以及二级科目编号删除汇总中间表中对应数据
                        String deleteSql="DELETE FROM BO_XR_FM_BUDGET_DATA WHERE YEAR='"+year+"' " +
                                "AND BUDGET_DEPTID='"+deptId+"' AND SEC_NO='"+deletesecNos.get(i)+"'";
                        DBSql.update(deleteSql);

                    }
                }

                //正常修改情况
                //根据二级科目编号对比数据
                for(int i=0;i<dataList.size();i++){
                    //获取汇总中间表中二级科目编号
                    String secNo=dataList.get(i).getString("SEC_NO");
                    for(int j=0;j<dataChangenList.size();j++){
                        //获取部门预算编制变更表中二级科目编号
                        String changeSecNo=dataChangenList.get(j).getString("SEC_NO");
                        if(secNo.equals(changeSecNo)){
                            //判断个月预算是否改变

                            int JANUARY=(dataList.get(i).get("JANUARY")==dataChangenList.get(j).get("JANUARY"))?0:1;
                            int FEBRUARY=(dataList.get(i).get("FEBRUARY")==dataChangenList.get(j).get("FEBRUARY"))?0:1;
                            int MARCH=(dataList.get(i).get("MARCH")==dataChangenList.get(j).get("MARCH"))?0:1;
                            int APRIL=(dataList.get(i).get("APRIL")==dataChangenList.get(j).get("APRIL"))?0:1;
                            int MAY=(dataList.get(i).get("MAY")==dataChangenList.get(j).get("MAY"))?0:1;
                            int JUNE=(dataList.get(i).get("JUNE")==dataChangenList.get(j).get("JUNE"))?0:1;
                            int JULY=(dataList.get(i).get("JULY")==dataChangenList.get(j).get("JULY"))?0:1;
                            int AUGUST=(dataList.get(i).get("AUGUST")==dataChangenList.get(j).get("AUGUST"))?0:1;
                            int SEPTEMBER=(dataList.get(i).get("SEPTEMBER")==dataChangenList.get(j).get("SEPTEMBER"))?0:1;
                            int OCTOBER=(dataList.get(i).get("OCTOBER")==dataChangenList.get(j).get("OCTOBER"))?0:1;
                            int NOVEMBER=(dataList.get(i).get("NOVEMBER")==dataChangenList.get(j).get("NOVEMBER"))?0:1;
                            int DECEMBER=(dataList.get(i).get("DECEMBER")==dataChangenList.get(j).get("DECEMBER"))?0:1;

                            int flag=JANUARY+FEBRUARY+MARCH+APRIL+MAY+JUNE+JULY+AUGUST+SEPTEMBER+OCTOBER+NOVEMBER+DECEMBER;
                            if(flag>0){
                                StringBuffer str=new StringBuffer();
                                str.append("UPDATE BO_XR_FM_BUDGET_DATA");
                                str.append(" SET JANUARY="+dataChangenList.get(j).get("JANUARY")+",");
                                str.append("FEBRUARY="+dataChangenList.get(j).get("FEBRUARY")+",");
                                str.append("MARCH="+dataChangenList.get(j).get("MARCH")+",");
                                str.append("APRIL="+dataChangenList.get(j).get("APRIL")+",");
                                str.append("MAY="+dataChangenList.get(j).get("MAY")+",");
                                str.append("JUNE="+dataChangenList.get(j).get("JUNE")+",");
                                str.append("JULY="+dataChangenList.get(j).get("JULY")+",");
                                str.append("AUGUST="+dataChangenList.get(j).get("AUGUST")+",");
                                str.append("SEPTEMBER="+dataChangenList.get(j).get("SEPTEMBER")+",");
                                str.append("OCTOBER="+dataChangenList.get(j).get("OCTOBER")+",");
                                str.append("NOVEMBER="+dataChangenList.get(j).get("NOVEMBER")+",");
                                str.append("DECEMBER="+dataChangenList.get(j).get("DECEMBER")+",");
                                str.append("TOTAL="+dataChangenList.get(j).get("TOTAL")+",");
                                str.append("RATIO='"+dataChangenList.get(j).getString("RATIO")+"'");
                                str.append(" WHERE YEAR='"+dataList.get(i).getString("YEAR")+"' AND BUDGET_DEPTID='"+dataList.get(i).getString("BUDGET_DEPTID")+"'");
                                str.append(" AND SEC_NO='"+dataList.get(i).getString("SEC_NO")+"'");
                            }
                        }
                    }
                }


            }

        }else{
            //更改流程状态为2(非正常结束)
            FlagUtil.updateFlag(boName,bindId,2);
        }
    }

    public static List<String> secNoRemoveAll(List<BO> dataList,List<BO> dataChangeList,String type){
        List<String> secNos=new ArrayList<String>();
        for(int i=0;i<dataList.size();i++){
            secNos.add(dataList.get(i).getString("SEC_NO"));
        }

        List<String> changesecNos=new ArrayList<String>();
        for(int i=0;i<dataChangeList.size();i++){
            changesecNos.add(dataChangeList.get(i).getString("SEC_NO"));
        }

        if(type.equals("新增")){
            changesecNos.removeAll(secNos);
            return changesecNos;
        }if(type.equals("删除")){
            secNos.removeAll(changesecNos);
            return secNos;
        }
        else{
            return null;
        }



    }
}
