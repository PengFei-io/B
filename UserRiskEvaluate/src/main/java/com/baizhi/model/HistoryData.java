package com.baizhi.model;

import java.io.Serializable;
import java.util.*;

public class HistoryData implements Serializable {
    // 星期一 : "10:1,13:3,20:4,23:3,18:4'
    private Map<String, String> loginHabits = new HashMap<String, String>();
    // 存储最近的10次输入特征
    private List<Integer[]> historyInputFutures = new ArrayList<Integer[]>();
    //历史密码和当前密码 - 乱序
    private Set<String> historyPasswords = new HashSet<String>();

    //记录用户历史登陆城市
    private Set<String> historyCity = new HashSet<String>();
    private String lastCity = null; //用户上一次登陆城市
    private Double latitude = 0.0; // 用户上一次 纬度
    private Double longitude = 0.0;//用户上一次 经度

    //记录上一次用户登陆设备信息
    private String lastAgent = null;

    public HistoryData(Map<String, String> loginHabits, List<Integer[]> historyInputFutures, Set<String> historyPasswords, Set<String> historyCity, String lastCity, Double latitude, Double longitude, String lastAgent) {
        this.loginHabits = loginHabits;
        this.historyInputFutures = historyInputFutures;
        this.historyPasswords = historyPasswords;
        this.historyCity = historyCity;
        this.lastCity = lastCity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastAgent = lastAgent;
    }

    public HistoryData() {
    }

    public Map<String, String> getLoginHabits() {
        return loginHabits;
    }

    public void setLoginHabits(Map<String, String> loginHabits) {
        this.loginHabits = loginHabits;
    }

    public List<Integer[]> getHistoryInputFutures() {
        return historyInputFutures;
    }

    public void setHistoryInputFutures(List<Integer[]> historyInputFutures) {
        this.historyInputFutures = historyInputFutures;
    }

    public Set<String> getHistoryPasswords() {
        return historyPasswords;
    }

    public void setHistoryPasswords(Set<String> historyPasswords) {
        this.historyPasswords = historyPasswords;
    }

    public Set<String> getHistoryCity() {
        return historyCity;
    }

    public void setHistoryCity(Set<String> historyCity) {
        this.historyCity = historyCity;
    }

    public String getLastCity() {
        return lastCity;
    }

    public void setLastCity(String lastCity) {
        this.lastCity = lastCity;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLastAgent() {
        return lastAgent;
    }

    public void setLastAgent(String lastAgent) {
        this.lastAgent = lastAgent;
    }
}
