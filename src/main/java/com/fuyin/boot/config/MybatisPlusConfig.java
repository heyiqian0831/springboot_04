package com.fuyin.boot.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @ClassName MybatisPlusConfig
 * @Description TODO
 * @Author 何义祈安
 * @Date 2022/9/12 16:26
 * @Version 1.0
 */
@Component
public class MybatisPlusConfig {

    /**
     * 3.4.0之后版本 分页查询配置
     * @return
     */
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor(){
//        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
//
//        //配置分页对应插件 PaginationInnerInterceptor
//        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
//        //配置乐观锁对应插件 OptimisticLockerInnerInterceptor
//        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
//
//        return mybatisPlusInterceptor;
//    }

}
