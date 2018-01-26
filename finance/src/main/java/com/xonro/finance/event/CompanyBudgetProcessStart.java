package com.xonro.finance.event;

import com.actionsoft.bpms.bo.engine.BO;
import com.actionsoft.bpms.bpmn.engine.core.delegate.ProcessExecutionContext;
import com.actionsoft.bpms.bpmn.engine.listener.ExecuteListener;
import com.actionsoft.bpms.util.DBSql;
import com.actionsoft.sdk.local.SDK;
import com.xonro.finance.util.dictUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 公司预算审核流程发起时，自动带入公司本年度的预算汇总数据
 * @author haolh
 * @date 2018-1-19
 */
public class CompanyBudgetProcessStart extends ExecuteListener {

    @Override
    public String getDescription() {
        return "流程启动后，自动带入公司本年度的预算汇总数据";
    }

    @Override
    public void execute(ProcessExecutionContext ctx) throws Exception {
        //获取流程Id
        String bindId = ctx.getProcessInstance().getId();
        //当前年份
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));

        Connection conn = DBSql.open();
        Statement stat = null;
        ResultSet result = null;

        //查询本年度公司的预算 按照科目汇总
        String sql = "select FIR_NO,SEC_NO,SUM(JANUARY) JANUARY,SUM(FEBRUARY) FEBRUARY,SUM(FEBRUARY) FEBRUARY,SUM(APRIL) APRIL,SUM(MAY) MAY,SUM(JUNE) JUNE," +
                "SUM(JULY) JULY,SUM(AUGUST) AUGUST,SUM(SEPTEMBER) SEPTEMBER,SUM(OCTOBER) OCTOBER,SUM(NOVEMBER) NOVEMBER,SUM(DECEMBER) DECEMBER " +
                "from BO_XR_FM_BUDGET_DATA where YEAR='" + year + "' group by FIR_NO,SEC_NO";
        //定义公司年度预算集合
        List<BO> budgetList = new ArrayList<BO>();

        try{
            stat = conn.createStatement();
            //执行sql查询  获得预算数据
            result = stat.executeQuery(sql);
            //获取查询的列
            ResultSetMetaData md = result.getMetaData();
            int columnCount = md.getColumnCount();

            String[] months={"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY",
                    "AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};

            //公司总预算
            BigDecimal budgetSum = new BigDecimal(0);
            while (result.next()) {
                //单科目总预算
                BigDecimal total = new BigDecimal(0);
                //定义BO数据
                BO budgetBo = new BO();
                //循环将列的值插入BO
                for (int i = 1; i <= columnCount; i++) {
                    budgetBo.set(md.getColumnName(i), result.getObject(i));
                    for (String month : months) {
                        if(md.getColumnName(i).equals(month)){
                            total = total.add((BigDecimal)result.getObject(i));
                        }
                    }
                }
                budgetSum = budgetSum.add(total);
                //插入科目名称信息
                budgetBo.set("TOTAL", total);
                budgetBo.set("FIR_NAME", dictUtil.getFirSubjectName(result.getString("FIR_NO")));
                budgetBo.set("SEC_NAME",dictUtil.getFirSubjectName(result.getString("SEC_NO")));
                //将预算放入List集合中
                budgetList.add(budgetBo);
            }
            //公司预算主表
            BO mainBo = new BO();
            mainBo.set("YEAR",year);
            mainBo.set("BUDGETSUM",budgetSum);
            //更新主表年份和总预算
            SDK.getBOAPI().updateByBindId("BO_XR_FM_BUDGET",bindId,"YEAR",year);
            SDK.getBOAPI().updateByBindId("BO_XR_FM_BUDGET",bindId,"BUDGETSUM",budgetSum);
            //插入预算汇总数据
            SDK.getBOAPI().create("BO_XR_FM_BUDGET_S",budgetList,bindId,ctx.getUserContext().getUID());
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            DBSql.close(conn);
        }

    }

}
