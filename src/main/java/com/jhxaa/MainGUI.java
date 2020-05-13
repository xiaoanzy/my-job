package com.jhxaa;

import com.jhxaa.job51.Job51GUI;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/11/23:34
 * @Description:
 */
public class MainGUI {

    static Scanner scanner = new Scanner(System.in);

    public void menu() {
        JobGUI jobGUI = null;
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
                    jobGUI = new Job51GUI();
                    ((Job51GUI) jobGUI).run();
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
                    System.out.println("请选择正确的选项!");
                    continue;
            }
            clear();
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
}
