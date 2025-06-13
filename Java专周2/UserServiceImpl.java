package com.labmanagement.service;

import com.labmanagement.dao.UserDAO;
import com.labmanagement.dao.UserDAOImpl;
import com.labmanagement.entity.Lab;
import com.labmanagement.entity.Student;
import com.labmanagement.entity.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public User login(String name, String password) {
        return userDAO.login(name, password);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public List<Lab> getAllLabs() {
        return userDAO.getAllLabs();
    }

    @Override
    public List<User> getAllTeachers() {
        return userDAO.getAllTeachers();
    }

    @Override
    public boolean assignLabToTeacher(int teaNum, int labId) {
        return userDAO.assignLabToTeacher(teaNum, labId);
    }

    @Override
    public boolean recordStudentLabStart(int stuNum, int labId) {
        return userDAO.recordStudentLabStart(stuNum, labId);
    }

    @Override
    public boolean recordStudentLabEnd(int stuNum) {
        return userDAO.recordStudentLabEnd(stuNum);
    }

    @Override
    public List<Student> getAllStudentLabUsage() {
        return userDAO.getAllStudentLabUsage();
    }

    @Override
    public List<Student> getCurrentStudentLabUsage() {
        return userDAO.getCurrentStudentLabUsage();
    }
}