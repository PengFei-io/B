package com.baizhi.evaluate;

import com.baizhi.model.EvalauteData;
import com.baizhi.model.EvaluateReport;
import com.baizhi.model.HistoryData;

import java.util.List;

public class EvalauteChain {
    private List<Evaluate> evaluates;
    private Integer pos = 0;//记录评估位置
    private Integer size;//记录所有的评估大小

    public EvalauteChain(List<Evaluate> evaluates) {
        this.evaluates = evaluates;
        this.size = evaluates.size();
    }

    public void evaluate(EvalauteData evalauteData,
                         HistoryData historyData,
                         EvaluateReport evaluateReport) {
        if (pos < size) {
            pos++;
            Evaluate evaluate = evaluates.get(pos - 1);
            evaluate.evalue(evalauteData, historyData, evaluateReport, this);
        } else {
            return;
        }
    }

}
