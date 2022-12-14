package com.fuyin.boot.model.param;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName: ListSystemLog4Page
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/22 22:59
 * @Version: 1.0
 */
@Data
@ToString
public class ListLog4Page {
    /**
    *@Description 页码
    **/
    private Integer pageNum;
    /**
     *@Description 每页数量
     **/
    private Integer pageSize;
}
