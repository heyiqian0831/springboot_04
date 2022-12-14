package com.fuyin.boot.mgb.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * @author 何义祈安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user")
public class UserDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String phoneNumber;

    private String email;

    private String status;

    private String lastIp;

    private Date lastLoginTime;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @Version
    private Integer version;

    private Integer delFlag;

    private static final long serialVersionUID = 1L;


}
