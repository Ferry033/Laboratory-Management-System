package com.labmanagement.dao;

import com.labmanagement.entity.Lab;
import com.labmanagement.entity.Student;
import com.labmanagement.entity.User;
import com.labmanagement.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    @Override
    public User login(String name, String password) {
        User user = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM user WHERE name = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setNum(rs.getInt("num"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setUsercol(rs.getString("usercol"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM user";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setNum(rs.getInt("num"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setUsercol(rs.getString("usercol"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return userList;
    }

    @Override
    public List<Lab> getAllLabs() {
        List<Lab> labList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT lab_id, lab_name FROM lab";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Lab lab = new Lab();
                lab.setLabId(rs.getInt("lab_id"));
                lab.setLabName(rs.getString("lab_name"));
                labList.add(lab);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return labList;
    }

    @Override
    public List<User> getAllTeachers() {
        List<User> teacherList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM user WHERE usercol = 'teacher'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setNum(rs.getInt("num"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setUsercol(rs.getString("usercol"));
                teacherList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return teacherList;
    }

    @Override
    public boolean assignLabToTeacher(int teaNum, int labId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO teacher_lab (tea_num, lab_id) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, teaNum);
            pstmt.setInt(2, labId);
            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, null);
        }

        return success;
    }

    @Override
    public boolean recordStudentLabStart(int stuNum, int labId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String checkSql = "SELECT * FROM student_lab_usage WHERE stu_num = ? AND end_time IS NULL";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, stuNum);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return false; // 学生已经在使用实验室
            }

            String insertSql = "INSERT INTO student_lab_usage (stu_num, lab_id, start_time) VALUES (?, ?, NOW())";
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setInt(1, stuNum);
            pstmt.setInt(2, labId);
            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, null);
        }

        return success;
    }

    @Override
    public boolean recordStudentLabEnd(int stuNum) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            String updateSql = "UPDATE student_lab_usage SET end_time = NOW() WHERE stu_num = ? AND end_time IS NULL";
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setInt(1, stuNum);
            int rowsAffected = pstmt.executeUpdate();
            success = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, null);
        }

        return success;
    }

    @Override
    public List<Student> getAllStudentLabUsage() {
        List<Student> studentUsageList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM student_lab_usage";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStuNum(rs.getInt("stu_num"));
                student.setLabId(rs.getInt("lab_id"));
                student.setStartTime(rs.getTimestamp("start_time"));
                student.setEndTime(rs.getTimestamp("end_time"));
                studentUsageList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return studentUsageList;
    }

    @Override
    public List<Student> getCurrentStudentLabUsage() {
        List<Student> studentUsageList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM student_lab_usage WHERE end_time IS NULL";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStuNum(rs.getInt("stu_num"));
                student.setLabId(rs.getInt("lab_id"));
                student.setStartTime(rs.getTimestamp("start_time"));
                student.setEndTime(rs.getTimestamp("end_time"));
                studentUsageList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return studentUsageList;
    }
}