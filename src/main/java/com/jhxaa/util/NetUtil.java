package com.jhxaa.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class NetUtil {

    private static String cookies;

    public static String getCookies() {
        return cookies;
    }

    public static void setCookies(Map<String, String> cookiesMap) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : cookiesMap.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
        }
        System.out.println(builder.toString());
        NetUtil.cookies = builder.toString();
    }

    public static String doGet(String urlPath) throws IOException {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        String tempLine = null;
        StringBuilder stringBuilder = null;
        try {
            url = new URL(urlPath);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Cookie", getCookies());
            connection.setDoInput(true);
            httpURLConnection = (HttpURLConnection) connection;
            stringBuilder = new StringBuilder();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (httpURLConnection.getResponseCode() >= 300) {
            try {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                stringBuilder.append(tempLine);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return stringBuilder.toString();
    }
}
