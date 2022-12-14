package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * @ClassName: InsertUserParam
 * @Author: 何义祈安
 * @Description: 管理员新增用户传入的用户信息
 * @Date: 2022/11/18 15:43
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InsertUserParam {
    /**
     *@Description 用户id唯一标识
     **/
    private Long id;
    /**
    *@Description 用户名
    **/
    private String username;
    /**
     *@Description 用户手机号
     **/
    private String phoneNumber;
    /**
     *@Description 用户权限
     **/
    private String role;
}
