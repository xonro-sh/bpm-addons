package com.xonro.finance.web;

import com.actionsoft.bpms.commons.htmlframework.HtmlPageTemplate;
import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.bpms.server.bind.annotation.Controller;
import com.actionsoft.bpms.server.bind.annotation.Mapping;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.bpms.util.UtilNumber;
import com.xonro.finance.util.dictUtil;
import com.xonro.finance.web.server.*;

import java.util.HashMap;
import java.util.Map;

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
    /**
     *根据年份以及预算部门Id获取数据
     * @param year
     * @param departmentId
     */
    @Mapping("com.xonro.apps.finance.budgetSummary")
    public String budgetSummary(UserContext me,String year,String departmentId,String subject){
        return new BudgetSummary().getBudget(me,year,departmentId,subject);
    }
    /**
     * 打开预算收支分析页面
     */
    @Mapping("com.xonro.apps.finance.budgetPage")
    public String budgetPage(UserContext me){
        //页面显示的数据
        Map<String, Object> view = new HashMap<String, Object>();
        view.put("sid",me.getSessionId());
        return HtmlPageTemplate.merge("com.xonro.apps.finance","budgetSummary.html", view);
    }
    /**
     * 改变年份刷新公司年度预算数据
     * @param bindId
     * @param year
     */
    @Mapping("com.xonro.apps.finance_updateCompanyBudget")
    public String updateCompanyBudget(UserContext me,String bindId,String year){
        //公司预算审批流程
        CompanyBudgetProcess companyBudgetProcess = new CompanyBudgetProcess();
        return companyBudgetProcess.updateBudget(me,bindId,year);
    }
    /**
     * 获取二级科目数据
     * @param bindId
     * @param year
     */
    @Mapping("com.xonro.apps.finance_getSecSubjects")
    public String getSecSubjects(UserContext me,String bindId,String year){
        //返回二级科目集合
        return dictUtil.getSecSubjects();
    }

    /**
     * 获取金额大写
     * @param totalAmount  合计金额
     * @return
     */
    @Mapping("com.xonro.apps.finance_getAmountBig")
    public String getAmountBig(String totalAmount){
        return UtilNumber.toRMB(Double.valueOf(totalAmount));
    }

}
