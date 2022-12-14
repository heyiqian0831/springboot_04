package com.fuyin.boot.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: UserAgentInfo
 * @Author: 何义祈安
 * @Description: 访问对象头信息
 * @Date: 2022/11/23 14:44
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAgentInfo {
    private String hardwareType;
    private String os;
    private String browserType;
    private String ip;
    private String method;
}
