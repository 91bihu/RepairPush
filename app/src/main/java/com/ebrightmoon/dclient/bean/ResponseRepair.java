package com.ebrightmoon.dclient.bean;

/**
 * 作者：create by  Administrator on 2018/12/11
 * 邮箱：2315813288@qq.com
 */
public class ResponseRepair {


    /**
     * code : 200
     * msg : 授权成功
     * userName : test1102
     * loginStatus : 1
     * agentId : 16973
     * token : 15n7kObQM2DVQ3JfAxQOfHjp2198Z+Sn3l4wXjdr1tFIpMG+VV9yE/AZHmN/+zbX5DYTKgCSrt4rjocb08L5PO05cFmspWdEi2vcyB/QevkOYRFQJQJmAla1Ig9crOEnFZ/6N3CvsSx3KVOQrvX+9wMkA7JluxYaesFc8Nzm29s=
     * RepeatQuote : 1
     * agentName : test1
     * topAgentId : 102
     */

    private int code;
    private String msg;
    private String userName;
    private int loginStatus;
    private int agentId;
    private String token;
    private String RepeatQuote;
    private String agentName;
    private int topAgentId;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRepeatQuote() {
        return RepeatQuote;
    }

    public void setRepeatQuote(String RepeatQuote) {
        this.RepeatQuote = RepeatQuote;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getTopAgentId() {
        return topAgentId;
    }

    public void setTopAgentId(int topAgentId) {
        this.topAgentId = topAgentId;
    }
}
