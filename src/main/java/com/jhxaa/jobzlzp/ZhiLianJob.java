package com.jhxaa.jobzlzp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jhxaa.util.FileUtil;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/06/10/9:04
 * @Description:
 */
public class ZhiLianJob {

    private static List<String> disableList = FileUtil.readFileByLines("./config/filter.config");

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "./config/browser_core/chromedriver.exe");
//        System.setProperty("webdriver.chrome.driver", "C:\\Users\\xiaoan\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get("https://www.baidu.com/");
        driver.manage().window().maximize();//浏览器最大化
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);//超时等待30秒
    }

    /**
     * 得到json
     *
     * @return
     */
    public String getSearchResultJson() {
        String searchResultJson = FileUtil.readFileByLines("./config/zlzp_search_result_json.config").get(0);
        return searchResultJson;
    }

    /**
     * 解析json
     *
     * @param jsonStr
     * @return
     */
    public JSONArray parseJsonString(String jsonStr) {
        JSONObject parseObject = JSON.parseObject(jsonStr);
        if (parseObject.getIntValue("code") != 200) {
            return new JSONArray();
        }
        return parseObject.getJSONObject("data").getJSONArray("results");
    }

    /**
     * 过滤公司
     *
     * @param jsonArray
     */
    public void filterCompany(JSONArray jsonArray) {
        String companyName = "";
        JSONObject jsonObject = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            companyName = jsonObject.getJSONObject("company").getString("name");
            if (filter(companyName)) {
                jsonArray.remove(jsonObject);
                i--;
                jsonObject = null;
            }
        }
    }

    /**
     * 得到简历详情页
     *
     * @param jsonArray
     */
    public List<String> getCompanyUrlList(JSONArray jsonArray) {
        String positionURL = "";
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            positionURL = jsonArray.getJSONObject(i).getString("positionURL");
            list.add(positionURL);
        }
        return list;
    }

    /**
     * 根据参数过滤
     *
     * @param name
     * @return
     */
    public boolean filter(String name) {
        for (String s : disableList) {
            if (name.trim().toLowerCase().contains(s.trim().toUpperCase())) {
                return true;
            }
        }
        return false;
    }

}
