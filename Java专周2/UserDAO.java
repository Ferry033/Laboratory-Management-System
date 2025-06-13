package com.labmanagement.dao;

import com.labmanagement.entity.Lab;
import com.labmanagement.entity.Student;
import com.labmanagement.entity.User;

import java.util.List;

public interface UserDAO {
    User login(String name, String password);
    List<User> getAllUsers();
    List<Lab> getAllLabs();
    List<User> getAllTeachers();
    boolean assignLabToTeacher(int teaNum, int labId);
    boolean recordStudentLabStart(int stuNum, int labId);
    boolean recordStudentLabEnd(int stuNum);
    List<Student> getAllStudentLabUsage();  // 新增：获取所有学生使用实验室的记录
    List<Student> getCurrentStudentLabUsage();  // 新增：获取当前正在使用实验室的学生信息
}