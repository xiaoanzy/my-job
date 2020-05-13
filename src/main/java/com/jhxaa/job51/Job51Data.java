package com.jhxaa.job51;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 安
 * @Date: 2020/05/12/0:02
 * @Description:
 */
public class Job51Data implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 查询次数
     */
    private Integer findICount;

    /**
     * 投递次数
     */
    private Integer sendCount;

    /**
     * 用户cookies
     */
    private Map<String, String> cookies;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getFindICount() {
        return findICount;
    }

    public void setFindICount(Integer findICount) {
        this.findICount = findICount;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
