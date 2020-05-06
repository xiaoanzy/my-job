package com.jhxaa.job51;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jhxaa.util.FileUtil;
import com.jhxaa.util.NetUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Job51Executor {

    private static Map<String, String> urlMap = new HashMap<>();
    private static Queue<String> job51IdQueue = new LinkedBlockingQueue<>();
    private static List<String> disableList = FileUtil.readFileByLines("./config/filter.config");

    static {
        init();
    }

    /**
     * 设置url链接
     */
    private static void setUrlData() {
        if (urlMap.isEmpty()) {
            urlMap.put("login", "https://login.51job.com/");
            urlMap.put("whoviewme", "https://i.51job.com/userset/ajax/whoviewme.php");
            urlMap.put("search", "https://search.51job.com/list/040000,000000,0000,00,9,99,%s,2,%s.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0,0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=");
            urlMap.put("detail", "https://jobs.51job.com/shenzhen/%s.html?s=01&t=0");
            urlMap.put("submit", "https://i.51job.com/delivery/delivery.php");
            urlMap.put("", "");
        }
    }

    /**
     * 初始化
     */
    private static void init() {
        setUrlData();
    }

    public static void main(String[] args) throws IOException {
        long s, e;
        s = System.currentTimeMillis();
        String userName = "czjhxa@163.com", passWord = "AaLl!@1076418191";
        Connection.Response response = doLogin(userName, passWord);
        Document parse = Jsoup.parse(response.body());
        Job51Data.setCookies(response.cookies());//设置用户状态
        System.out.println(response.cookies());
        Job51Data.setFindICount(getWhoLookingAtMe());//设置谁看过我
        Job51Data.setUserName(parse.getElementsByClass("uname e_icon at").text());//设置用户名称
        Job51Data.setSendCount(getSendCount(parse));
        String searchName = "java";
        collectBySearchResultName(searchName, 1, 3);
        System.out.println(job51IdQueue.size());
        filterSearchList();
        System.out.println(job51IdQueue.size());
        submitData();
        e = System.currentTimeMillis();
        System.out.println("耗时间：" + (e - s));
    }

    private static String getParams() {
        String jobId = getJobId(job51IdQueue);
        String text = "?rand=" + Math.random() + "&jsoncallback=jQuery18300098957260373327_" + System.currentTimeMillis() + "&jobid=" + getJobId(job51IdQueue) + "&prd=search.51job.com&prp=01&cd=search.51job.com&cp=01&resumeid=&cvlan=&coverid=&qpostset=&elementname=delivery_jobid&deliverytype=2&deliverydomain=%2F%2Fi.51job.com&language=c&imgpath=%2F%2Fimg03.51jobcdn.com&_=" + System.currentTimeMillis();
        System.out.println(text);
        return text;
    }


    /**
     * 设置提交数据请求头
     *
     * @return
     */
    private static Map getSubmitHeader() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("Host", "login.51job.com");
//        hashMap.put("Referer", "https://login.51job.com");
        hashMap.put("Host", "i.51job.com");
        hashMap.put("Connection", "keep-alive");
        hashMap.put("Accept-Language", "zh-CN,zh;q=0.9");
        hashMap.put("Accept-Encoding", "gzip, deflate, br");
        hashMap.put("Sec-Fetch-Site", "same-site");
        hashMap.put("Sec-Fetch-Mode", "no-cors");
        hashMap.put("Sec-Fetch-Dest", "script");
        hashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.9 Safari/537.36");
        hashMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9;charset=utf-8");
        return hashMap;
    }

    /**
     * 投简历操作
     */
    private static void submitData() {// map.put("jsoncallback", "jQuery183005098957260373327_1588261608907");
        String submit = urlMap.get("submit") + getParams();
        Document document = null;
        String body = null;
        try {
            body = NetUtil.doGet(submit);
            body = Jsoup.connect(submit)
                    .cookies(Job51Data.getCookies())
                    .headers(getSubmitHeader())
//                    .data(getTestData())
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject parseJSONP = parseJSONP(body);
        String string = parseJSONP.getJSONObject("content").getJSONObject("content").getString("html");
        Document parse = Jsoup.parse(string);
        String center = parse.getElementsByClass("stage s2 center").html()
                .replace("<span>", "")
                .replace(" ", "")
                .replace("</span>", "").trim();
        System.out.println(center);
    }

    /**
     * 过滤无效的id
     */
    private static void filterSearchList() {
        String detail = null;
        Element element = null;
        String html = null;
        Iterator<String> iterator = job51IdQueue.iterator();
        try {
            // int  sss = 0;
            while (iterator.hasNext()) {
                detail = String.format(urlMap.get("detail"), iterator.next());
                element = Jsoup.connect(detail)
                        .headers(getHeader())
                        .cookies(getCookies())
                        .ignoreContentType(true)
                        .get().body();
                element.getElementsByClass("rjlist r3").remove();//删除推荐岗位列表
                html = element.html().trim();
                if (isNotEmptySearchResult(html) == true) {
                    iterator.remove();
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否有非法的词汇
     *
     * @param result
     * @return
     */
    private static boolean isNotEmptySearchResult(String result) {
        result
                .replace("\t", "")
                .replace("\n", "");
//                .replace(" ", "");
        for (String itme : disableList) {
            if (result.contains(itme)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 按条件查询数据
     *
     * @param searchName
     * @param startPage
     * @param endPage
     */
    public static void collectBySearchResultName(String searchName, Integer startPage, Integer endPage) {
        Document document = null;
        for (Integer i = 0; i < endPage; i++) {
            String search = String.format(urlMap.get("search"), searchName, startPage);
            try {
                document = Jsoup.connect(search)
                        .ignoreContentType(true)
                        .cookies(Job51Data.getCookies())
                        .headers(getHeader()).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements attribute = document.getElementsByAttributeValue("name", "delivery_jobid");
            for (int i1 = 0; i1 < attribute.size(); i1++) {
                String val = attribute.get(i1).val();
                job51IdQueue.add(val);
            }
        }
    }

    /**
     * 登录
     *
     * @param userName
     * @param passWord
     * @return
     */
    public static Connection.Response doLogin(String userName, String passWord) {
        String loginUrl = urlMap.get("login");
        Connection.Response response = null;
        try {
            response = Jsoup.
                    connect(loginUrl)
                    .method(Connection.Method.POST)
                    .headers(getHeader())
                    .cookies(getCookies())
                    .data(getLoginData(userName, passWord))
                    .ignoreContentType(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.charset("gbk");
        return response;
    }

    /**
     * 获取我投的简历次数
     *
     * @param document
     * @return
     */
    public static Integer getSendCount(Document document) {
        Elements rsl_r1 = document.getElementsByClass("rsl r1");
        Document parse = Jsoup.parse(rsl_r1.html());
        Integer result = Integer.valueOf(parse.getElementsByClass("c_orange").text());
        return result;
    }

    /**
     * jsonp格式转换json格式
     *
     * @param jsonp
     * @return
     */
    public static JSONObject parseJSONP(String jsonp) {
        int startIndex = jsonp.indexOf("(");
        int endIndex = jsonp.lastIndexOf(")");
        String json = jsonp.substring(startIndex + 1, endIndex);
        return JSON.parseObject(json);
    }


    /**
     * 查询谁看过我
     *
     * @return
     */
    private static Integer getWhoLookingAtMe() {
        Connection.Response response1 = null;
        Integer result = 0;
        try {
            response1 = Jsoup.
                    connect(urlMap.get("whoviewme"))
                    .method(Connection.Method.POST)
                    .headers(getHeader())
                    .cookies(Job51Data.getCookies())
                    .ignoreContentType(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject parseObject = JSON.parseObject(response1.body());
        if ("success".equals(parseObject.getString("msg"))) {
            Document parse = Jsoup.parse(parseObject.getString("content"));
            Elements e_tit = parse.getElementsByClass("e_tit");
            String html = e_tit.html();
            String tempFirstText = "被查看 <span class=\"c_orange\">";
            int startIndex = html.indexOf(tempFirstText) + tempFirstText.length();
            int lastIndex = html.indexOf("</span>");
            result = Integer.valueOf(html.substring(startIndex, lastIndex));
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * 设置登录数据
     *
     * @param userName
     * @param passWord
     * @return
     */
    public static Map getLoginData(String userName, String passWord) {
        HashMap<String, String> stringHashMap = new HashMap<String, String>();
        stringHashMap.put("lang", "c");
        stringHashMap.put("action", "save");
        stringHashMap.put("from_domain", "i");
        stringHashMap.put("loginname", userName);
        stringHashMap.put("password", passWord);
        stringHashMap.put("verifycode", "");
        stringHashMap.put("isread", "on");
        return stringHashMap;
    }

    public static Map getCookies() {
        HashMap hashMap = new HashMap();
        hashMap.put("guid", "f57293cf71fbdd5cc830d699597962a4");
        hashMap.put("slife", "lowbrowser%3Dnot%26%7C%26");
        return hashMap;
    }


    /**
     * 拼接jobID
     *
     * @param queue
     * @return
     */
    private static String getJobId(Queue<String> queue) {
        String res = null;
        int length = queue.size();
        int i = 1;
        StringBuilder builder = new StringBuilder();
        for (String s : queue) {
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

    private static Map getTestData() {
        String text = "?rand=" + Math.random() + "&jsoncallback=jQuery18300098957260373327_" + System.currentTimeMillis() + "&jobid=(121803263%3A0)&prd=search.51job.com&prp=01&cd=search.51job.com&cp=01&resumeid=&cvlan=&coverid=&qpostset=&elementname=delivery_jobid&deliverytype=2&deliverydomain=%2F%2Fi.51job.com&language=c&imgpath=%2F%2Fimg03.51jobcdn.com&_=" + System.currentTimeMillis();
        System.out.println(text);
        text = text.substring(1, text.length() - 1);
        String[] split = text.split("&");
        HashMap<String, String> map = new HashMap<>();
        for (String s : split) {
            String[] strings = s.split("=");
            if (strings.length == 2) {
                map.put(strings[0], strings[1]);
            }
            if (strings.length == 1) {
                map.put(strings[0], "");
            }
        }
        return map;
    }

    /**
     * 投简历用的数据
     *
     * @return
     */
    public static Map getSubmitData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("rand", String.valueOf(Math.random()));
        map.put("jsoncallback", "jQuery183005098957260373327_1588261608907");
        map.put("jobid", getJobId(job51IdQueue));
        map.put("prd", "search.51job.com");
        map.put("prp", "01");
        map.put("cd", "search.51job.com");
        map.put("cp", "01");
        map.put("resumeid", "");
        map.put("cvlan", "");
        map.put("coverid", "");
        map.put("qpostset", "");
        map.put("elementname", "delivery_jobid");
        map.put("deliverytype", "2");
        map.put("deliverydomain", "%2F%2Fi.51job.com");
        map.put("language", "c");
        map.put("imgpath", "%2F%2Fimg03.51jobcdn.com");
        map.put("_", String.valueOf(System.currentTimeMillis()));
//        map.put("", "");
//        map.put("", "");
//        map.put("", "");
        return map;
    }

    private static Map getsas() {
        HashMap<String, String> map = new HashMap<>();
        map.put("jobid", getJobId(job51IdQueue));
        map.put("prd", "search.51job.com");
        map.put("prp", "01");
        map.put("cd", "search.51job.com");
        map.put("cp", "01");
        map.put("resumeid", "");
        map.put("cvlan", "");
        map.put("coverid", "");
        map.put("qpostset", "");
        map.put("elementname", "delivery_jobid");
        map.put("deliverytype", "2");
        map.put("deliverydomain", "%2F%2Fi.51job.com");
        map.put("language", "c");
        map.put("imgpath", "%2F%2Fimg03.51jobcdn.com");
        map.put("_", String.valueOf(System.currentTimeMillis()));
        return map;
    }

    /**
     * 设置请求头
     *
     * @return
     */
    public static Map getHeader() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("Host", "login.51job.com");
//        hashMap.put("Referer", "https://login.51job.com");
        hashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.9 Safari/537.36");
        hashMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9;charset=utf-8");
        return hashMap;
    }

    static class Job51Data {

        private static String userName;

        private static Integer findICount;
        private static Integer sendCount;
        private static Map<String, String> cookies;

        public static Integer getSendCount() {
            return sendCount;
        }

        public static void setSendCount(Integer sendCount) {
            Job51Data.sendCount = sendCount;
        }

        public static Integer getFindICount() {
            return findICount;
        }

        public static void setFindICount(Integer findICount) {
            Job51Data.findICount = findICount;
        }

        public static Map<String, String> getCookies() {
            return cookies;
        }

        public static void setCookies(Map<String, String> cookies) {
            Job51Data.cookies = cookies;
        }

        public static String getUserName() {
            return userName;
        }

        public static void setUserName(String userName) {
            Job51Data.userName = userName;
        }
    }
}
