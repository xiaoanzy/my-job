package com.jhxaa.util;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/13/13:04
 * @Description:
 */
public class StringUtil {


    /**
     * 截取HTML指定文字
     *
     * @param htmlStr
     * @param startSubStr
     * @param endSubStr
     * @return
     */
    public static String subHtmlString(String htmlStr, String startSubStr, String endSubStr) {
        int startIndex = htmlStr.indexOf(startSubStr) + startSubStr.length();
        int lastIndex = htmlStr.indexOf(endSubStr);
        return htmlStr.substring(startIndex, lastIndex);
    }

    /**
     * 消除无效字段
     *
     * @param text
     * @return
     */
    public static String replaceSubmitResult(String text) {
        return text.replace("<span>", "")
                .replace("<br>", "")
                .replace(" ", "")
                .replace("\n", "")
                .replace("\t", "")
                .replace(" ", "")
                .replace("</span>", "").trim();
    }


}
