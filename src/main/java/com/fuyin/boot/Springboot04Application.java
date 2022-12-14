package com.fuyin.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 何义祈安
 */
@SpringBootApplication
@MapperScan(basePackages = "com.fuyin.boot.mgb.mapper")
public class Springboot04Application {

    public static void main(String[] args) {
        SpringApplication.run(Springboot04Application.class, args);
    }

}
