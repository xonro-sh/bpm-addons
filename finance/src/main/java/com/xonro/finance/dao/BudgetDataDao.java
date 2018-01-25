package com.xonro.finance.dao;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.commons.database.RowMapper;
import com.actionsoft.bpms.commons.mvc.dao.DaoObject;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.exception.AWSDataAccessException;
import com.actionsoft.sdk.local.SDK;
import com.xonro.finance.util.MonthUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 预算汇总表Dao
 * @author hjj
 * @date 2018-1-22
 */
public class BudgetDataDao extends DaoObject{
    @Override
    public int insert(Object o) throws AWSDataAccessException {
        return 0;
    }

    @Override
    public int update(Object o) throws AWSDataAccessException {
        return 0;
    }

    @Override
    public String entityName() {
        return "BO_XR_FM_BUDGET_DATA";
    }

    @Override
    public RowMapper rowMapper() {
        return null;
    }

    /**
     * 修改汇总表，报销金额数据
     * @param year    年份
     * @param month   月份
     * @param amount  金额
     * @param deptId  部门Id
     * @param secNo   二级科目编号
     * @return
     */
    public int updateActualSum(String year,String month,double amount,String deptId,String secNo){
        StringBuffer sql=new StringBuffer();

        sql.append("UPDATE "+entityName()+" SET ");
        sql.append(MonthUtil.getActualLable(month)+"="+MonthUtil.getActualLable(month)+"+"+amount);
        sql.append(" WHERE YEAR='"+year+"'  AND BUDGET_DEPTID='"+deptId+"'");
        sql.append(" AND SEC_NO='"+secNo+"'");
        //更新汇总表数据
       return DBSql.update(sql.toString());
    }
    /**
     * 将对应条件的中间表数据，预算清0
     * @param year       年份
     * @param deptId     预算部门Id
     * @param secNo      二级科目编号
     * @return
     */
    public int updateZero(String year,String deptId,String secNo){
        StringBuffer strSql=new StringBuffer();
        strSql.append("UPDATE "+entityName());
        strSql.append(" SET JANUARY=0,");
        strSql.append("FEBRUARY=0,");
        strSql.append("MARCH=0,");
        strSql.append("APRIL=0,");
        strSql.append("MAY=0,");
        strSql.append("JUNE=0,");
        strSql.append("JULY=0,");
        strSql.append("AUGUST=0,");
        strSql.append("SEPTEMBER=0,");
        strSql.append("OCTOBER=0,");
        strSql.append("NOVEMBER=0,");
        strSql.append("DECEMBER=0,");
        strSql.append("TOTAL=0,");
        strSql.append("RATIO='0'");
        strSql.append(" WHERE YEAR='"+year+"' AND BUDGET_DEPTID='"+deptId+"'");
        strSql.append(" AND SEC_NO='"+secNo+"'");
        return DBSql.update(strSql.toString());
    }

    /**
     * 提取中间表对应子表数据
     * @param deptId  预算部门Id
     * @param year    年份
     * @return
     */
    public  List<BO> getSonData(String deptId,String year,String secNo){
        //根据预算部门Id和年份获取对应中间数据
        List<BO> boList=getBoList(deptId,year,secNo);
        //提取子表数据
        List<BO> sonList=new ArrayList<BO>();
        if(boList!=null && boList.size()>0){
            for(int i=0;i<boList.size();i++){
                BO data=new BO();

                //一级科目编号
                data.set("FIR_NO",boList.get(i).get("FIR_NO"));
                //一级科目名称
                data.set("FIR_NAME",boList.get(i).get("FIR_NAME"));
                //二级科目编号
                data.set("SEC_NO",boList.get(i).get("SEC_NO"));
                //二级科目名称
                data.set("SEC_NAME",boList.get(i).get("SEC_NAME"));
                //1月份
                data.set("JANUARY",boList.get(i).get("JANUARY"));
                //2月份
                data.set("FEBRUARY",boList.get(i).get("FEBRUARY"));
                //3月份
                data.set("MARCH",boList.get(i).get("MARCH"));
                //4月份
                data.set("APRIL",boList.get(i).get("APRIL"));
                //5月份
                data.set("MAY",boList.get(i).get("MAY"));
                //6月份
                data.set("JUNE",boList.get(i).get("JUNE"));
                //7月份
                data.set("JULY",boList.get(i).get("JULY"));
                //8月份
                data.set("AUGUST",boList.get(i).get("AUGUST"));
                //9月份
                data.set("SEPTEMBER",boList.get(i).get("SEPTEMBER"));
                //10月份
                data.set("OCTOBER",boList.get(i).get("OCTOBER"));
                //11月份
                data.set("NOVEMBER",boList.get(i).get("NOVEMBER"));
                //12月份
                data.set("DECEMBER",boList.get(i).get("DECEMBER"));
                //合计
                data.set("TOTAL",boList.get(i).get("TOTAL"));
                //占比(%)
                data.set("RATIO",boList.get(i).get("RATIO"));

                //将数据添加到集合中
                sonList.add(data);
            }

        }
        return sonList;
    }

    /**
     *根据年份以及预算部门Id,获取中间表所有数据
     * @param deptId   预算部门Id
     * @param year     年份
     * @param secNo    二级科目编号
     * @return
     */
    public List<BO> getBoList(String deptId, String year, String secNo){
        if(!"".equals(secNo) && null!=secNo){
            return SDK.getBOAPI().query(entityName()).addQuery("BUDGET_DEPTID=",deptId).addQuery("YEAR=",year).addQuery("SEC_NO=",secNo).list();
        }
        return SDK.getBOAPI().query(entityName()).addQuery("BUDGET_DEPTID=",deptId).addQuery("YEAR=",year).list();
    }

}
