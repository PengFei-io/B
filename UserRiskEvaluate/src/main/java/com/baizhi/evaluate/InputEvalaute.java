package com.baizhi.evaluate;

import com.baizhi.model.EvalauteData;
import com.baizhi.model.EvaluateReport;
import com.baizhi.model.HistoryData;

public class InputEvalaute extends Evaluate {

    public InputEvalaute(String type) {
        super(type);
    }

    @Override
    public void evalue(EvalauteData evalauteData,
                       HistoryData historyData,
                       EvaluateReport evaluateReport,
                       EvalauteChain chain) {
        System.out.println("处理输入习惯");
        evaluateReport.addReport(this.type, true);
        chain.evaluate(evalauteData, historyData, evaluateReport);
    }
}
