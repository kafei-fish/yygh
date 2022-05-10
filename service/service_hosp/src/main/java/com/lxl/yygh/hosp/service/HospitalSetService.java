package com.lxl.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxl.yygh.model.hosp.HospitalSet;
import com.lxl.yygh.vo.hosp.HospitalSetQueryVo;

/**
 * @Author LiXiaoLong
 * @Date 2022/4/28 9:56
 * @PackageName:com.lxl.yygh.hosp.service.Impl
 * @ClassName: HospitalSetService
 * @Description: TODO
 * @Version 1.0
 */
public interface HospitalSetService extends IService<HospitalSet> {
    Page<HospitalSet> pageList(Page<HospitalSet> pageParam, HospitalSetQueryVo hospitalSetQueryVo);
    String sginKey(String hoscod);

}
