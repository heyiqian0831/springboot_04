package com.fuyin.boot.model.param;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName UserOldNewPassword
 * @Description 用户输入的新密码和旧密码
 * @Author 何义祈安
 * @Date 2022/9/13 21:38
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserOldNewPasswordParam {
    private String oldPassword;

    private String newPassword;

}
