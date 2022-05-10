package com.lxl.yygh.hosp.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author LiXiaoLong
 * @Date 2022/4/28 9:58
 * @PackageName:com.lxl.yygh.hosp.config
 * @ClassName: HospConfig
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
@MapperScan("com.lxl.yygh.hosp.mapper")
@EnableTransactionManagement
public class HospConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
