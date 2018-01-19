package com.xonro.finance.web;

import com.actionsoft.bpms.server.UserContext;
import com.actionsoft.bpms.server.bind.annotation.Controller;
import com.actionsoft.bpms.server.bind.annotation.Mapping;
import com.xonro.finance.web.servlet.GetDeptBudgetInfo;

/**
 * 财务管理系统控制器
 * @author hjj
 * @date 2018-1-17
 */
@Controller
public class WebController {
    /**
     *根据年份以及预算部门Id获取数据
     * @param bindId
     * @param year
     * @param deptId
     */
    @Mapping("com.xonro.apps.finance.getDeptBudgetInfo")
    public String getDeptBudgetInfo(UserContext me,String bindId,String year,String deptId){
        GetDeptBudgetInfo gt=new GetDeptBudgetInfo();
        return gt.getInfo(me,bindId,year,deptId);
    }

}
