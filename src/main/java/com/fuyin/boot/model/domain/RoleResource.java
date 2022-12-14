package com.fuyin.boot.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName: RoleResource
 * @Author: 何义祈安
 * @Description: 角色和其权限数据表
 * @Date: 2022/11/19 22:32
 * @Version: 1.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoleResource {
    /**
     * 角色逻辑标识
     **/
    private int roleDelFlag;
    /**
     * 权限标识
     **/
    private Long resourceId;
    /**
     * 权限描述
     **/
    private String resourceDesc;
}
