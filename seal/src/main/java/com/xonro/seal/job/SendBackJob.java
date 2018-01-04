package com.xonro.seal.job;

import com.actionsoft.bpms.commons.database.RowMap;
import com.actionsoft.bpms.schedule.IJob;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检测印章外借达到归还日期，提醒外借人员归还印章
 * @author hjj
 * @date 2018/1/4
 *
 */
public class SendBackJob implements IJob{
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //获取所有正常结束并且计划归还日期为当天的，印章借用流程信息
        String sql="SELECT * FROM BO_XR_SM_BORROW WHERE PLANDATE=now() and XORONFLAG=1";
        Map<String,Object> map = new HashMap<String,Object>();
        List<RowMap> bolist= DBSql.getList(sql,map);

        if(null!=bolist && !"".equals(bolist) && bolist.size()>0){
            //遍历所有印章借用流程信息
            for(int i=0;i<bolist.size();i++){
                //获取借用印章信息，以及申请人账号
                //印章名称
                String sealName=bolist.get(i).getString("SEAL_NAME");
                //申请日期
                String date=bolist.get(i).getString("APPLYDATE");
                //申请人账号
                String userId=bolist.get(i).getString("USERID");
                //申请人姓名
                String userName=bolist.get(i).getString("USERNAME");

                //向申请人推送信息
                SDK.getNotificationAPI().sendMessage("admin",userId,userName+"您与"+date+"申请外借的"+sealName+"今日已到归还日期,请注意归还!");
            }
        }



    }
}
