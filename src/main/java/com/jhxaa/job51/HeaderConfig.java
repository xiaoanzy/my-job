package com.jhxaa.job51;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/12/12:38
 * @Description:
 */
public class HeaderConfig {


    /**
     * 得到登录请求头
     *
     * @return
     */
    public Map getUserInfoHeader() {
        Map<String, Object> map = new HashMap<>();
        map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        map.put("Accept-Encoding", "gzip, deflate, br");
        map.put("Accept-Language", "zh-CN,zh;q=0.9");
        map.put("Cache-Control", "max-age=0");
        map.put("Connection", "keep-alive");
        map.put("Host", "i.51job.com");
        map.put("Referer", "https://login.51job.com/login.php");
        map.put("Sec-Fetch-Dest", "document");
        map.put("Sec-Fetch-Mode", "navigate");
        map.put("Sec-Fetch-Site", "same-site");
        map.put("Sec-Fetch-User", "?1");
        map.put("Upgrade-Insecure-Requests", "1");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.7 Safari/537.36");
        return map;
    }

    /**
     * 设置提交数据请求头
     *
     * @return
     */
    public Map getSubmitHeader() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Connection", "keep-alive");
        hashMap.put("Host", "i.51job.com");
        hashMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        hashMap.put("Accept-Encoding", "gzip, deflate, br");
        hashMap.put("Sec-Fetch-Site", "same-site");
        hashMap.put("Sec-Fetch-Mode", "no-cors");
        hashMap.put("Sec-Fetch-Dest", "script");
        hashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.9 Safari/537.36");
        hashMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9;charset=utf-8");
        return hashMap;
    }

//    /**
//     * @return
//     */
//    public Map getCacheCookies() {
//        HashMap hashMap = new HashMap();
//        hashMap.put("guid", "f57293cf71fbdd5cc830d699597962a4");
//        hashMap.put("slife", "lowbrowser%3Dnot%26%7C%26");
//        return hashMap;
//    }

    /**
     * 常用请求头
     *
     * @return
     */
    public Map getConmmonHeader() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.9 Safari/537.36");
        hashMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9;charset=utf-8");
        return hashMap;
    }
}
