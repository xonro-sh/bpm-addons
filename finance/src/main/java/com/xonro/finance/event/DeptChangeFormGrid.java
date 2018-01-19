package com.xonro.finance.event;

import com.actionsoft.bpms.bo.design.model.BOItemModel;
import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.FormGridFilterListener;
import com.actionsoft.bpms.bpmn.engine.listener.FormGridRowLookAndFeel;
import com.actionsoft.bpms.form.design.model.FormItemModel;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *部门预算编制变更，子表格式化事件
 * @author hjj
 * @date 2018-1-19
 */
public class DeptChangeFormGrid extends FormGridFilterListener {
    @Override
    public String orderByStatement(ProcessExecutionContext processExecutionContext) {
        return null;
    }

    @Override
    public FormGridRowLookAndFeel acceptRowData(ProcessExecutionContext context, List<BOItemModel> list, BO bo) {
        //创建一个对象
        FormGridRowLookAndFeel diyLookAndFeel = new FormGridRowLookAndFeel();
        //获取流程Id
        String bindId=context.getProcessInstance().getId();
        //根据流程Id获取，年份以及预算部门Id
        String year= DBSql.getString("SELECT YEAR FROM BO_XR_FM_DEPT_CHANGE_SHOW WHERE BINDID='"+bindId+"'");
        String deptId=DBSql.getString("SELECT BUDGET_DEPTID FROM BO_XR_FM_DEPT_CHANGE_SHOW WHERE BINDID='"+bindId+"'");
        //根据年份以及预算部门Id，获取汇总表中对应数据
        List<BO> boList= SDK.getBOAPI().query("BO_XR_FM_BUDGET_DATA").addQuery("YEAR=",year).addQuery("BUDGET_DEPTID=",deptId).list();
        List<String> secNoList=new ArrayList<String>();
        if(boList!=null && boList.size()>0){
            String [] monthArray={"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER",
                    "OCTOBER","NOVEMBER","DECEMBER"};
            //提取汇总表中对应的二级科目编号
            for(int i=0;i<boList.size();i++){
                secNoList.add(boList.get(i).getString("SEC_NO"));
            }
            String secNo=bo.getString("SEC_NO");
            //判断是否新增科目
            if(secNoList.contains(secNo)==false){
                diyLookAndFeel.setCellCSS("style='background-color:yellow;font-color: ffffff;'");
            }else{
                //根据当前年份和预算部门Id，以及当前二级科目Id，获取汇总中间表中对应月份数据,进行判断
                for(int i=0;i<monthArray.length;i++){
                    DecimalFormat df = new DecimalFormat("0.00");
                    double changeData=Double.valueOf(bo.getString(monthArray[i]));
                    double data=getMonthData(monthArray[i],year,deptId,secNo);
                    if(changeData>data){
                        bo.set(monthArray[i],"<a style=\"color: red\">"+df.format(changeData)+"↑</a>");
                    }if(changeData<data){
                        bo.set(monthArray[i],"<a style=\"color: lawngreen\">"+df.format(changeData)+"↓</a>");
                    }
                }
            }
            return  diyLookAndFeel;


        }

        return null;
    }

    @Override
    public String getCustomeTableHeaderHtml(ProcessExecutionContext processExecutionContext, FormItemModel formItemModel, List<String> list) {
        return null;
    }

    /**
     *根据年份、预算部门Id、二级科目编号获取
     * 预算汇总表中对应月份数据
     * @param fieldName
     * @param year
     * @param deptId
     * @param secNo
     * @return
     */
    public double getMonthData(String fieldName,String year,String deptId,String secNo){
        String str=DBSql.getString("SELECT "+fieldName+" FROM BO_XR_FM_BUDGET_DATA WHERE YEAR='"+year+"' " +
                "AND BUDGET_DEPTID='"+deptId+"' AND SEC_NO='"+secNo+"'");
        return  Double.valueOf(str);
    }
}
