package com.xonro.finance.web.server;

import com.actionsoft.bpms.commons.database.RowMap;
import com.actionsoft.bpms.commons.echarts.EChartsOptionBuilder;
import com.actionsoft.bpms.commons.echarts.EChartsSeries;
import com.actionsoft.bpms.commons.mvc.view.ResponseObject;
import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.bpms.util.DBSql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by henry on 2018-1-22.
 */
public class CompanyBudgetView {
    public String getCompanyBudgetView(UserContext me,String year){
        ResponseObject ro = ResponseObject.newOkResponse();
        EChartsOptionBuilder edChartBuilder = new EChartsOptionBuilder();
        edChartBuilder.options().title("show", Boolean.valueOf(true)).title("text", "数据汇总图形报表").title("subtext","纯属虚构");
        edChartBuilder.options().tooltip("trigger","axis");
        edChartBuilder.options().legend("data",new String[]{"预算金额","报销金额"});
        edChartBuilder.options().xAxis("type","category");
        String sql="SELECT SEC_NAME,sum(TOTAL) as TOTAL,SUM(TOTAL_ACTUAL) as " +
                "TOTAL_ACTUAL FROM BO_XR_FM_BUDGET_DATA WHERE YEAR='2018' group by SEC_NAME";
        List<RowMap> dataList= DBSql.getMaps(sql,new HashMap<String, Object>());
        List<String> xData=new ArrayList<>();
        List<Double> bugetData=new ArrayList<>();
        List<Double> actualData=new ArrayList<>();
        if(dataList!=null && dataList.size()>0){
            for(int i=0;i<dataList.size();i++){
                bugetData.add(dataList.get(i).getDouble("TOTAL"));
                actualData.add(dataList.get(i).getDouble("TOTAL_ACTUAL"));
                xData.add(dataList.get(i).getString("SEC_NAME"));
            }
        }
        edChartBuilder.options().xAxis("data",xData);
        edChartBuilder.options().yAxis("type","value");

        EChartsSeries s6 = new EChartsSeries();
        s6.set("name","预算金额").set("type","bar").set("data",bugetData);
        edChartBuilder.options().seriesPut(s6);
        EChartsSeries s7= new EChartsSeries();
        s7.set("name","报销金额").set("type","bar").set("data",actualData);
        edChartBuilder.options().seriesPut(s7);
        ro.put("edOption", edChartBuilder.fetch());

        return  ro.toString();
    }

}
