package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName: GetResourceByAdmin
 * @Author: 何义祈安
 * @Description: 管理员在角色管理界面点击编辑 数据库操作是查询该角色权限，参数：id,其他的有需要再加
 * @Date: 2022/11/19 21:54
 * @Version: 1.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetResourceByAdmin {
    /**
    *@Description 角色id
    **/
    private Long id;
}
