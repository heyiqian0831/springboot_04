package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName: UserSelectPage
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/20 14:49
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserSelectPage {
    /**
    *@Description 查询页码
    **/
    private int current;

    /**
     *@Description 条数
     **/
    private int size;
}
