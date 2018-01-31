package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.sdk.local.SDK;
import com.xonro.finance.util.FlagUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门预算编制流程结束后,将有效的流程数据插入预算汇总中间表中
 * @author hjj
 * @date 2018-1-15
 */
public class DeptBudgetAExeListener extends ExecuteListener {
    @Override
    public String getDescription() {
        return "流程结束后,将有效的流程数据插入预算汇总中间表中";
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
            //获取部门预算编制流程主表数据
            List<BO> deptList=SDK.getBOAPI().query("BO_XR_FM_DEPT_BUDGET").addQuery("BINDID=",bindId).list();
            //获取部门预算编制流程子表数据
            List<BO> deptSonList=SDK.getBOAPI().query("BO_XR_FM_DEPT_BUDGET_S").addQuery("BINDID=",bindId).list();
            //获取中间表数据
            List<BO> boList= getDeptMiddleData(deptList,deptSonList);
            //将数据插入中间表中
            SDK.getBOAPI().create("BO_XR_FM_BUDGET_DATA",boList,bindId,"admin");

        }else{
            //更改流程状态为2(非正常结束)
            FlagUtil.updateFlag(boName,bindId,2);
        }
    }

    /**
     * 获取部门预算编制中间表数据
     * @param deptList
     * @param deptSonList
     * @return
     */
    public static List<BO> getDeptMiddleData(List<BO> deptList,List<BO> deptSonList){
        List<BO> boList=new ArrayList<BO>();
        if(deptList!=null && deptList.size()>0 && deptSonList!=null && deptSonList.size()>0){
            for(int i=0;i<deptSonList.size();i++){
                BO boData=new BO();
                //年份
                boData.set("YEAR",deptList.get(0).get("YEAR"));
                //预算部门
                boData.set("BUDGET_DEPT",deptList.get(0).get("BUDGET_DEPT"));
                //预算部门ID
                boData.set("BUDGET_DEPTID",deptList.get(0).get("BUDGET_DEPTID"));
                //一级科目编号
                boData.set("FIR_NO",deptSonList.get(i).get("FIR_NO"));
                //一级科目名称
                boData.set("FIR_NAME",deptSonList.get(i).get("FIR_NAME"));
                //二级科目编号
                boData.set("SEC_NO",deptSonList.get(i).get("SEC_NO"));
                //二级科目名称
                boData.set("SEC_NAME",deptSonList.get(i).get("SEC_NAME"));
                //1月份
                boData.set("JANUARY",deptSonList.get(i).get("JANUARY"));
                //2月份
                boData.set("FEBRUARY",deptSonList.get(i).get("FEBRUARY"));
                //3月份
                boData.set("MARCH",deptSonList.get(i).get("MARCH"));
                //4月份
                boData.set("APRIL",deptSonList.get(i).get("APRIL"));
                //5月份
                boData.set("MAY",deptSonList.get(i).get("MAY"));
                //6月份
                boData.set("JUNE",deptSonList.get(i).get("JUNE"));
                //7月份
                boData.set("JULY",deptSonList.get(i).get("JULY"));
                //8月份
                boData.set("AUGUST",deptSonList.get(i).get("AUGUST"));
                //9月份
                boData.set("SEPTEMBER",deptSonList.get(i).get("SEPTEMBER"));
                //10月份
                boData.set("OCTOBER",deptSonList.get(i).get("OCTOBER"));
                //11月份
                boData.set("NOVEMBER",deptSonList.get(i).get("NOVEMBER"));
                //12月份
                boData.set("DECEMBER",deptSonList.get(i).get("DECEMBER"));
                //合计
                boData.set("TOTAL",deptSonList.get(i).get("TOTAL"));
                //占比(%)
                boData.set("RATIO",deptSonList.get(i).get("RATIO"));
                //1月份实际
                boData.set("JANUARY_ACTUAL",0);
                //2月份实际
                boData.set("FEBRUARY_ACTUAL",0);
                //3月份实际
                boData.set("MARCH_ACTUAL",0);
                //4月份实际
                boData.set("APRIL_ACTUAL",0);
                //5月份实际
                boData.set("MAY_ACTUAL",0);
                //6月份实际
                boData.set("JUNE_ACTUAL",0);
                //7月份实际
                boData.set("JULY_ACTUAL",0);
                //8月份实际
                boData.set("AUGUST_ACTUAL",0);
                //9月份实际
                boData.set("SEPTEMBER_ACTUAL",0);
                //10月份实际
                boData.set("OCTOBER_ACTUAL",0);
                //11月份实际
                boData.set("NOVEMBER_ACTUAL",0);
                //12月份实际
                boData.set("DECEMBER_ACTUAL",0);
                //合计实际
                boData.set("TOTAL_ACTUAL",0);
                //占比实际
                boData.set("RATIO_ACTUAL","0");

                //将数据添加到集合中
                boList.add(boData);
            }
        }
        return  boList;
    }
}
