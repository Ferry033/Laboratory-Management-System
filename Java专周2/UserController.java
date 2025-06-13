package com.labmanagement.controller;

import com.labmanagement.entity.Lab;
import com.labmanagement.entity.Student;
import com.labmanagement.entity.User;
import com.labmanagement.service.UserService;
import com.labmanagement.service.UserServiceImpl;

import java.util.List;
import java.util.Scanner;

public class UserController {
    private UserService userService = new UserServiceImpl();
    private Scanner scanner = new Scanner(System.in);

    public void login() {
        System.out.print("请输入用户名: ");
        String name = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        User user = userService.login(name, password);
        if (user != null) {
            System.out.println("登录成功！");
            // 根据角色直接进入对应功能
            switch (user.getUsercol()) {
                case "student":
                    studentMenu(user);
                    break;
                case "teacher":
                    teacherMenu(user);
                    break;
                case "admin":
                    adminMenu(user);
                    break;
                default:
                    System.out.println("未知用户角色！");
            }
        } else {
            System.out.println("用户名或密码错误！");
        }
    }

    private void studentMenu(User user) {
        while (true) {
            System.out.println("\n===== 学生功能菜单 =====");
            System.out.println("1. 开始使用实验室");
            System.out.println("2. 下机");
            System.out.println("3. 查看使用记录");
            System.out.println("4. 退出系统");
            System.out.print("请选择: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    startUsingLab(user);
                    break;
                case 2:
                    endUsingLab(user);
                    break;
                case 3:
                    viewUsageRecord(user);
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }

    private void startUsingLab(User user) {
        List<Lab> labList = userService.getAllLabs();
        if (labList.isEmpty()) {
            System.out.println("没有找到任何实验室记录！");
            return;
        }

        System.out.println("实验室列表：");
        System.out.println("ID\t实验室名称");
        for (Lab lab : labList) {
            System.out.println(lab.getLabId() + "\t" + lab.getLabName());
        }

        System.out.print("请输入要使用的实验室ID: ");
        int labId = scanner.nextInt();
        scanner.nextLine();

        boolean labExists = false;
        for (Lab lab : labList) {
            if (lab.getLabId() == labId) {
                labExists = true;
                break;
            }
        }

        if (!labExists) {
            System.out.println("无效的实验室ID！");
            return;
        }

        boolean success = userService.recordStudentLabStart(user.getNum(), labId);
        if (success) {
            System.out.println("成功记录开始使用实验室ID " + labId);
        } else {
            System.out.println("记录失败，可能是该学生已在使用实验室或实验室ID无效！");
        }
    }

    private void endUsingLab(User user) {
        boolean success = userService.recordStudentLabEnd(user.getNum());
        if (success) {
            System.out.println("成功记录结束使用实验室");
        } else {
            System.out.println("记录失败，可能是该学生没有在使用实验室！");
        }
    }

    private void viewUsageRecord(User user) {
        List<Student> studentUsageList = userService.getAllStudentLabUsage();
        System.out.println("\n使用记录：");
        System.out.println("学生ID\t实验室ID\t开始时间\t结束时间");
        for (Student student : studentUsageList) {
            if (student.getStuNum() == user.getNum()) {
                System.out.println(student.getStuNum() + "\t" +
                        student.getLabId() + "\t" +
                        student.getStartTime() + "\t" +
                        student.getEndTime());
            }
        }
    }

    private void teacherMenu(User user) {
        while (true) {
            System.out.println("\n===== 教师功能菜单 =====");
            System.out.println("1. 查看当前学生信息");
            System.out.println("2. 退出系统");
            System.out.print("请选择: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    List<Student> currentStudentUsage = userService.getCurrentStudentLabUsage();
                    if (currentStudentUsage.isEmpty()) {
                        System.out.println("当前没有学生在使用实验室。");
                    } else {
                        System.out.println("当前正在使用实验室的学生信息：");
                        System.out.println("学生ID\t实验室ID\t开始时间");
                        for (Student student : currentStudentUsage) {
                            System.out.println(student.getStuNum() + "\t" +
                                    student.getLabId() + "\t" +
                                    student.getStartTime());
                        }
                    }
                    break;
                case 2:
                    System.exit(0);
                    break;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }

    private void adminMenu(User user) {
        while (true) {
            System.out.println("\n===== 管理员功能菜单 =====");
            System.out.println("1. 查看所有实验室");
            System.out.println("2. 分配实验室给老师");
            System.out.println("3. 记录学生使用实验室时间");
            System.out.println("4. 开启/关闭实验室");
            System.out.println("5. 退出系统");
            System.out.print("请选择: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllLabs();
                    break;
                case 2:
                    assignLabToTeacher();
                    break;
                case 3:
                    recordStudentLabUsage();
                    break;
                case 4:
                    toggleLabStatus();
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }

    private void viewAllLabs() {
        List<Lab> labList = userService.getAllLabs();
        if (labList.isEmpty()) {
            System.out.println("没有找到任何实验室记录！");
        } else {
            System.out.println("实验室列表：");
            System.out.println("ID\t实验室名称");
            for (Lab lab : labList) {
                System.out.println(lab.getLabId() + "\t" + lab.getLabName());
            }
        }
    }

    private void assignLabToTeacher() {
        List<User> teacherList = userService.getAllTeachers();
        if (teacherList.isEmpty()) {
            System.out.println("没有找到任何老师记录！");
            return;
        }

        System.out.println("老师列表：");
        System.out.println("ID\t老师姓名");
        for (User teacher : teacherList) {
            System.out.println(teacher.getNum() + "\t" + teacher.getName());
        }

        System.out.print("请输入要分配实验室的老师ID: ");
        int teaNum = scanner.nextInt();
        scanner.nextLine();

        boolean teacherExists = false;
        for (User teacher : teacherList) {
            if (teacher.getNum() == teaNum) {
                teacherExists = true;
                break;
            }
        }

        if (!teacherExists) {
            System.out.println("无效的老师ID！");
            return;
        }

        List<Lab> labList = userService.getAllLabs();
        if (labList.isEmpty()) {
            System.out.println("没有找到任何实验室记录！");
            return;
        }

        System.out.println("实验室列表：");
        System.out.println("ID\t实验室名称");
        for (Lab lab : labList) {
            System.out.println(lab.getLabId() + "\t" + lab.getLabName());
        }

        System.out.print("请输入要分配的实验室ID: ");
        int labId = scanner.nextInt();
        scanner.nextLine();

        boolean labExists = false;
        for (Lab lab : labList) {
            if (lab.getLabId() == labId) {
                labExists = true;
                break;
            }
        }

        if (!labExists) {
            System.out.println("无效的实验室ID！");
            return;
        }

        boolean success = userService.assignLabToTeacher(teaNum, labId);
        if (success) {
            System.out.println("成功将实验室ID " + labId + " 分配给老师ID " + teaNum);
        } else {
            System.out.println("分配失败，可能是该老师ID不存在或实验室ID无效！");
        }
    }

    private void recordStudentLabUsage() {
        List<Student> studentUsageList = userService.getAllStudentLabUsage();
        if (studentUsageList.isEmpty()) {
            System.out.println("没有找到任何学生使用实验室的记录！");
            return;
        }

        System.out.println("当前学生使用实验室的记录：");
        System.out.println("记录ID\t学生ID\t实验室ID\t开始时间\t结束时间");
        for (Student student : studentUsageList) {
            System.out.println(student.getId() + "\t" +
                    student.getStuNum() + "\t" +
                    student.getLabId() + "\t" +
                    student.getStartTime() + "\t" +
                    student.getEndTime());
        }

        System.out.print("请输入要结束使用实验室的学生ID (输入0返回): ");
        int stuNum = scanner.nextInt();
        scanner.nextLine();

        if (stuNum == 0) {
            return;
        }

        boolean success = userService.recordStudentLabEnd(stuNum);
        if (success) {
            System.out.println("成功记录学生ID " + stuNum + " 结束使用实验室");
        } else {
            System.out.println("记录失败，可能是该学生没有在使用实验室！");
        }
    }

    private void toggleLabStatus() {
        System.out.println("开启/关闭实验室的功能尚未实现，这里可以添加具体的逻辑。");
    }
}