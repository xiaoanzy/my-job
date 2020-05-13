package com.jhxaa;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/11/23:40
 * @Description:
 */
public class JobGUI {
    protected Scanner scanner;

    public JobGUI() {
        if (null == scanner) {
            scanner = new Scanner(System.in);
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
