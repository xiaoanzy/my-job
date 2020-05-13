package com.jhxaa.job51;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.jhxaa.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/13/12:22
 * @Description:
 */
public class Job51 {

    private static List<String> disableList = FileUtil.readFileByLines("./config/filter.config");
    private static HeaderConfig headerConfig = null;
    //提交链接
    private static List<String> urlList = new ArrayList<>();
    private Job51Data job51Data = null;
    private Job51UrlConfig job51UrlConfig = null;
    private Map<String, String> accessUrl = null;
    //公司信息map
    private Map<String, String> unitInfo = new HashMap<>();

    public Job51() {
        init();
    }

    public static void main(String[] args) {

    }

    /**
     * 拼接jobID
     *
     * @param set
     * @return
     */
    private static String getJobId(Set<String> set) {
        String res = null;
        int length = set.size();
        int i = 1;
        StringBuilder builder = new StringBuilder();
        for (String s : set) {
            builder.append(s);
            builder.append(":0");
            if (length != i) {
                builder.append(",");
            }
            i++;
        }
        //URLEncoder.encode(builder.toString(), "utf-8")
//        res = builder.toString();
        try {
            res = URLEncoder.encode(builder.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "(" + res + ")";
    }

    /**
     * 保存提交集合
     *
     * @param set
     */
    private static void saveUrl(Set<String> set) {
        String text = "?rand=" + Math.random() + "&jsoncallback=jQuery18300098957260373327_" + System.currentTimeMillis() + "&jobid=" + getJobId(set) + "&prd=search.51job.com&prp=01&cd=search.51job.com&cp=01&resumeid=&cvlan=&coverid=&qpostset=&elementname=delivery_jobid&deliverytype=2&deliverydomain=%2F%2Fi.51job.com&language=c&imgpath=%2F%2Fimg03.51jobcdn.com&_=" + System.currentTimeMillis();
        urlList.add(text);
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void init() {
        if (EmptyUtil.isNotEmpty(unitInfo)) {
            unitInfo.clear();
        }
        if (EmptyUtil.isEmpty(job51Data)) {
            job51Data = new Job51Data();
        }
        if (EmptyUtil.isEmpty(job51UrlConfig)) {
            job51UrlConfig = new Job51UrlConfig();
        }
        if (EmptyUtil.isEmpty(headerConfig)) {
            headerConfig = new HeaderConfig();
        }
        if (EmptyUtil.isEmpty(accessUrl)) {
            accessUrl = new Job51UrlConfig().getUrlMap();
        }
    }

    /**
     * 按cookies登录
     *
     * @param cookie
     */
    public Element doLoginByCookie(String cookie) {
        Document document = HttpClientUtil.doGet(
                accessUrl.get("userset"),
                headerConfig.getUserInfoHeader(),
                null,
                cookie
        );
        return document.body();
    }

    /**
     * 保存用户数据
     *
     * @param document
     * @param cookie
     */
    public void saveUserData(Document document, String cookie) {
        job51Data.setUserName(getUserName(document));
        job51Data.setSendCount(getSendCount(document));
        job51Data.setFindICount(getWhoLookingAtMe(cookie));
    }

    /**
     * 我的投递次数
     *
     * @return
     */
    public int getSendCount(Element element) {
        Elements rsl_r1 = element.getElementsByClass("rsl r1");
        Document parse = Jsoup.parse(rsl_r1.html());
        Integer result = Integer.valueOf(parse.getElementsByClass("c_orange").text());
        return result;
    }

    /**
     * 查询谁看过我
     *
     * @return
     */
    private int getWhoLookingAtMe(String cookie) {
        int result = 0;
        Document document = HttpClientUtil.doGet(
                accessUrl.get("whoviewme"),
                headerConfig.getConmmonHeader(),
                null,
                cookie);
        JSONObject parseObject = JSON.parseObject(document.body().html());
        if ("success".equals(parseObject.getString("msg"))) {
            Document parse = Jsoup.parse(parseObject.getString("content"));
            Elements e_tit = parse.getElementsByClass("e_tit");
            String html = e_tit.html();
            String tempFirstText = "被查看 <span class=\"c_orange\">";
            String tempLastText = "\"</span>\"";
            result = Integer.valueOf(StringUtil.subHtmlString(html, tempFirstText, tempLastText));
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * 获取用户名字
     *
     * @param element
     * @return
     */
    public String getUserName(Element element) {
        return element.getElementsByClass("uname e_icon at").html();
    }

    /**
     * 按条件查询数据
     *
     * @param searchName
     * @param startPage
     * @param endPage
     */
    public void collectBySearchResultName(String searchName, Integer startPage, Integer endPage, String cookie) {
        Document document = null;
        String searchUrl = null;
        for (Integer i = 0; i < endPage; i++) {
            searchUrl = String.format(accessUrl.get("search"), searchName, i + startPage);
            document = HttpClientUtil.doGet(searchUrl, headerConfig.getConmmonHeader(), null, cookie);
            Elements unitIdEl = document.getElementsByAttributeValue("name", "delivery_jobid");
            Elements unitNameEl = document.getElementsByClass("t2");
            unitNameEl.remove(0);
            Document parse = null;
            String key = null, value = null;
            for (int i1 = 0; i1 < unitIdEl.size(); i1++) {
                parse = Jsoup.parse(unitNameEl.get(i1).html());
                key = unitIdEl.get(i1).val();
                value = parse.getElementsByTag("a").html();
                System.out.println(String.format("公司ID[%s]，公司名称[%s]", key, value));
                unitInfo.put(unitIdEl.get(i1).val(), parse.getElementsByTag("a").html());
            }
            System.out.println(String.format("===============当前[%s]页,有[%s]条数据=================", (i + 1), unitIdEl.size()));
        }
    }

    /**
     * 过滤无效的公司
     *
     * @param cookie
     */
    public void filterUnitInfo(String cookie) {
        String detail = null;
        Element element = null;
        String html = null;
        String jobId = null;
        String unitName = null;
        Set<String> stringSet = unitInfo.keySet();
        System.out.println(String.format("总共[%s]个数据", stringSet.size()));
        Iterator<String> iterator = stringSet.iterator();
        try {
            // int  sss = 0;
            while (iterator.hasNext()) {
                jobId = iterator.next();
                detail = String.format(accessUrl.get("detail"), jobId);
                element = HttpClientUtil.doGet(
                        detail,
                        headerConfig.getConmmonHeader(),
                        null,
                        cookie
                ).body();
                element.getElementsByClass("rjlist r3").remove();//删除推荐岗位列表
                element.getElementsByClass("tmsg inbox").remove();//删除公司简介
                html = element.html().trim();
                Map<String, Object> notEmptySearchResult = isNotEmptySearchResult(html);
                boolean checkResult = (boolean) notEmptySearchResult.get("status");
                if (checkResult) {
                    unitName = unitInfo.get(jobId);
                    iterator.remove();
                    //检查结果[%s],已过滤ID[%s],过滤公司的名称[%s],过滤原因[%s],剩余可用公司个数[%s]
                    String format = String.format("检查结果:::[%s],已过滤ID:::[%s]:::,过滤公司的名称:::[%s]:::,过滤原因:::[%s]:::,剩余可用公司个数:::[%s]",
                            checkResult,
                            jobId,
                            unitName,
                            String.valueOf(notEmptySearchResult.get("why")),
                            unitInfo.size()
                    );
                    System.out.println(format);
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 投简历操作
     */
    public void submitData(String cookie) {// map.put("jsoncallback", "jQuery183005098957260373327_1588261608907");
        System.out.println("正在分批提交");
        System.out.println("=====================");
        String submit = null;
        for (int i = 0; i < urlList.size(); i++) {
            System.out.println(String.format("剩余提交个数【%s】", urlList.size() - i));
            submit = accessUrl.get("submit") + urlList.get(i);
            String body = null;
            try {
                body = HttpClientUtil.doGet(
                        submit,
                        headerConfig.getSubmitHeader(),
                        null,
                        cookie
                ).body().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject parseJSONP = JSON.parseObject(new JSONPObject(body).toJSONString());
            String string = parseJSONP.getJSONObject("content").getJSONObject("content").getString("html");
            Document parse = Jsoup.parse(string);
            String center = parse.getElementsByClass("stage s2 center").html();
            center = StringUtil.replaceSubmitResult(center);
            System.out.println(center);
            SystemUtil.sleep(3000);
        }
        System.out.println("完成分批提交");
        SystemUtil.exitSystem();
    }

    /**
     * 创建提交链接集合
     */
    public void insertUrlList() {
        Set<String> hashSet = new HashSet<>();
        int length = unitInfo.size();
        int num = 0;
        int i = 0;
        Iterator<String> iterator = unitInfo.keySet().iterator();
        while (iterator.hasNext()) {
            i++;
            hashSet.add(iterator.next());
            if (i % 30 == 0) {
                num = num + i;
                saveUrl(hashSet);
                hashSet.clear();
            }
            if (length == i) {
                saveUrl(hashSet);
            }
        }
    }

    /**
     * 检查是否有非法的词汇
     *
     * @param result
     * @return
     */
    private HashMap<String, Object> isNotEmptySearchResult(String result) {
        HashMap<String, Object> map = new HashMap<>();
        result = result
                .replace("\t", "")
                .replace("\n", "")
                .replace(" ", "");
        Iterator<String> iterator = unitInfo.keySet().iterator();
        String key = null;
        for (String itme : disableList) {
            if (result.contains(itme)) {
                map.put("status", true);
                map.put("why", itme);
                return map;
            }
        }
        map.put("status", false);
        return map;
    }
}
