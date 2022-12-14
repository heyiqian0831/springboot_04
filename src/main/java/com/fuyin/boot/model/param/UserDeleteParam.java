package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName UserDelete
 * @Description 管理员删除用户接收前端数据模型
 * @Author 何义祈安
 * @Date 2022/9/19 15:51
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDeleteParam {
    private Long id;

    private String username;

    private String nickname;

    private String phoneNumber;

}
