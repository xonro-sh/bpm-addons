package com.xonro.ehr.attendance.event;

import com.actionsoft.bpms.dw.design.event.DataWindowFormatDataEventInterface;
import com.actionsoft.bpms.dw.exec.data.DataSourceEngine;
import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.bpms.util.DBSql;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 排班查询报表格式化事件
 * @author  hjj
 * @date    2018-2-5
 */
public class SchedulingDataWindowFormat implements DataWindowFormatDataEventInterface {
    @Override
    public void formatData(UserContext userContext, JSONArray datas) {
        String[] days={"DAY1","DAY2","DAY3","DAY4","DAY5","DAY6","DAY7","DAY8","DAY9","DAY10","DAY11","DAY12","DAY13","DAY14","DAY15","DAY16",
                "DAY17","DAY18","DAY19","DAY20","DAY21", "DAY22","DAY23","DAY24","DAY25","DAY26","DAY27","DAY28","DAY29","DAY30","DAY31"};
        for(Object datao:datas){
            //遍历获取单条信息
            JSONObject data = (JSONObject) datao;
            for(int i=0;i<days.length;i++){
                String columnValue=data.getString(days[i]);
                String fieldName=days[i];
                dataDataSource(data,fieldName,columnValue);
            }
        }
    }
    public void dataDataSource(JSONObject data,String fieldName,String columnValue){
        if(columnValue!=null && !columnValue.equals("")){
            //根据班次编号获取班次名称
            String title= DBSql.getString("SELECT TITLE FROM BO_XR_HR_TC_CATEGORY WHERE NO='"+columnValue+"'");
            //获取开始时间
            String stime=DBSql.getString("SELECT STIME FROM BO_XR_HR_TC_CATEGORY WHERE NO='"+columnValue+"'");
            //获取结束时间
            String etime=DBSql.getString("SELECT ETIME FROM BO_XR_HR_TC_CATEGORY WHERE NO='"+columnValue+"'");
            columnValue="<div class=\"tal\" awsui-qtip=\"text:'"+title+"</br>"+stime+"--"+etime+"',position:'bottom',bordercolor:'#FBEED5',color:'#C09853',bgcolor:'#FCF8E3'\">"+title+"</div>";
            data.put(fieldName+ DataSourceEngine.AWS_DW_FIXED_CLOMUN_SHOW_RULE_SUFFIX,columnValue);
        }

    }
}
