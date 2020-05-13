package com.jhxaa.job51;

import com.jhxaa.util.EmptyUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/12/13:30
 * @Description:
 */
public class Job51UrlConfig {


    private Map<String, String> urlMap = new HashMap<>();

    public Job51UrlConfig() {
        init();
    }

    private void init() {
        if (EmptyUtil.isEmpty(urlMap)) {
            urlMap.put("login", "https://login.51job.com/");
            urlMap.put("whoviewme", "https://i.51job.com/userset/ajax/whoviewme.php");
            urlMap.put("search", "https://search.51job.com/list/040000,000000,0000,00,9,99,%s,2,%s.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0,0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=");
            urlMap.put("detail", "https://jobs.51job.com/shenzhen/%s.html?s=01&t=0");
            urlMap.put("submit", "https://i.51job.com/delivery/delivery.php");
            urlMap.put("userset", "https://i.51job.com/userset/my_51job.php");
        }
    }


    public Map getUrlMap() {
        return urlMap;
    }
}
