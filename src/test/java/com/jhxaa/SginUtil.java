package com.jhxaa;

import com.jhxaa.util.EmptyUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/12/16:33
 * @Description:
 */
public class SginUtil {

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        StringBuilder builder = new StringBuilder(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes("utf-8"));
            for (int i = 0; i < array.length; i++) {
                builder.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    /**
     * 校验签名
     *
     * @param obj
     * @param <T>
     * @throws Exception
     */
    public static <T> void checkRequestSgin(T obj) throws Exception {
        if (EmptyUtil.isEmpty(obj)) {
            return;
        }
        Map<String, Object> map = new TreeMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        if (EmptyUtil.isEmpty(map.get("sign"))) {
            throw new Exception("无效签名");
        }
        String remoteSign = map.get("sign").toString();
        map.remove("sign");
        List<String> keyList = new ArrayList<String>(map.keySet());
        // 将key排序然后拼接字符串
        Collections.sort(keyList);
        StringBuilder rawString = new StringBuilder();
        for (String key : keyList) {
            Object value = map.get(key);
            if (value != null && EmptyUtil.isEmpty(String.valueOf(value))) {
                rawString.append(key).append("=").append(value).append("&");
            }
        }
        rawString.append("key=");
        String md5 = getMD5(rawString.toString());
        if (!md5.equalsIgnoreCase(remoteSign)) {
            throw new Exception("校验签名失败");
        }
    }

    public static void main(String[] args) {
        StringBuilder rawString = new StringBuilder();
        rawString.append("111").append("=").append("222").append("&");
        rawString.append("key=");
        System.out.println(rawString.toString());
    }


}
