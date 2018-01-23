package com.xonro.finance.web.server;

import com.actionsoft.bpms.commons.database.RowMap;
import com.actionsoft.bpms.commons.echarts.EChartsOptionBuilder;
import com.actionsoft.bpms.commons.echarts.EChartsSeries;
import com.actionsoft.bpms.commons.mvc.view.ResponseObject;
import com.actionsoft.bpms.util.DBSql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *点击科目名称，显示对应图形报表
 *@author hjj
 * @date 2018-1-23
 */
public class SubjectView {

    public  String getSubjectView(String secNo,String year,String deptId){
        ResponseObject ro = ResponseObject.newOkResponse();
        EChartsOptionBuilder edChartBuilder = new EChartsOptionBuilder();
        //根据科目编号，获取科目名称
        String secName=DBSql.getString("select CNNAME from BO_ACT_DICT_KV_ITEM where DICTKEY='FINANCE.SUBJECT' and ITEMNO='"+secNo+"'");
        edChartBuilder.options().title("show", Boolean.valueOf(true)).title("text", secName+"图形报表");
        edChartBuilder.options().tooltip("trigger","axis");
        edChartBuilder.options().legend("data",new String[]{"预算金额","报销金额"});
        edChartBuilder.options().xAxis("type","category");
        //根据传入参数获取数据
        StringBuffer strSql=new StringBuffer();
        strSql.append("SELECT SUM(JANUARY) AS JANUARY,SUM(FEBRUARY) AS FEBRUARY,");
        strSql.append("SUM(MARCH) AS MARCH,SUM(APRIL) AS APRIL,SUM(MAY) AS MAY,");
        strSql.append("SUM(JUNE) AS JUNE,SUM(JULY) AS JULY,SUM(AUGUST) AS AUGUST,");
        strSql.append("SUM(SEPTEMBER) AS SEPTEMBER,SUM(OCTOBER) AS OCTOBER,SUM(NOVEMBER) AS NOVEMBER,");
        strSql.append("SUM(DECEMBER) AS DECEMBER,SUM(JANUARY_ACTUAL) AS JANUARY_ACTUAL,SUM(FEBRUARY_ACTUAL) AS FEBRUARY_ACTUAL,");
        strSql.append("SUM(MARCH_ACTUAL) AS MARCH_ACTUAL,SUM(APRIL_ACTUAL) AS APRIL_ACTUAL,SUM(MAY_ACTUAL) AS MAY_ACTUAL,");
        strSql.append("SUM(JUNE_ACTUAL) AS JUNE_ACTUAL,SUM(JULY_ACTUAL) AS JULY_ACTUAL,SUM(AUGUST_ACTUAL) AS AUGUST_ACTUAL,");
        strSql.append("SUM(SEPTEMBER_ACTUAL) AS SEPTEMBER_ACTUAL,SUM(OCTOBER_ACTUAL) AS OCTOBER_ACTUAL,SUM(NOVEMBER_ACTUAL) AS NOVEMBER_ACTUAL,");
        strSql.append("SUM(DECEMBER_ACTUAL) AS DECEMBER_ACTUAL");
        strSql.append(" FROM BO_XR_FM_BUDGET_DATA WHERE SEC_NO='"+secNo+"'");
        //判断用户是否选择年份
        if(null!=year && !"".equals(year)){
            strSql.append(" AND YEAR='"+year+"'");
        }
        //判断用户是否选择部门
        if(null!=deptId && !"".equals(deptId)){
            strSql.append(" AND BUDGET_DEPTID='"+deptId+"'");
        }
        strSql.append(" GROUP BY SEC_NO");
        List<RowMap> dataList= DBSql.getMaps(strSql.toString(),new HashMap<String, Object>());
        List<Double> bugetData=new ArrayList<>();
        List<Double> actualData=new ArrayList<>();
        if(dataList!=null && dataList.size()>0){
            for(int i=0;i<dataList.size();i++){
                //预算
                bugetData.add(dataList.get(0).getDouble("JANUARY"));
                bugetData.add(dataList.get(0).getDouble("FEBRUARY"));
                bugetData.add(dataList.get(0).getDouble("MARCH"));
                bugetData.add(dataList.get(0).getDouble("APRIL"));
                bugetData.add(dataList.get(0).getDouble("MAY"));
                bugetData.add(dataList.get(0).getDouble("JUNE"));
                bugetData.add(dataList.get(0).getDouble("JULY"));
                bugetData.add(dataList.get(0).getDouble("AUGUST"));
                bugetData.add(dataList.get(0).getDouble("SEPTEMBER"));
                bugetData.add(dataList.get(0).getDouble("OCTOBER"));
                bugetData.add(dataList.get(0).getDouble("NOVEMBER"));
                bugetData.add(dataList.get(0).getDouble("DECEMBER"));
                //报销
                actualData.add(dataList.get(0).getDouble("JANUARY_ACTUAL"));
                actualData.add(dataList.get(0).getDouble("FEBRUARY_ACTUAL"));
                actualData.add(dataList.get(0).getDouble("MARCH_ACTUAL"));
                actualData.add(dataList.get(0).getDouble("APRIL_ACTUAL"));
                actualData.add(dataList.get(0).getDouble("MAY_ACTUAL"));
                actualData.add(dataList.get(0).getDouble("JUNE_ACTUAL"));
                actualData.add(dataList.get(0).getDouble("JULY_ACTUAL"));
                actualData.add(dataList.get(0).getDouble("AUGUST_ACTUAL"));
                actualData.add(dataList.get(0).getDouble("SEPTEMBER_ACTUAL"));
                actualData.add(dataList.get(0).getDouble("OCTOBER_ACTUAL"));
                actualData.add(dataList.get(0).getDouble("NOVEMBER_ACTUAL"));
                actualData.add(dataList.get(0).getDouble("DECEMBER_ACTUAL"));
            }
        }
        edChartBuilder.options().xAxis("data",new String[]{"1月份","2月份","3月份","4月份","5月份","6月份","7月份","8月份","9月份","10月份","11月份","12月份"});
        edChartBuilder.options().yAxis("type","value");

        EChartsSeries s1 = new EChartsSeries();
        s1.set("name","预算金额").set("type","bar").set("data",bugetData);
        edChartBuilder.options().seriesPut(s1);
        EChartsSeries s2= new EChartsSeries();
        s2.set("name","报销金额").set("type","bar").set("data",actualData);
        edChartBuilder.options().seriesPut(s2);
        ro.put("edOption", edChartBuilder.fetch());

        return  ro.toString();
    }
}
