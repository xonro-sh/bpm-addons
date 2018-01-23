package com.xonro.finance.util;

/**
 * 月份辅助类
 * @author hjj
 * @date 2018-1-23
 */
public class MonthUtil {
    /**
     *根据穿过来的月份，返回对应预算存储字段
     * @param month  月份
     * @return
     */
    public static String  getBudgetLable(String month){
        if(null!=month && !"".equals(month)){
            String[] months={"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};
            for(int i=0;i<months.length;i++){
                if(i==Integer.valueOf(month)){
                    return months[i-1];
                }
            }
        }
        return  null;
    }

    /**
     * 根据穿过来的月份，返回对应报销存储字典
     * @param month
     * @return
     */
    public static  String getActualLable(String month){
        if(null!=month && !"".equals(month)){
            String[] months={"JANUARY_ACTUAL","FEBRUARY_ACTUAL","MARCH_ACTUAL","APRIL_ACTUAL","MAY_ACTUAL","JUNE_ACTUAL","JULY_ACTUAL",
                    "AUGUST_ACTUAL","SEPTEMBER_ACTUAL","OCTOBER_ACTUAL","NOVEMBER_ACTUAL","DECEMBER_ACTUAL"};
            for(int i=0;i<months.length;i++){
                if(i==Integer.valueOf(month)){
                    return months[i-1];
                }
            }
        }
        return null;
    }
}
