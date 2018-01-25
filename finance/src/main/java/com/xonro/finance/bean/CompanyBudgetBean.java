package com.xonro.finance.bean;

import java.math.BigDecimal;

/**
 * 预算数据汇总Bean
 * @author haolh
 * @date 2018-1-21
 */
public class CompanyBudgetBean {

    String BUDGET_DEPTID;   //预算部门ID
    String BUDGET_DEPT;    //预算部门
    String YEAR;    //年份
    String FIR_NO;  //一级科目编号
    String FIR_NAME;    //一级科目名称
    String SEC_NO;  //二级科目编号
    String SEC_NAME;    //二级科目名称

    BigDecimal JANUARY;     //1月份
    BigDecimal FEBRUARY;    //2月份
    BigDecimal MARCH;       //3月份
    BigDecimal APRIL;       //4月份
    BigDecimal MAY;     //5月份
    BigDecimal JUNE;    //6月份
    BigDecimal JULY;    //7月份
    BigDecimal AUGUST;  //8月份
    BigDecimal SEPTEMBER;   //9月份
    BigDecimal OCTOBER;   //10月份
    BigDecimal NOVEMBER;    //11月份
    BigDecimal DECEMBER;    //12月份
    BigDecimal TOTAL;   //合计
    BigDecimal RATIO;   //占比

    BigDecimal JANUARY_ACTUAL;     //1月份实际
    BigDecimal FEBRUARY_ACTUAL;    //2月份
    BigDecimal MARCH_ACTUAL;       //3月份
    BigDecimal APRIL_ACTUAL;       //4月份
    BigDecimal MAY_ACTUAL;     //5月份
    BigDecimal JUNE_ACTUAL;    //6月份
    BigDecimal JULY_ACTUAL;    //7月份
    BigDecimal AUGUST_ACTUAL;  //8月份
    BigDecimal SEPTEMBER_ACTUAL;   //9月份
    BigDecimal OCTOBER_ACTUAL;   //10月份
    BigDecimal NOVEMBER_ACTUAL;    //11月份
    BigDecimal DECEMBER_ACTUAL;    //12月份
    BigDecimal TOTAL_ACTUAL;   //合计
    BigDecimal RATIO_ACTUAL;   //占比

    public String getBUDGET_DEPTID() {
        return BUDGET_DEPTID;
    }

    public void setBUDGET_DEPTID(String BUDGET_DEPTID) {
        this.BUDGET_DEPTID = BUDGET_DEPTID;
    }

    public String getBUDGET_DEPT() {
        return BUDGET_DEPT;
    }

    public void setBUDGET_DEPT(String BUDGET_DEPT) {
        this.BUDGET_DEPT = BUDGET_DEPT;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public String getFIR_NO() {
        return FIR_NO;
    }

    public void setFIR_NO(String FIR_NO) {
        this.FIR_NO = FIR_NO;
    }

    public String getFIR_NAME() {
        return FIR_NAME;
    }

    public void setFIR_NAME(String FIR_NAME) {
        this.FIR_NAME = FIR_NAME;
    }

    public String getSEC_NO() {
        return SEC_NO;
    }

    public void setSEC_NO(String SEC_NO) {
        this.SEC_NO = SEC_NO;
    }

    public String getSEC_NAME() {
        return SEC_NAME;
    }

    public void setSEC_NAME(String SEC_NAME) {
        this.SEC_NAME = SEC_NAME;
    }

    public BigDecimal getJANUARY() {
        return JANUARY;
    }

    public void setJANUARY(BigDecimal JANUARY) {
        this.JANUARY = JANUARY;
    }

    public BigDecimal getFEBRUARY() {
        return FEBRUARY;
    }

    public void setFEBRUARY(BigDecimal FEBRUARY) {
        this.FEBRUARY = FEBRUARY;
    }

    public BigDecimal getMARCH() {
        return MARCH;
    }

    public void setMARCH(BigDecimal MARCH) {
        this.MARCH = MARCH;
    }

    public BigDecimal getAPRIL() {
        return APRIL;
    }

    public void setAPRIL(BigDecimal APRIL) {
        this.APRIL = APRIL;
    }

    public BigDecimal getMAY() {
        return MAY;
    }

    public void setMAY(BigDecimal MAY) {
        this.MAY = MAY;
    }

    public BigDecimal getJUNE() {
        return JUNE;
    }

    public void setJUNE(BigDecimal JUNE) {
        this.JUNE = JUNE;
    }

    public BigDecimal getJULY() {
        return JULY;
    }

    public void setJULY(BigDecimal JULY) {
        this.JULY = JULY;
    }

    public BigDecimal getAUGUST() {
        return AUGUST;
    }

    public void setAUGUST(BigDecimal AUGUST) {
        this.AUGUST = AUGUST;
    }

    public BigDecimal getSEPTEMBER() {
        return SEPTEMBER;
    }

    public void setSEPTEMBER(BigDecimal SEPTEMBER) {
        this.SEPTEMBER = SEPTEMBER;
    }

    public BigDecimal getOCTOBER() {
        return OCTOBER;
    }

    public void setOCTOBER(BigDecimal OCTOBER) {
        this.OCTOBER = OCTOBER;
    }

    public BigDecimal getNOVEMBER() {
        return NOVEMBER;
    }

    public void setNOVEMBER(BigDecimal NOVEMBER) {
        this.NOVEMBER = NOVEMBER;
    }

    public BigDecimal getDECEMBER() {
        return DECEMBER;
    }

    public void setDECEMBER(BigDecimal DECEMBER) {
        this.DECEMBER = DECEMBER;
    }

    public BigDecimal getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(BigDecimal TOTAL) {
        this.TOTAL = TOTAL;
    }

    public BigDecimal getRATIO() {
        return RATIO;
    }

    public void setRATIO(BigDecimal RATIO) {
        this.RATIO = RATIO;
    }

    public BigDecimal getJANUARY_ACTUAL() {
        return JANUARY_ACTUAL;
    }

    public void setJANUARY_ACTUAL(BigDecimal JANUARY_ACTUAL) {
        this.JANUARY_ACTUAL = JANUARY_ACTUAL;
    }

    public BigDecimal getFEBRUARY_ACTUAL() {
        return FEBRUARY_ACTUAL;
    }

    public void setFEBRUARY_ACTUAL(BigDecimal FEBRUARY_ACTUAL) {
        this.FEBRUARY_ACTUAL = FEBRUARY_ACTUAL;
    }

    public BigDecimal getMARCH_ACTUAL() {
        return MARCH_ACTUAL;
    }

    public void setMARCH_ACTUAL(BigDecimal MARCH_ACTUAL) {
        this.MARCH_ACTUAL = MARCH_ACTUAL;
    }

    public BigDecimal getAPRIL_ACTUAL() {
        return APRIL_ACTUAL;
    }

    public void setAPRIL_ACTUAL(BigDecimal APRIL_ACTUAL) {
        this.APRIL_ACTUAL = APRIL_ACTUAL;
    }

    public BigDecimal getMAY_ACTUAL() {
        return MAY_ACTUAL;
    }

    public void setMAY_ACTUAL(BigDecimal MAY_ACTUAL) {
        this.MAY_ACTUAL = MAY_ACTUAL;
    }

    public BigDecimal getJUNE_ACTUAL() {
        return JUNE_ACTUAL;
    }

    public void setJUNE_ACTUAL(BigDecimal JUNE_ACTUAL) {
        this.JUNE_ACTUAL = JUNE_ACTUAL;
    }

    public BigDecimal getJULY_ACTUAL() {
        return JULY_ACTUAL;
    }

    public void setJULY_ACTUAL(BigDecimal JULY_ACTUAL) {
        this.JULY_ACTUAL = JULY_ACTUAL;
    }

    public BigDecimal getAUGUST_ACTUAL() {
        return AUGUST_ACTUAL;
    }

    public void setAUGUST_ACTUAL(BigDecimal AUGUST_ACTUAL) {
        this.AUGUST_ACTUAL = AUGUST_ACTUAL;
    }

    public BigDecimal getSEPTEMBER_ACTUAL() {
        return SEPTEMBER_ACTUAL;
    }

    public void setSEPTEMBER_ACTUAL(BigDecimal SEPTEMBER_ACTUAL) {
        this.SEPTEMBER_ACTUAL = SEPTEMBER_ACTUAL;
    }

    public BigDecimal getOCTOBER_ACTUAL() {
        return OCTOBER_ACTUAL;
    }

    public void setOCTOBER_ACTUAL(BigDecimal OCTOBER_ACTUAL) {
        this.OCTOBER_ACTUAL = OCTOBER_ACTUAL;
    }

    public BigDecimal getNOVEMBER_ACTUAL() {
        return NOVEMBER_ACTUAL;
    }

    public void setNOVEMBER_ACTUAL(BigDecimal NOVEMBER_ACTUAL) {
        this.NOVEMBER_ACTUAL = NOVEMBER_ACTUAL;
    }

    public BigDecimal getDECEMBER_ACTUAL() {
        return DECEMBER_ACTUAL;
    }

    public void setDECEMBER_ACTUAL(BigDecimal DECEMBER_ACTUAL) {
        this.DECEMBER_ACTUAL = DECEMBER_ACTUAL;
    }

    public BigDecimal getTOTAL_ACTUAL() {
        return TOTAL_ACTUAL;
    }

    public void setTOTAL_ACTUAL(BigDecimal TOTAL_ACTUAL) {
        this.TOTAL_ACTUAL = TOTAL_ACTUAL;
    }

    public BigDecimal getRATIO_ACTUAL() {
        return RATIO_ACTUAL;
    }

    public void setRATIO_ACTUAL(BigDecimal RATIO_ACTUAL) {
        this.RATIO_ACTUAL = RATIO_ACTUAL;
    }



}
