package com.fuyin.boot.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.fuyin.boot.mgb.entity.RoleDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @ClassName UserAndRoles
 * @Description 用户管理模块：用户+用户角色
 * @Author 何义祈安
 * @Date 2022/9/15 11:12
 * @Version 1.0
 */
@Data
@ToString
public class UserAndRoles {
    /**
     * 用户id
     **/
    private Long id;

    /**
     * 用户
     **/
    private String username;

    private String password;
    /**
     * 用户昵称
     **/
    private String nickname;
    /**
     * 用户手机号
     **/
    private String phoneNumber;

    private String email;

    private String status;

    private String lastIp;

    private Date lastLoginTime;

    /**
     * 用户创建时间
     **/
    private Date gmtCreate;

    /**
     * 用户更新时间
     **/
    private Date gmtModified;

    private Integer version;

    private Integer delFlag;

    /**
    *@Description 持有RoleDO多个集合
    **/
    private List<RoleDO> roleDOList;

    public UserAndRoles() {
    }

    public UserAndRoles(Long id, String username, String password, String nickname, String phoneNumber, String email, String status, String lastIp, Date lastLoginTime, Date gmtCreate, Date gmtModified, Integer version, Integer delFlag, List<RoleDO> roleDOList) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.status = status;
        this.lastIp = lastIp;
        this.lastLoginTime = lastLoginTime;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.version = version;
        this.delFlag = delFlag;
        this.roleDOList = roleDOList;
    }

}
