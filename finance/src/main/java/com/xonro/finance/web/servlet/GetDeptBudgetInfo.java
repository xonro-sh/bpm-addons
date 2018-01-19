package com.xonro.finance.web.servlet;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;
import com.xonro.finance.util.BoDataUtil;

import java.util.List;

/**
 * 根据年份以及预算部门Id获取数据
 * @author hjj
 * @date 2018-1-18
 */
public class GetDeptBudgetInfo {
    public String getInfo(UserContext me,String bindId, String year, String deptId){
        String result="0";
        //根据bindId判断部门预算编制子表中是否有数据
        List<BO> boList=null;
        if(null!=bindId && !"".equals(bindId)){
            boList= SDK.getBOAPI().query("BO_XR_FM_DEPT_BUDGET_CHANGE_S").addQuery("BINDID=",bindId).list();
        }
        //如果有就删除数据
        if(boList!=null && boList.size()>0){
            String sql="DELETE FROM BO_XR_FM_DEPT_BUDGET_CHANGE_S WHERE BINDID='"+bindId+"'";
            int delFlag=DBSql.update(sql);
            if(delFlag==0){
                result="1";
            }

        }
        //根据年份以及预算部门Id，获取部门预算维护表中预算金额
        String budgetSum=DBSql.getString("SELECT BUDGETSUM FROM BO_XR_FM_DEPTBUDGET WHERE YEAR='"+year+"' AND BUDGET_DEPTID='"+deptId+"'");
        //根据年份以及预算部门Id获取汇总中间表中预算数据
        List<BO> sonDataList= BoDataUtil.getSonData(deptId,year,null);
        if(sonDataList!=null && sonDataList.size()>0){
            //重新插入部门预算编制变更子表中
            int[] number=SDK.getBOAPI().create("BO_XR_FM_DEPT_BUDGET_CHANGE_S",sonDataList,bindId,me.getUID());
            if(number==null){
                result="2";
            }
        }
        return result+"|"+budgetSum;
    }
}
