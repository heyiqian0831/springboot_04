package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName: InputEnterpriceParam
 * @Author: 何义祈安
 * @Description: 用户点击AI诊断分析传进来的企业基本信息
 * @Date: 2022/11/10 17:57
 * @Version: 1.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InputEnterpriceParam {
    /**
     * 企业名称
    **/
    private String name;
    /**
     * 企业信用代码
    **/
    private String enterpriseCode;
    /**
     * 企业重组原因
     **/
    private String reason;
    /**
     * 企业重组方式（类型）
     **/
    private String type;
}
