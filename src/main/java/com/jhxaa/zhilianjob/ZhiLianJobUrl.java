package com.jhxaa.zhilianjob;

import com.jhxaa.util.EmptyUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/14/21:16
 * @Description:
 */
public class ZhiLianJobUrl {

    private Map<String, String> jobUrl = null;

    public Map<String, String> getJobUrl() {
        return jobUrl;
    }


    private void init() {
        if (EmptyUtil.isEmpty(jobUrl)) {
            jobUrl = new HashMap<>();
            jobUrl.put("", "");
            jobUrl.put("", "");
            jobUrl.put("", "");
            jobUrl.put("", "");
            jobUrl.put("", "");
        }
    }
}
