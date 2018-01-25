package com.xonro.finance.util;

import com.actionsoft.bpms.util.DBSql;

/**
 * 字典公共类
 * @author  haolh
 * @date  2018/1/25.
 */
public class dictUtil {
    /**
     * 根据一级科目编号获取一级科目名称
     * @param firSubjectNo
     * @return
     */
    public static String getFirSubjectName(String firSubjectNo){
        return DBSql.getString("select CNNAME from BO_ACT_DICT_KV_ITEM " +
                "where DICTKEY='FINANCE.SUBJECT' and ITEMNO='"+firSubjectNo+"'");
    }
    /**
     * 根据二级科目编号获取二级科目名称
     * @param secSubjectNo
     * @return
     */
    public static String getSecSubjectName(String secSubjectNo){
        return DBSql.getString("select CNNAME from BO_ACT_DICT_KV_ITEM " +
                "where DICTKEY='FINANCE.SUBJECT' and ITEMNO='"+secSubjectNo+"'");
    }
}
