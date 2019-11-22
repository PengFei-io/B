package com.baizhi.evaluate;

import com.baizhi.model.EvalauteData;
import com.baizhi.model.EvaluateReport;
import com.baizhi.model.HistoryData;

public class RegionEvalaute extends Evaluate {

    public RegionEvalaute(String type) {
        super(type);
    }

    @Override
    public void evalue(EvalauteData evalauteData, HistoryData historyData, EvaluateReport evaluateReport,
                       EvalauteChain chain) {
        System.out.println("处理登陆地域");
        evaluateReport.addReport(this.type,true);
        chain.evaluate(evalauteData,historyData,evaluateReport);
    }
}
