package com.baizhi.evaluate;

import com.baizhi.model.EvalauteData;
import com.baizhi.model.EvaluateReport;
import com.baizhi.model.HistoryData;

public class PassowdEvalaute extends Evaluate {

    public PassowdEvalaute(String type) {
        super(type);
    }

    @Override
    public void evalue(EvalauteData evalauteData, HistoryData historyData, EvaluateReport evaluateReport,
                       EvalauteChain chain) {
        System.out.println("处理密码输入");
        evaluateReport.addReport(this.type,true);
        chain.evaluate(evalauteData,historyData,evaluateReport);
    }
}
