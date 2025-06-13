package com.labmanagement.gui;

import com.labmanagement.entity.User;
import com.labmanagement.service.UserService;
import com.labmanagement.service.UserServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserService userService;

    public LoginFrame() {
        try {
            System.out.println("LoginFrame constructor called.");
            userService = new UserServiceImpl();
            setTitle("实验室管理系统 - 登录");
            setSize(300, 150);
            setLocation(100, 100);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2));

            panel.add(new JLabel("用户名:"));
            usernameField = new JTextField();
            panel.add(usernameField);

            panel.add(new JLabel("密码:"));
            passwordField = new JPasswordField();
            panel.add(passwordField);

            loginButton = new JButton("登录");
            panel.add(loginButton);

            add(panel);

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());

                    User user = userService.login(username, password);
                    if (user != null) {
                        JOptionPane.showMessageDialog(LoginFrame.this, "登录成功！");
                        dispose();

                        // 根据用户角色直接跳转到对应界面
                        String role = user.getUsercol();
                        switch (role) {
                            case "student":
                                new StudentFrame(user).setVisible(true);
                                break;
                            case "teacher":
                                new TeacherFrame(user).setVisible(true);
                                break;
                            case "admin":
                                new AdminFrame(user).setVisible(true);
                                break;
                            default:
                                JOptionPane.showMessageDialog(LoginFrame.this, "未知用户角色！", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            System.out.println("LoginFrame initialized.");
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}