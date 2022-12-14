package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName: DebtSchemeParam
 * @Author: 何义祈安
 * @Description: 债务方案内容管理--确认提交--控制层入参类型，结合三个表
 * @Date: 2022/11/21 21:22
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DebtSchemeParam {
    /**
    *@Description id
    **/
    private Long id;
    /**
     *@Description 实施内容设定
     **/
    private String implementation;
    /**
     *@Description 重组结果预设
     **/
    private String resultPreset;
    /**
    *@Description 债务重组类型
    **/
    private Object type;
}
