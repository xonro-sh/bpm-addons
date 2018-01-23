package com.xonro.finance.web.server;

import com.actionsoft.bpms.commons.database.RowMap;
import com.actionsoft.bpms.commons.echarts.EChartsOptionBuilder;
import com.actionsoft.bpms.commons.echarts.EChartsSeries;
import com.actionsoft.bpms.commons.mvc.view.ResponseObject;
import com.actionsoft.bpms.util.DBSql;
import com.xonro.finance.util.MonthUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 点击月份，显示对应图形报表
 * @author hjj
 * @date  2018-1-23
 */
public class MonthView {
    public String getMonthView(String month,String year,String deptId){
        ResponseObject ro = ResponseObject.newOkResponse();
        EChartsOptionBuilder edChartBuilder = new EChartsOptionBuilder();
        edChartBuilder.options().title("show", Boolean.valueOf(true)).title("text", month+"汇总图形报表").title("subtext","纯属虚构");
        edChartBuilder.options().tooltip("trigger","axis");
        edChartBuilder.options().legend("data",new String[]{"预算金额","报销金额"});
        edChartBuilder.options().xAxis("type","category");
        StringBuffer strSql=new StringBuffer();
        strSql.append("SELECT SUM("+ MonthUtil.getBudgetLable(month)+") AS "+MonthUtil.getBudgetLable(month)+",");
        strSql.append("SUM("+MonthUtil.getActualLable(month)+") AS "+MonthUtil.getActualLable(month)+",");
        strSql.append("SEC_NAME FROM BO_XR_FM_BUDGET_DATA WHERE 1=1 ");
        //判断用户是否选择年份
        if(null!=year && !"".equals(year)){
            strSql.append(" AND YEAR='"+year+"'");
        }
        //判断用户是否选择部门
        if(null!=deptId && !"".equals(deptId)){
            strSql.append(" AND BUDGET_DEPTID='"+deptId+"'");
        }
        strSql.append(" GROUP BY  SEC_NAME");
        List<RowMap> dataList= DBSql.getMaps(strSql.toString(),new HashMap<String, Object>());
        List<String> xData=new ArrayList<>();
        List<Double> bugetData=new ArrayList<>();
        List<Double> actualData=new ArrayList<>();
        if(dataList!=null && dataList.size()>0){
            for(int i=0;i<dataList.size();i++){
                bugetData.add(dataList.get(i).getDouble(MonthUtil.getBudgetLable(month)));
                actualData.add(dataList.get(i).getDouble(MonthUtil.getActualLable(month)));
                xData.add(dataList.get(i).getString("SEC_NAME"));
            }
        }
        edChartBuilder.options().xAxis("data",xData);
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
