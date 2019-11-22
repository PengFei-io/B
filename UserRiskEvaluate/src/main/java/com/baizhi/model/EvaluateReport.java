package com.baizhi.model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EvaluateReport implements Serializable {
    private Map<String, Boolean> report = new HashMap<>();

    public void addReport(String type, Boolean risk) {
        report.put(type, risk);
    }

    public Map<String, Boolean> getRepoert() {
        return report;
    }
}
