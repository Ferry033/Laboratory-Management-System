package com.labmanagement.service;

import com.labmanagement.entity.Lab;
import com.labmanagement.entity.Student;
import com.labmanagement.entity.User;

import java.util.List;

public interface UserService {
    User login(String name, String password);
    List<User> getAllUsers();
    List<Lab> getAllLabs();  // 新增：获取所有实验室列表
    List<User> getAllTeachers();  // 新增：获取所有老师列表
    boolean assignLabToTeacher(int teaNum, int labId);  // 新增：分配实验室给老师
    boolean recordStudentLabStart(int stuNum, int labId);  // 新增：记录学生开始使用实验室
    boolean recordStudentLabEnd(int stuNum);  // 新增：记录学生结束使用实验室
    List<Student> getAllStudentLabUsage();  // 新增：获取所有学生使用实验室的记录
    List<Student> getCurrentStudentLabUsage();  // 新增：获取当前正在使用实验室的学生信息
}