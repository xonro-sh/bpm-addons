package com.xonro.finance.web;

import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.bpms.server.bind.annotation.Controller;
import com.actionsoft.bpms.server.bind.annotation.Mapping;
import com.actionsoft.bpms.util.DBSql;
import com.xonro.finance.web.server.GetDeptBudgetInfo;
import com.xonro.finance.web.server.CompanyBudgetView;

/**
 * 财务管理系统控制器
 * @author hjj
 * @date 2018-1-17
 */
@Controller
public class WebController {
    /**
     *根据年份以及预算部门Id获取数据
     * @param bindId
     * @param year
     * @param deptId
     */
    @Mapping("com.xonro.apps.finance_getDeptBudgetInfo")
    public String getDeptBudgetInfo(UserContext me,String bindId,String year,String deptId){
        GetDeptBudgetInfo gt=new GetDeptBudgetInfo();
        return gt.getInfo(me,bindId,year,deptId);
    }

    /**
     * 根据年份以及预算部门Id，获取对应预算金额数据
     * @param year
     * @param deptId
     * @return
     */
    @Mapping("com.xonro.apps.finance_getBudgetSum")
    public  String getBudgetSum(String year,String deptId){
        return DBSql.getString("SELECT BUDGETSUM FROM BO_XR_FM_DEPTBUDGET WHERE YEAR='"+year+"' AND BUDGET_DEPTID='"+deptId+"'");
    }

    /**
     * 测试
     * @return
     */
    @Mapping("com.xonro.apps.finance_getCompanyBudgetView")
     public String getCompanyBudgetView(UserContext me,String year){
        CompanyBudgetView tv=new CompanyBudgetView();
        return tv.getCompanyBudgetView(me,year);
     }


}
