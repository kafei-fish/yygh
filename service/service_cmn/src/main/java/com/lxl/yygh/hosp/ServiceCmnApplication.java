package com.lxl.yygh.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/2 14:18
 * @PackageName:com.lxl.yygh.hosp
 * @ClassName: ServiceCmnApplication
 * @Description: TODO
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.lxl.yygh.hosp.mapper")
@ComponentScan(basePackages = {"com.lxl"})
@EnableDiscoveryClient
public class ServiceCmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnApplication.class,args);
    }
}
