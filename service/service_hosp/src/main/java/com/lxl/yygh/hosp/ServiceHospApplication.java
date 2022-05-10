package com.lxl.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author LiXiaoLong
 * @Date 2022/4/28 9:54
 * @PackageName:com.lxl.yygh.hosp
 * @ClassName: ServiceHospApplication
 * @Description: TODO
 * @Version 1.0
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.lxl"})

@EnableDiscoveryClient //加入注册中心
@EnableFeignClients(basePackages = {"com.lxl"}) //开启远程调用
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class,args);
    }
}
