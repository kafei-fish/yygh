package com.lxl.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/8 19:00
 * @PackageName:com.lxl.yygh.cmn.client
 * @ClassName: DictFiginClient
 * @Description: TODO
 * @Version 1.0
 * @FeignClient 远程调用  service-cmn
 */

@FeignClient("service-cmn")
public interface DictFeignClient {
    /**
     * 获取数据字典名称
     * @param parentDictCode 父节点编码
     * @param value 值
     * @return name
     */
    @GetMapping("/admin/cmn/dict/getName/{parentDictCode}/{value}")
    public String getName(@PathVariable("parentDictCode") String parentDictCode, @PathVariable("value") String value);

    /**
     * 获取数据字典名称
     * @param value 值
     * @return name
     */
    @GetMapping("/admin/cmn/dict/getName/{value}")
    public String getName(@PathVariable("value") String value);
}
