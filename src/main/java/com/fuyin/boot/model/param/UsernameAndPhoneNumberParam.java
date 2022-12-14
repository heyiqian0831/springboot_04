package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName NicknameAndPhoneNumber
 * @Description 管理员根据用户名和用户手机查询用户
 * @Author 何义祈安
 * @Date 2022/9/15 20:04
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsernameAndPhoneNumberParam {
    /**
    * 用户昵称
    **/
    private String userName;

    /**
     * 用户手机号
     **/
    private String phoneNumber;
}
