package com.labmanagement.gui;

import com.labmanagement.entity.User;
import com.labmanagement.entity.Lab;
import com.labmanagement.entity.Student;
import com.labmanagement.service.UserService;
import com.labmanagement.service.UserServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentFrame extends JFrame {
    private User user;
    private UserService userService;
    private JButton startLabButton;
    private JButton endLabButton;
    private JButton viewUsageButton;
    private JButton backButton;

    public StudentFrame(User user) {
        this.user = user;
        userService = new UserServiceImpl();
        setTitle("实验室管理系统 - 学生功能");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        startLabButton = new JButton("开始使用实验室");
        endLabButton = new JButton("下机");
        viewUsageButton = new JButton("查看使用记录");
        backButton = new JButton("退出系统");

        panel.add(startLabButton);
        panel.add(endLabButton);
        panel.add(viewUsageButton);
        panel.add(backButton);

        add(panel);

        startLabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startUsingLab();
            }
        });

        endLabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endUsingLab();
            }
        });

        viewUsageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewUsageRecord();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void startUsingLab() {
        List<Lab> labList = userService.getAllLabs();
        if (labList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有找到任何实验室记录！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] labNames = new String[labList.size()];
        for (int i = 0; i < labList.size(); i++) {
            labNames[i] = labList.get(i).getLabName();
        }

        String selectedLab = (String) JOptionPane.showInputDialog(
                this,
                "请选择要使用的实验室:",
                "选择实验室",
                JOptionPane.PLAIN_MESSAGE,
                null,
                labNames,
                labNames[0]);

        if (selectedLab != null) {
            int labId = -1;
            for (Lab lab : labList) {
                if (lab.getLabName().equals(selectedLab)) {
                    labId = lab.getLabId();
                    break;
                }
            }

            if (labId != -1) {
                boolean success = userService.recordStudentLabStart(user.getNum(), labId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "成功记录开始使用实验室: " + selectedLab);
                } else {
                    JOptionPane.showMessageDialog(this, "记录失败，可能是该学生已在使用实验室或实验室ID无效！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void endUsingLab() {
        boolean success = userService.recordStudentLabEnd(user.getNum());
        if (success) {
            JOptionPane.showMessageDialog(this, "成功记录结束使用实验室");
        } else {
            JOptionPane.showMessageDialog(this, "记录失败，可能是该学生没有在使用实验室！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewUsageRecord() {
        List<Student> studentUsageList = userService.getAllStudentLabUsage();
        StringBuilder sb = new StringBuilder();
        sb.append("学生ID\t实验室ID\t开始时间\t结束时间\n");

        for (Student student : studentUsageList) {
            if (student.getStuNum() == user.getNum()) {
                sb.append(student.getStuNum()).append("\t")
                        .append(student.getLabId()).append("\t")
                        .append(student.getStartTime()).append("\t")
                        .append(student.getEndTime()).append("\n");
            }
        }

        if (sb.length() <= "学生ID\t实验室ID\t开始时间\t结束时间\n".length()) {
            JOptionPane.showMessageDialog(this, "没有找到该学生的使用记录！");
        } else {
            JOptionPane.showMessageDialog(this, sb.toString(), "使用记录", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}