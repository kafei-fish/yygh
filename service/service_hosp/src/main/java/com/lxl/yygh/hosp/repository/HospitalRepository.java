package com.lxl.yygh.hosp.repository;

import com.lxl.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/6 14:15
 * @PackageName:com.lxl.yygh.hosp.repository
 * @ClassName: HospitalRepository
 * @Description: TODO
 * @Version 1.0
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    /**
     * 判断医院信息是否存在
     * @param hoscode 医院的唯一编码
     * @return 返回医院信息
     */
    Hospital getHospitalByHoscode(String hoscode);
}
