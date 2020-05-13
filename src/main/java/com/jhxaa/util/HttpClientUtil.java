package com.jhxaa.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/12/13:06
 * @Description:
 */
public class HttpClientUtil {

    public static Document doGet(String url, Map headerMap, Map dataMap, String cookie) {
        return doRequest(url, Connection.Method.GET, headerMap, dataMap, cookie);
    }

    public static Document doPost(String url, Map headerMap, Map dataMap, String cookie) {
        return doRequest(url, Connection.Method.POST, headerMap, dataMap, cookie);
    }

    public static Document doRequest(String url, Connection.Method method, Map headerMap, Map dataMap, String cookie) {
        Document document = null;
        if (EmptyUtil.isEmpty(dataMap)) {
            dataMap = new HashMap<>();
        }
        try {
            document = Jsoup.connect(url)
                    .headers(headerMap)
                    .data(dataMap)
                    .cookie("Cookie", cookie)
                    .method(method)
                    .execute().parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }
}
