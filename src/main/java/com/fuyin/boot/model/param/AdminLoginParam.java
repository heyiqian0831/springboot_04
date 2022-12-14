package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName AdminLoginParam
 * @Description TODO
 * @Author 何义祈安
 * @Date 2022/9/14 21:05
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminLoginParam {

    private String phoneNumber;

    private String password;
}
