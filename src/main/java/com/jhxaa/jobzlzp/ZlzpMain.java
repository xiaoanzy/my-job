package com.jhxaa.jobzlzp;

import com.alibaba.fastjson.JSONArray;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/06/10/10:09
 * @Description:
 */
public class ZlzpMain {

    public static void main(String[] args) throws IOException {
        ZhiLianJob zhiLianJob = new ZhiLianJob();
        String searchResultJson = zhiLianJob.getSearchResultJson();
        JSONArray parseJsonString = zhiLianJob.parseJsonString(searchResultJson);
        zhiLianJob.filterCompany(parseJsonString);
        List<String> companyUrlList = zhiLianJob.getCompanyUrlList(parseJsonString);
        System.out.println(parseJsonString.size());
        try {
            String llq = "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe";
            for (String s : companyUrlList) {
                new ProcessBuilder(llq, s).start();
                Thread.sleep(1100);
            }
        } catch (Exception e) {
            System.out.println("Error executing progarm.");
        }
    }


    public static void getarr(List<String> filterList, int lenght) {
//        List<String> filterList = FileUtil.readFileByLines("./config/filter.config");
        StringBuilder builder = new StringBuilder("var filterNameArr = ");
        builder.append("[");
        for (int i = 0; i < filterList.size(); i++) {
            builder.append(String.format("'%s'", filterList.get(i)));
            if (i != filterList.size() - 1) {
                builder.append(",");
            }
            if (i % lenght == 0) {
                builder.append("\n");
            }

        }
        builder.append("];");
        System.out.println(builder.toString());
    }
}
