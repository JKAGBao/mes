package com.yxst.mes.fragment.bean;

public class InspectLine {
    private String lineName;
    private String startTime;
    private String stopTime;
    private String person;
    private String state;

    public InspectLine(String lineName, String startTime, String endTime, String person, String state) {

        this.lineName = lineName;
        this.startTime = startTime;
        this.stopTime = endTime;
        this.person = person;
        this.state = state;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public String getPerson() {
        return person;
    }

    public String getState() {
        return state;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setState(String state) {
        this.state = state;
    }
}