package com.lxl.yygh.hosp.service;

import com.lxl.yygh.model.hosp.Department;
import com.lxl.yygh.vo.hosp.DepartmentQueryVo;
import com.lxl.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/6 18:10
 * @PackageName:com.lxl.yygh.hosp.service
 * @ClassName: DepartmentSrvice
 * @Description: TODO
 * @Version 1.0
 */
public interface DepartmentSrvice {

    /**
     * save departmengt
     * @param map bumen
     */
    void saveDepartment(Map<String, Object> map);

    /**
     *
     * @return
     */
    List<Department> departmentList();

    /**
     *
     * @param page page
     * @param limit limit
     * @param departmentQueryVo departmentQueryVo
     * @return pageList
     */
    Page<Department> selectPage(int page, int limit, DepartmentQueryVo departmentQueryVo);

    /**
     * 删除 Department
     * @param hoscode 给医院分配的唯一标识
     * @param depcode 科室编号
     */
    void removeDepartment(String hoscode,String depcode);


    /**
     * 全部科室信息
     * @param hoscode 医院编号
     * @return 科室以及子科室
     */
    List<DepartmentVo> findDeptTree(String hoscode);

    String getDeptName(String hoscode, String depcode);
}
