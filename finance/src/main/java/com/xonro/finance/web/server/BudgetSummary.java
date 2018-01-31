package com.xonro.finance.web.server;

import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.bpms.util.DBSql;
import com.alibaba.fastjson.JSON;
import com.xonro.finance.bean.CompanyBudgetBean;
import com.xonro.finance.util.dictUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 预算汇总处理
 * @author haolh
 * @date 2018-1-21
 */
public class BudgetSummary {
    /**
     * 获取预算汇总数据
     * @param me
     * @param year
     * @param departmentId
     * @return
     */
    public String getBudget(UserContext me, String year, String departmentId,String subject){
        Connection conn = DBSql.open();
        Statement stat = null;
        ResultSet result = null;

        //获取公司年度预算数据SQL
        StringBuffer sql = new StringBuffer("select FIR_NO,SEC_NO,SUM(JANUARY) JANUARY,SUM(FEBRUARY) FEBRUARY,SUM(MARCH) MARCH,SUM(APRIL) APRIL,SUM(MAY) MAY,SUM(JUNE) JUNE," +
                "SUM(JULY) JULY,SUM(AUGUST) AUGUST,SUM(SEPTEMBER) SEPTEMBER,SUM(OCTOBER) OCTOBER,SUM(NOVEMBER) NOVEMBER,SUM(DECEMBER) DECEMBER,SUM(TOTAL) TOTAL," +
                "SUM(JANUARY_ACTUAL) JANUARY_ACTUAL,SUM(FEBRUARY_ACTUAL) FEBRUARY_ACTUAL,SUM(MARCH_ACTUAL) MARCH_ACTUAL,SUM(APRIL_ACTUAL) APRIL_ACTUAL," +
                "SUM(MAY_ACTUAL) MAY_ACTUAL,SUM(JUNE_ACTUAL) JUNE_ACTUAL,SUM(JULY_ACTUAL) JULY_ACTUAL,SUM(AUGUST_ACTUAL) AUGUST_ACTUAL,SUM(SEPTEMBER_ACTUAL) SEPTEMBER_ACTUAL," +
                "SUM(OCTOBER_ACTUAL) OCTOBER_ACTUAL,SUM(NOVEMBER_ACTUAL) NOVEMBER_ACTUAL,SUM(DECEMBER_ACTUAL) DECEMBER_ACTUAL,SUM(TOTAL_ACTUAL) TOTAL_ACTUAL " +
                "from BO_XR_FM_BUDGET_DATA where YEAR='" + year + "'");
        //增加部门查询
        if(!("".equals(departmentId) || departmentId == null)){
            sql.append(" and BUDGET_DEPTID='"+departmentId+"'");
        }
        //增加科目查询
        if(!("".equals(subject) || subject == null)){
            sql.append(" and SEC_NO='"+subject+"'");
        }
        //根据科目分组查询
        sql.append(" group by FIR_NO,SEC_NO");
        //新建预算集合
        List<CompanyBudgetBean> budget = new ArrayList<CompanyBudgetBean>();
        try{
            stat = conn.createStatement();
            result = stat.executeQuery(sql.toString());
            //从查询结果集 生成集合
            while (result.next()) {
                CompanyBudgetBean companyBudget = new CompanyBudgetBean();
//                companyBudget.setFIR_NO(result.getString("FIR_NO"));
//                companyBudget.setFIR_NAME(dictUtil.getFirSubjectName(result.getString("FIR_NO")));
                companyBudget.setSEC_NO(result.getString("SEC_NO"));
                companyBudget.setSEC_NAME(dictUtil.getSecSubjectName(result.getString("SEC_NO")));
                companyBudget.setJANUARY(new BigDecimal(result.getString("JANUARY")));
                companyBudget.setFEBRUARY(new BigDecimal(result.getString("FEBRUARY")));
                companyBudget.setMARCH(new BigDecimal(result.getString("MARCH")));
                companyBudget.setAPRIL(new BigDecimal(result.getString("APRIL")));
                companyBudget.setMAY(new BigDecimal(result.getString("MAY")));
                companyBudget.setJUNE(new BigDecimal(result.getString("JUNE")));
                companyBudget.setJULY(new BigDecimal(result.getString("JULY")));
                companyBudget.setAUGUST(new BigDecimal(result.getString("AUGUST")));
                companyBudget.setSEPTEMBER(new BigDecimal(result.getString("SEPTEMBER")));
                companyBudget.setOCTOBER(new BigDecimal(result.getString("OCTOBER")));
                companyBudget.setNOVEMBER(new BigDecimal(result.getString("NOVEMBER")));
                companyBudget.setDECEMBER(new BigDecimal(result.getString("DECEMBER")));
                companyBudget.setTOTAL(new BigDecimal(result.getString("TOTAL")));
                companyBudget.setJANUARY_ACTUAL(new BigDecimal(result.getString("JANUARY_ACTUAL")));
                companyBudget.setFEBRUARY_ACTUAL(new BigDecimal(result.getString("FEBRUARY_ACTUAL")));
                companyBudget.setMARCH_ACTUAL(new BigDecimal(result.getString("MARCH_ACTUAL")));
                companyBudget.setAPRIL_ACTUAL(new BigDecimal(result.getString("APRIL_ACTUAL")));
                companyBudget.setMAY_ACTUAL(new BigDecimal(result.getString("MAY_ACTUAL")));
                companyBudget.setJUNE_ACTUAL(new BigDecimal(result.getString("JUNE_ACTUAL")));
                companyBudget.setJULY_ACTUAL(new BigDecimal(result.getString("JULY_ACTUAL")));
                companyBudget.setAUGUST_ACTUAL(new BigDecimal(result.getString("AUGUST_ACTUAL")));
                companyBudget.setSEPTEMBER_ACTUAL(new BigDecimal(result.getString("SEPTEMBER_ACTUAL")));
                companyBudget.setOCTOBER_ACTUAL(new BigDecimal(result.getString("OCTOBER_ACTUAL")));
                companyBudget.setNOVEMBER_ACTUAL(new BigDecimal(result.getString("NOVEMBER_ACTUAL")));
                companyBudget.setDECEMBER_ACTUAL(new BigDecimal(result.getString("DECEMBER_ACTUAL")));
                companyBudget.setTOTAL_ACTUAL(new BigDecimal(result.getString("TOTAL_ACTUAL")));

                //集合中增加预算数据
                budget.add(companyBudget);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBSql.close(conn);
        }
        if(budget.size() == 0){
            return "";
        }
        return JSON.toJSONString(budget);
    }
}
