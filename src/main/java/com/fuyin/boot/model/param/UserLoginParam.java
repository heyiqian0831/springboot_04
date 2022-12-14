package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName UserLoginParam
 * @Description 用户登录手机号和密码
 * @Author 何义祈安
 * @Date 2022/9/19 13:43
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserLoginParam {

    private String phoneNumber;

    private String password;
}
