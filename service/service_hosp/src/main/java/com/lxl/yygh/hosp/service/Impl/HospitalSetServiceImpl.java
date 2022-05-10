package com.lxl.yygh.hosp.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxl.yygh.hosp.mapper.HospitalSetMapper;
import com.lxl.yygh.hosp.service.HospitalSetService;
import com.lxl.yygh.model.hosp.HospitalSet;
import com.lxl.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 医院设置
 * @Author LiXiaoLong
 * @Date 2022/4/28 9:56
 * @PackageName:com.lxl.yygh.hosp.service.Impl
 * @ClassName: HospitalSetServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService  {
    @Override
    public Page<HospitalSet> pageList(Page<HospitalSet> pageParam, HospitalSetQueryVo hospitalSetQueryVo) {
        QueryWrapper<HospitalSet> queryWrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(hospitalSetQueryVo.getHosname())){
            queryWrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }
        if(!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode())){
            queryWrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        }
        queryWrapper.orderByAsc("create_time");
        return this.baseMapper.selectPage(pageParam, queryWrapper);

    }

    @Override
    public String sginKey(String hoscod) {
        HospitalSet hospitalSet = this.getOne(new QueryWrapper<HospitalSet>().eq("hoscode", hoscod));

        return hospitalSet.getSignKey();
    }
}
