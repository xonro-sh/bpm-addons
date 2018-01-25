package com.xonro.finance.web;

import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.bpms.server.bind.annotation.Controller;
import com.actionsoft.bpms.server.bind.annotation.Mapping;
import com.actionsoft.bpms.util.DBSql;
import com.xonro.finance.web.server.CompanyBudgetView;
import com.xonro.finance.web.server.GetDeptBudgetInfo;
import com.xonro.finance.web.server.MonthView;
import com.xonro.finance.web.server.SubjectView;

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
        return  tv.getCompanyBudgetView(me,year);
     }

    /**
     * 点击科目名称，显示对应图形报表
     * @param secNo  二级科目编号
     * @param year   年份
     * @param deptId 部门编号
     * @return
     */
    @Mapping("com.xonro.apps.finance_getSubjectView")
    public String getSubjectView(String secNo,String year,String deptId){
        SubjectView sv=new SubjectView();
        return  sv.getSubjectView(secNo,year,deptId);
    }

    /**
     * 点击月份，显示对应图形报表
     * @param month   月份
     * @param year    年份
     * @param deptId  部门Id
     * @return
     */
    @Mapping("com.xonro.apps.finance_getMonthView")
    public String getMonthView(String month,String year,String deptId){
        MonthView mv=new MonthView();
        return mv.getMonthView(month,year,deptId);
    }


}
