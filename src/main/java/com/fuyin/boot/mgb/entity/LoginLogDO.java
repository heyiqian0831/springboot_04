package com.fuyin.boot.mgb.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("login_log")
public class LoginLogDO implements Serializable {
    /**
     * 日志编号
     **/
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
    * 访问用户
    **/
    private Long userId;
    /**
     * 访问用户
     **/
    private String username;
    /**
     * 登录ip
     **/
    private String ip;
    /**
     * 浏览器
     **/
    private String browser;
    /**
     * 操作系统
     **/
    private String operatingSystem;
    /**
     * 登录状态
     **/
    private String loginStatus;
    /**
     * 登录信息
     **/
    private String loginInformation;
    /**
     * 登录日期
     **/
    private Date loginDate;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getLoginInformation() {
        return loginInformation;
    }

    public void setLoginInformation(String loginInformation) {
        this.loginInformation = loginInformation;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", username=").append(username);
        sb.append(", ip=").append(ip);
        sb.append(", browser=").append(browser);
        sb.append(", operatingSystem=").append(operatingSystem);
        sb.append(", loginStatus=").append(loginStatus);
        sb.append(", loginInformation=").append(loginInformation);
        sb.append(", loginDate=").append(loginDate);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
