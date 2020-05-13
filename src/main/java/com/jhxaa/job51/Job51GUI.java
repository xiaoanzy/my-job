package com.jhxaa.job51;

import com.jhxaa.JobGUI;
import com.jhxaa.util.EmptyUtil;

/**
 * 界面
 */
public class Job51GUI extends JobGUI {

    private static Job51Data job51Data;
    private Job51Executor job51Executor = null;

    public Job51GUI() {
        super();
        if (EmptyUtil.isEmpty(job51Executor)) {
            job51Executor = new Job51Executor();
        }
    }

    public void login() {
        while (true) {
            System.out.println("请输入账号：");
            String username = scanner.nextLine();
            System.out.println("请输入密码：");
            String passWord = scanner.nextLine();

        }
    }

    public void init() {

    }


    /**
     * 运行51job模块
     */
    public void run() {

    }


}
