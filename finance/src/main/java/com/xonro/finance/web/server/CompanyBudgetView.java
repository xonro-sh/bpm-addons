package com.xonro.finance.web.server;

import com.actionsoft.bpms.commons.echarts.EChartsOptionBuilder;
import com.actionsoft.bpms.commons.echarts.EChartsSeries;
import com.actionsoft.bpms.commons.mvc.view.ResponseObject;
import com.actionsoft.bpms.server.UserContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 2018-1-22.
 */
public class CompanyBudgetView {
    public String getCompanyBudgetView(UserContext me,String year){
        ResponseObject ro = ResponseObject.newOkResponse();
        EChartsOptionBuilder edChartBuilder = new EChartsOptionBuilder();
        edChartBuilder.options().title("show", Boolean.valueOf(true)).title("text", "某站点用户访问来源").title("subtext","纯属虚构").title("x", "center");
        edChartBuilder.options().tooltip("show", Boolean.valueOf(true));
        edChartBuilder.options().tooltip("trigger","item");
        edChartBuilder.options().tooltip("formatter", "{a} <br/>{b} : {c} ({d}%)");
        edChartBuilder.options().legend("orient","vertical");
        edChartBuilder.options().legend("x","left");
        String sql="SELECT SEC_NAME,sum(TOTAL) as TOTAL,SUM(TOTAL_ACTUAL) as " +
                "TOTAL_ACTUAL FROM BO_XR_FM_BUDGET_DATA WHERE YEAR='2018' group by SEC_NAME";

        List<String> xData=new ArrayList<>();

        String[] data={"直接访问","邮件营销","联盟广告","视频广告","搜索引擎"};
        edChartBuilder.options().legend("data",data);
        List<Map> edChartDataValues=new ArrayList<>();

        Map v = new HashMap();
        v.put("value","335");
        v.put("name","直接访问");
        Map v1 = new HashMap();
        v1.put("value","310");
        v1.put("name","邮件营销");
        Map v2 = new HashMap();
        v2.put("value","234");
        v2.put("name","联盟广告");
        Map v3 = new HashMap();
        v3.put("value","135");
        v3.put("name","视频广告");
        Map v4 = new HashMap();
        v4.put("value","1548");
        v4.put("name","搜索引擎");
        edChartDataValues.add(v);
        edChartDataValues.add(v1);
        edChartDataValues.add(v2);
        edChartDataValues.add(v3);
        edChartDataValues.add(v4);

        EChartsSeries s1 = new EChartsSeries();
        s1.set("name","访问来源").set("type","pie").set("radius","55%").set("center",new String[] {"50%", "60%"}).set("data",edChartDataValues);
        edChartBuilder.options().seriesPut(s1);
        ro.put("edOption",edChartBuilder.fetch());

        return  ro.toString();
    }

}
