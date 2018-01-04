package com.xonro.seal.util;

import com.actionsoft.bpms.util.DBSql;


/**
 * 辅助工具类
 *
 * @author henry
 * @date  2017/1/2
 */
public class SealUtil {
    public static void updateSealFlag(String sealNo, int flag) {
        try {
            if (!"".equals(sealNo) && null != sealNo) {
                String sql = "update BO_XR_SM_SEAL set FLAG=" + flag
                        + "  where SEAL_NO='" + sealNo + "'";
                DBSql.update(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
