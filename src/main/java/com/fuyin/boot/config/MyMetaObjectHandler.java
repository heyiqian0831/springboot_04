package com.fuyin.boot.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName MetaObjectHandler
 * @Description 自动填充handler
 * @Author 何义祈安
 * @Date 2022/9/12 12:07
 * @Version 1.0
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
//    private Date gmtCreate;
//    private Date gmtModified;
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified",new Date(),metaObject);
//        LocalDateTime.now()
    }
}
