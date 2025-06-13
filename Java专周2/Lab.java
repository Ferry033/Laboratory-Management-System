package com.labmanagement.entity;

public class Lab {
    private int labId;      // 对应数据库的 lab_id
    private String labName; // 对应数据库的 lab_name

    // getters and setters
    public int getLabId() {
        return labId;
    }

    public void setLabId(int labId) {
        this.labId = labId;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }
}