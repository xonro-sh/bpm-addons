package com.xonro.ehr.attendance.web;

import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.bpms.server.bind.annotation.Controller;
import com.actionsoft.bpms.server.bind.annotation.Mapping;
import com.xonro.ehr.attendance.web.server.AutoSchedulingWeb;

/**
 * 人事管理模块控制器类
 * @author haolh
 * @date 2018-1-31
 */
@Controller
public class WebController {
    /**
     * 自动生成排班数据
     * @param
     * @param
     * @param
     */
    @Mapping("com.xonro.apps.ehr_autoScheduling")
    public String autoScheduling(UserContext me,String bindId,String year,String month,String type,String departmentId,
                                 String userIdList,String category){
        AutoSchedulingWeb autoSchedulingWeb = new AutoSchedulingWeb();
        return autoSchedulingWeb.autoScheduling(me,bindId,year,month,type,departmentId,userIdList,category);
    }
}