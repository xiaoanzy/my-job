package com.jhxaa.job51;

import com.jhxaa.util.FileUtil;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/14/0:49
 * @Description:
 */
public class Job51Main {

    static Job51 job51 = new Job51();
    static ArrayList<String> list = FileUtil.readFileByLines("config/job_51_cookies.config");

    public static void main(String[] args) {
        String cookie = list.get(0);
        Element element = job51.doLoginByCookie(cookie);
        job51.collectBySearchResultName("java", 1, 30, cookie);
        job51.filterUnitInfo(cookie);
        job51.insertUrlList();
        job51.showFilterCountInfo();
        // System.out.println(JSON.toJSONString(job51.getUrlList()));
        job51.submitData(cookie);
    }


}