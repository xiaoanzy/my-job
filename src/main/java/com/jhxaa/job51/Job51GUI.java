package com.jhxaa.job51;

import com.jhxaa.util.EmptyUtil;

import java.util.Scanner;

/**
 * 界面
 */
public class Job51GUI {


    /**
     * 菜单
     */
    private void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("================================");
            System.out.println("=   1.进入51job");
            System.out.println("=   2.进入智联招聘");
            System.out.println("=   3.关于");
            System.out.println("=   4.退出应用");
            System.out.println("=   请选择：");
            System.out.println("==============V 1.0=============");
            switch (scanner.next()) {
                case "1":

                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    clear();
                    System.out.println("正在退出应用，请稍后");
                    System.exit(0);
                    break;
                default:
                    System.out.println("请选择正确的选项");
                    continue;
            }
            clear();
        }
    }

    /**
     * 登录
     */
    private void login() {
        Scanner scanner = new Scanner(System.in);
        String userName = null, passWord = null;
        while (true) {
            System.out.println("请输入账号：");
            userName = scanner.nextLine();
            System.out.println("请输入密码：");
            passWord = scanner.nextLine();
            if (EmptyUtil.isEmptyString(userName) || EmptyUtil.isEmptyString(passWord)) {
                System.out.println("账号或密码不能为空！");
                continue;
            }
            break;
        }

    }

    /**
     * 清理屏幕
     */
    private void clear() {
        for (int i = 0; i < 200; i++) {
            System.out.println(" ");
        }
    }

    public void start() {

    }
}
