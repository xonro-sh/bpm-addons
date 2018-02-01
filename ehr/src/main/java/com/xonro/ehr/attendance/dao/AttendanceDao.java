package com.xonro.ehr.attendance.dao;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.commons.database.RowMapper;
import com.actionsoft.bpms.commons.mvc.dao.DaoObject;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.exception.AWSDataAccessException;
import com.actionsoft.sdk.local.SDK;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 2018-2-1.
 */
public class AttendanceDao extends DaoObject {
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
        return "BO_XR_HR_TC_RECORD";
    }

    @Override
    public RowMapper rowMapper() {
        return null;
    }

    public int[] entryAttendancData(List<BO> inputData,String inputDeptId,String year,String month,String bindId){
        if(inputData!=null && inputData.size()>0){
            List<BO> dataList=new ArrayList<BO>();
            for(int i=0;i<inputData.size();i++){
                BO bo=new BO();
                //员工账号
                String userId=inputData.get(i).getString("USERID");
                //根据员工账号获取员工编号
                String userNo=DBSql.getString("SELECT USERNO FROM orguser WHERE USERID='"+userId+"'");
                //根据录入部门Id获取，部门名称
                String departmentName=DBSql.getString("SELECT DEPARTMENTNAME FROM orgdepartment WHERE ID='"+inputDeptId+"'");
                //公司名称
                String companyName=inputData.get(i).getString("COMPANYNAME");
                //根据公司名称，获取公司Id
                String companyId=DBSql.getString("SELECT ID FROM orgcompany WHERE COMPANYNAME='"+companyName+"'");
                //员工编号
                bo.set("USERNO",userNo);
                //员工账户
                bo.set("USERID",userId);
                //员工姓名
                bo.set("USERNAME",inputData.get(i).get("USERNAME"));
                //部门名称
                bo.set("DEPARTMENTNAME",departmentName);
                //部门ID
                bo.set("DEPARTMENTID",inputDeptId);
                //岗位
                bo.set("POSITIONNAME",inputData.get(i).get("POSITIONNAME"));
                //公司名称
                bo.set("COMPANYNAME",companyName);
                //公司ID
                bo.set("COMPANYID",companyId);
                //月份
                bo.set("MONTH",month);
                //年份
                bo.set("YEAR",year);
                //打卡时间
                bo.set("TIME",inputData.get(i).get("TIME"));
                //数据来源
                bo.set("DATA_SOURCES","1");

                dataList.add(bo);
            }
            return SDK.getBOAPI().create(entityName(),dataList,bindId,"admin");
        }
        return null;
    }
}
