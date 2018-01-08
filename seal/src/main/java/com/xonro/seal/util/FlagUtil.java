package com.xonro.seal.util;

import com.actionsoft.bpms.util.DBSql;

/**
 * 流程状态工具类
 * @author hjj
 * @date  2018/1/5
 */
public class FlagUtil {
    /**
     * 根据传过来的参数，修改对应流程状态
     * @param boName
     * @param bindId
     * @param flag
     */
    public static void updateSealFlag(String boName,String bindId, int flag) {
        if(null!=bindId && !"".equals(bindId)){
            String updateSql="UPDATE "+boName+" SET XORONFLAG="+flag+" WHERE BINDID='"+bindId+"'";
            DBSql.update(updateSql);
        }

    }

    /**
     * 根据bindId判断，该流程是否正常结束
     * Active：活动(运行中)
     * Suspend：挂起(暂停)
     * End：结束(正常)
     * Terminate：终止+结束(异常，terminateEventDefinition)
     * terminateAndCompensate：终止+补偿+结束(异常，terminateEventDefinition)
     * @param bindId
     * @return
     */
    public static String getControlState(String bindId){
        if(null!=bindId && !"".equals(bindId)){
            String sql="SELECT CONTROLSTATE FROM WFC_PROCESS WHERE ID='"+bindId+"'";
            return  DBSql.getString(sql);

        }
        return null;
    }

    /**
     * 根据印章编号修改印章外借流程中，是否归还字段状态
     * @param sealNo
     */
    public static void updateReturnFalg(String sealNo){
        if(null!=sealNo && !"".equals(sealNo)){
            String sql="UPDATE BO_XR_SM_BORROW SET IFRETURN=1 WHERE SEAL_NO='"+sealNo+"'";
            DBSql.update(sql);
        }

    }
}
