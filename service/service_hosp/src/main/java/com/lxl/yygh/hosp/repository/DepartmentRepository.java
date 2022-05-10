package com.lxl.yygh.hosp.repository;

import com.lxl.yygh.model.hosp.Department;
import com.lxl.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/6 18:09
 * @PackageName:com.lxl.yygh.hosp.repository
 * @ClassName: DepartmentRepository
 * @Description: TODO
 * @Version 1.0
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    /**
     * to ByHoscode
     * @param hoscode hoscode
     * @param depcode depcode
     * @return Department
     */
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);

    /**
     * to getAllByHoscode
     * @param hoscode hoscode
     * @return
     */
    List<Department> getAllByHoscode(String hoscode);
}
