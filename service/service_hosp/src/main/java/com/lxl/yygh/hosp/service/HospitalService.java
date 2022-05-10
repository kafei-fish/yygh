package com.lxl.yygh.hosp.service;

import com.lxl.yygh.model.hosp.Hospital;
import com.lxl.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/6 14:18
 * @PackageName:com.lxl.yygh.hosp.service
 * @ClassName: HospitalService
 * @Description: TODO
 * @Version 1.0
 */
public interface HospitalService {
    /**
     * 上传医院信息
     * @param paramMap paramMap
     */
    void saveHosp(Map<String,Object> paramMap);

    /**
     * 查询医院信息
     * @param hoscode
     * @return
     */
    Hospital showHospByHoscode(String hoscode);

    /**
     * 分页查询医院信息
     * @param page 当前页面
     * @param limit 页面记录条数
     * @param hospitalQueryVo 将查询信息封装为查询条件
     * @return  Page<Hospital>
     */
    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    /**
     * 医院上下线
     * @param id 医院id
     * @param status 状态
     */
    void updateStatus(String id, Integer status);

    /**
     * 医院详情
     * @param id 医院ID
     * @return showHospDetails
     */
    Map<String, Object> showHospDetails(String id);
}
