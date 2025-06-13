package com.labmanagement.entity;

public class Teacher extends User {
    private int teaNum;
    private int labId;

    // getters and setters
    public int getTeaNum() {
        return teaNum;
    }

    public void setTeaNum(int teaNum) {
        this.teaNum = teaNum;
    }

    public int getLabId() {
        return labId;
    }

    public void setLabId(int labId) {
        this.labId = labId;
    }
}