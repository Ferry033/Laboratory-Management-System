package com.labmanagement;

import com.labmanagement.gui.LoginFrame;  // 确保导入 LoginFrame

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Main class..."); // 调试信息
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Creating LoginFrame..."); // 调试信息
                new LoginFrame();
                System.out.println("LoginFrame created."); // 调试信息
            }
        });
    }
}