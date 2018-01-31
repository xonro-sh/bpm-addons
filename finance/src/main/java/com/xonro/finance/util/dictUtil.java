package com.xonro.finance.util;

import com.actionsoft.bpms.util.DBSql;
import com.alibaba.fastjson.JSON;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    /**
     * 获取二级科目及科目名称
     * @return
     */
    public static String getSecSubjects(){
        Connection conn = DBSql.open();
        Statement stat = null;
        ResultSet result = null;

        //查询所有二级科目
        String sql = "select ITEMNO,CNNAME from BO_ACT_DICT_KV_ITEM where DICTKEY='FINANCE.SUBJECT' and EXTTEXT1!='' and EXTTEXT3=''";

        //新建科目集合
        List<Map> subjectList = new ArrayList<Map>();
        try{
            stat = conn.createStatement();
            result = stat.executeQuery(sql.toString());
            //从查询结果集 生成集合
            while (result.next()) {
                Map map = new HashMap();
                map.put("ITEMNO",result.getString("ITEMNO"));
                map.put("CNNAME",result.getString("CNNAME"));
                subjectList.add(map);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            DBSql.close(conn);
        }
        return JSON.toJSONString(subjectList);
    }
}
