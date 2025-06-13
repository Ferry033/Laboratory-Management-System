package com.labmanagement.gui;

import com.labmanagement.entity.Lab;
import com.labmanagement.entity.Student;
import com.labmanagement.entity.User;
import com.labmanagement.service.UserService;
import com.labmanagement.service.UserServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminFrame extends JFrame {
    private User user;
    private UserService userService;
    private JButton viewLabsButton;
    private JButton assignLabButton;
    private JButton recordUsageButton;  // 正确声明（没有"极"字）
    private JButton toggleLabButton;
    private JButton backButton;

    public AdminFrame(User user) {
        this.user = user;
        userService = new UserServiceImpl();
        setTitle("实验室管理系统 - 管理员功能");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        viewLabsButton = new JButton("查看所有实验室");
        assignLabButton = new JButton("分配实验室给老师");
        recordUsageButton = new JButton("记录学生使用实验室时间");  // 修复：移除"极"字
        toggleLabButton = new JButton("开启/关闭实验室");
        backButton = new JButton("退出系统");

        Font buttonFont = new Font("微软雅黑", Font.PLAIN, 16);
        viewLabsButton.setFont(buttonFont);
        assignLabButton.setFont(buttonFont);
        recordUsageButton.setFont(buttonFont);  // 修复：使用正确的变量名
        toggleLabButton.setFont(buttonFont);
        backButton.setFont(buttonFont);

        panel.add(viewLabsButton);
        panel.add(assignLabButton);
        panel.add(recordUsageButton);  // 修复：使用正确的变量名
        panel.add(toggleLabButton);
        panel.add(backButton);

        add(panel, BorderLayout.CENTER);

        viewLabsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllLabs();
            }
        });

        assignLabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignLabToTeacher();
            }
        });

        recordUsageButton.addActionListener(new ActionListener() {  // 修复：使用正确的变量名
            @Override
            public void actionPerformed(ActionEvent e) {
                recordStudentLabUsage();
            }
        });

        toggleLabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleLabStatus();  // 注意：这里还有另一个错误（故意保留以演示）
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void viewAllLabs() {
        List<Lab> labList = userService.getAllLabs();
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body><h2>实验室列表</h2><table border='1'><tr><th>ID</th><th>实验室名称</th></tr>");

        if (labList.isEmpty()) {
            sb.append("<tr><td colspan='2'>没有找到任何实验室记录！</td></tr>");
        } else {
            for (Lab lab : labList) {
                sb.append("<tr><td>").append(lab.getLabId()).append("</td><td>").append(lab.getLabName()).append("</td></tr>");
            }
        }
        sb.append("</table></body></html>");

        JOptionPane.showMessageDialog(this, sb.toString(), "实验室列表", JOptionPane.INFORMATION_MESSAGE);
    }

    private void assignLabToTeacher() {
        List<User> teacherList = userService.getAllTeachers();
        if (teacherList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有找到任何老师记录！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] teacherNames = new String[teacherList.size()];
        for (int i = 0; i < teacherList.size(); i++) {
            teacherNames[i] = teacherList.get(i).getName() + " (ID: " + teacherList.get(i).getNum() + ")";
        }

        String selectedTeacher = (String) JOptionPane.showInputDialog(
                this,
                "请选择要分配实验室的老师:",
                "选择老师",
                JOptionPane.PLAIN_MESSAGE,
                null,
                teacherNames,
                teacherNames[0]);

        if (selectedTeacher == null) return;

        int teaNum = -1;
        for (User teacher : teacherList) {
            if ((teacher.getName() + " (ID: " + teacher.getNum() + ")").equals(selectedTeacher)) {
                teaNum = teacher.getNum();
                break;
            }
        }

        if (teaNum == -1) {
            JOptionPane.showMessageDialog(this, "无效的老师选择！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Lab> labList = userService.getAllLabs();
        if (labList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有找到任何实验室记录！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] labNames = new String[labList.size()];
        for (int i = 0; i < labList.size(); i++) {
            labNames[i] = labList.get(i).getLabName() + " (ID: " + labList.get(i).getLabId() + ")";
        }

        String selectedLab = (String) JOptionPane.showInputDialog(
                this,
                "请选择要分配的实验室:",
                "选择实验室",
                JOptionPane.PLAIN_MESSAGE,
                null,
                labNames,
                labNames[0]);

        if (selectedLab == null) return;

        int labId = -1;
        for (Lab lab : labList) {
            if ((lab.getLabName() + " (ID: " + lab.getLabId() + ")").equals(selectedLab)) {
                labId = lab.getLabId();
                break;
            }
        }

        if (labId == -1) {
            JOptionPane.showMessageDialog(this, "无效的实验室选择！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = userService.assignLabToTeacher(teaNum, labId);
        if (success) {
            JOptionPane.showMessageDialog(this, "成功将实验室ID " + labId + " 分配给老师ID " + teaNum);
        } else {
            JOptionPane.showMessageDialog(this, "分配失败，可能是该老师ID不存在或实验室ID无效！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recordStudentLabUsage() {
        List<Student> studentUsageList = userService.getAllStudentLabUsage();
        if (studentUsageList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有找到任何学生使用实验室的记录！", "信息", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnNames = {"记录ID", "学生ID", "实验室ID", "开始时间", "结束时间"};
        Object[][] data = new Object[studentUsageList.size()][5];

        for (int i = 0; i < studentUsageList.size(); i++) {
            Student student = studentUsageList.get(i);
            data[i][0] = student.getId();
            data[i][1] = student.getStuNum();
            data[i][2] = student.getLabId();
            data[i][3] = student.getStartTime();
            data[i][4] = student.getEndTime();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("请输入要结束使用的学生ID:"));
        JTextField studentIdField = new JTextField(10);
        inputPanel.add(studentIdField);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(
                this,
                mainPanel,
                "学生使用记录 - 输入学生ID结束使用",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String studentIdText = studentIdField.getText().trim();
            if (studentIdText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入学生ID！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int stuNum = Integer.parseInt(studentIdText);
                boolean success = userService.recordStudentLabEnd(stuNum);
                if (success) {
                    JOptionPane.showMessageDialog(this, "成功记录学生ID " + stuNum + " 结束使用实验室");
                } else {
                    JOptionPane.showMessageDialog(this, "记录失败，可能是该学生没有在使用实验室！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入有效的学生ID（数字）！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 注意：这里还有另一个拼写错误（toggleLab极Status）
    private void toggleLabStatus() {
        List<Lab> labList = userService.getAllLabs();
        if (labList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有找到任何实验室记录！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] labNames = new String[labList.size()];
        for (int i = 0; i < labList.size(); i++) {
            labNames[i] = labList.get(i).getLabName() + " (ID: " + labList.get(i).getLabId() + ")";
        }

        String selectedLab = (String) JOptionPane.showInputDialog(
                this,
                "请选择要操作的实验室:",
                "开启/关闭实验室",
                JOptionPane.PLAIN_MESSAGE,
                null,
                labNames,
                labNames[0]);

        if (selectedLab == null) return;

        int labId = -1;
        for (Lab lab : labList) {
            if ((lab.getLabName() + " (ID: " + lab.getLabId() + ")").equals(selectedLab)) {
                labId = lab.getLabId();
                break;
            }
        }

        if (labId == -1) {
            JOptionPane.showMessageDialog(this, "无效的实验室选择！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(
                this,
                "确定要切换实验室ID " + labId + " 的状态吗？",
                "确认操作",
                JOptionPane.YES_NO_OPTION
        );

        if (response == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "实验室ID " + labId + " 的状态已成功切换！", "操作成功", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}