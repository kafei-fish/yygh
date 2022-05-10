package com.lxl.yygh.hosp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxl.yygh.cmn.client.DictFeignClient;
import com.lxl.yygh.enums.DictEnum;
import com.lxl.yygh.hosp.repository.HospitalRepository;
import com.lxl.yygh.hosp.service.HospitalService;
import com.lxl.yygh.model.hosp.Hospital;
import com.lxl.yygh.model.hosp.HospitalSet;
import com.lxl.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/6 14:18
 * @PackageName:com.lxl.yygh.hosp.service.Impl
 * @ClassName: HospitalServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    @Resource
    private HospitalRepository hospitalRepository;
    @Resource
    private DictFeignClient dictFeignClient;
    /**
     * 保存医院信息，如果数据库存在就进行更新操作，如果不存在就就行插入操作
     * @param paramMap paramMap 医院信息参数
     */
    @Override
    public void saveHosp(Map<String, Object> paramMap) {
        //首先我们接收到数据之后将JSON数据之后。我们需要将json数据转换为Hosp对象
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Hospital.class);
        //判断医院是否存在
        Hospital targetHospital = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        if(!StringUtils.isEmpty(targetHospital)){
            hospital.setStatus(targetHospital.getStatus());
            hospital.setCreateTime(targetHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else {
            //0 未上线 1以上线
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital showHospByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        //首先进行排序查找
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        //创建分页对象
        Pageable pageable = PageRequest.of(page - 1, limit,sort);
        Page<Hospital> all=null;
            if(null !=hospitalQueryVo){
                //将查询信息copy到Hospital对象中
                Hospital hospital=new Hospital();
                BeanUtils.copyProperties(hospitalQueryVo,hospital);
                //开启模糊查询
                ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnoreCase(true);
                //开启查询
                Example<Hospital> example =Example.of(hospital,exampleMatcher);
                all= hospitalRepository.findAll(example, pageable);
            }
           all=hospitalRepository.findAll(pageable);
            all.getContent().forEach(item->{
                this.packHospital(item);
            });

        return all;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        if(status.intValue() == 1 || status.intValue() == 0){
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setUpdateTime(new Date());
            hospital.setStatus(status);
            hospitalRepository.save(hospital);
        }
        }

    @Override
    public Map<String, Object> showHospDetails(String id) {
        Map<String,Object> map=new HashMap<>();
        Hospital hospital = this.packHospital( hospitalRepository.findById(id).get());
        map.put("hospital",hospital);
        map.put("bookingRule",hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return map;
    }


    /**
     * 封装参数
     * @param hospital
     * @return
     */
    public Hospital packHospital(Hospital hospital){
        //医院等级
        String hostypeString = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(), hospital.getHostype());
        //省份
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        //城市
        String cityString=dictFeignClient.getName(hospital.getCityCode());
        //区号
        String districtString=dictFeignClient.getName(hospital.getDistrictCode());
        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("fullAddress",provinceString+cityString+districtString);
        return hospital;
    }

}
