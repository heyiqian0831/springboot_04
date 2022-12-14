package com.fuyin.boot.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 何义祈安 分页查询配置
 */
@Configuration
public class PageConfig {

    /**
     * 3.4.0之前的版本
     * @return
     */
   /* @Bean
    public PaginationInterceptor paginationInterceptor(){
        return  new PaginationInterceptor();
    }*/

    /**
     * 3.4.0之后版本 拦截器配置
     * @return
     */
   @Bean
   public MybatisPlusInterceptor mybatisPlusInterceptor(){
       MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
       //配置分页对应插件 PaginationInnerInterceptor
       mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
       //配置乐观锁对应插件 OptimisticLockerInnerInterceptor
       mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
       return mybatisPlusInterceptor;
   }

//    /**
//     * 3.4.0之后版本 配置乐观锁对应插件 OptimisticLockerInnerInterceptor
//     * @return
//     */
//    @Bean
//    public MybatisPlusInterceptor OptimisticLockerInterceptor(){
//        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
//        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
//        return mybatisPlusInterceptor;
//    }
}
