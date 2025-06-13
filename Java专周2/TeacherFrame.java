package com.labmanagement.gui;

import com.labmanagement.entity.User;
import com.labmanagement.entity.Student;
import com.labmanagement.service.UserService;
import com.labmanagement.service.UserServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TeacherFrame extends JFrame {
    private User user;
    private UserService userService;
    private JButton viewStudentsButton;
    private JButton backButton;

    public TeacherFrame(User user) {
        this.user = user;
        userService = new UserServiceImpl();
        setTitle("实验室管理系统 - 教师功能");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        viewStudentsButton = new JButton("查看当前学生信息");
        backButton = new JButton("退出系统");

        panel.add(viewStudentsButton);
        panel.add(backButton);

        add(panel);

        viewStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCurrentStudents();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void viewCurrentStudents() {
        List<Student> currentStudentUsage = userService.getCurrentStudentLabUsage();
        StringBuilder sb = new StringBuilder();
        sb.append("学生ID\t实验室ID\t开始时间\n");

        if (currentStudentUsage.isEmpty()) {
            sb.append("当前没有学生在使用实验室。");
        } else {
            for (Student student : currentStudentUsage) {
                sb.append(student.getStuNum()).append("\t")
                        .append(student.getLabId()).append("\t")
                        .append(student.getStartTime()).append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "当前学生信息", JOptionPane.INFORMATION_MESSAGE);
    }
}