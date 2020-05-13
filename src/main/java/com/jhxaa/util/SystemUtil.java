package com.jhxaa.util;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/14/0:12
 * @Description:
 */
public class SystemUtil {

    /**
     * 关闭系统
     */
    public static void exitSystem() {
        System.out.println("已退出系统...");
        System.exit(0);
    }

    public static void sleep(int sleep) {
        int i = (int) (Math.random() * sleep);
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
