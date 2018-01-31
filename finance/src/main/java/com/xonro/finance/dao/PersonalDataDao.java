package com.xonro.finance.dao;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.commons.database.RowMapper;
import com.actionsoft.bpms.commons.mvc.dao.DaoObject;
import com.actionsoft.exception.AWSDataAccessException;
import com.actionsoft.sdk.local.SDK;
import com.xonro.finance.util.dictUtil;

/**
 * 个人费用报销Dao
 * @author hjj
 * @date 2018-1-30
 */
public class PersonalDataDao extends DaoObject {
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
        return "BO_XR_FM_PERSONAL_EXPENSE";
    }

    @Override
    public RowMapper rowMapper() {
        return null;
    }

    public int createPersonalData(BO shareShow,String bindId,String year,String secNo){
        BO data=new BO();
        //根据二级科目编号获取科目名称
        String secName= dictUtil.getSecSubjectName(secNo);
        data.set("USERID",shareShow.get("USERID"));
        data.set("USERNAME",shareShow.get("USERNAME"));
        data.set("DEPARTMENTNAME",shareShow.get("DEPARTMENTNAME"));
        data.set("DEPARTMENTID",shareShow.get("DEPARTMENTID"));
        data.set("YEAR",year);
        data.set("SEC_NO",secNo);
        data.set("SEC_NAME",secName);
        data.set("TOTAL_AMOUNT",shareShow.get("TOTAL_AMOUNT"));
        //向个人报销明细表中插入数据
        return  SDK.getBOAPI().create(entityName(),data,bindId,"admin");
    }
}
