package com.lxl.yygh.hosp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxl.yygh.hosp.repository.DepartmentRepository;
import com.lxl.yygh.hosp.service.DepartmentSrvice;
import com.lxl.yygh.model.hosp.Department;
import com.lxl.yygh.vo.hosp.DepartmentQueryVo;
import com.lxl.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/6 18:11
 * @PackageName:com.lxl.yygh.hosp.service.Impl
 * @ClassName: DepartmentSrviceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
public class DepartmentSrviceImpl implements DepartmentSrvice {
    @Resource
    private DepartmentRepository departmentRepository;

    @Override
    public void saveDepartment(Map<String, Object> map) {
        Department department = JSONObject.parseObject(JSON.toJSONString(map), Department.class);
        //Judgment department isExit
        String hoscode = department.getHoscode();
        Department departmentExist =
                departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,department.getDepcode());
        if(departmentExist!=null){

            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        }else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }

    }

    @Override
    public List<Department> departmentList() {
        return departmentRepository.findAll();
    }

    @Override
    public Page<Department> selectPage(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        //排序规则
        Sort sort=Sort.by(Sort.Direction.DESC, "createTime");
        //创建分页对象
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        Department department=new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);
        //查询条件
        ExampleMatcher matcher =ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Example<Department> example = Example.of(department, matcher);
        //查询全部
        Page<Department> all = departmentRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public void removeDepartment(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department!=null){
            departmentRepository.deleteById(department.getId());
        }

    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //创建list集合用于封装数据
        List<DepartmentVo> result=new ArrayList<>();
        //根据医院编号，查询医院信息
        Department departmentQuery=new Department();
        departmentQuery.setHoscode(hoscode);
        Example<Department> example = Example.of(departmentQuery);
        //查询出来全部的科室信息
        List<Department> departmentList = departmentRepository.findAll(example);
        //使用stream流对科室进行分组
        //String 大科室名
        //List<Department> 对应的科室信息
        Map<String, List<Department>> departmentMap = departmentList
                .stream().collect(Collectors.groupingBy(Department::getBigcode));
        //遍历map集合
        for (Map.Entry<String, List<Department>> entry : departmentMap.entrySet()) {
            //获取大科室编号
            String bigCode = entry.getKey();
            //大科室对应的数据
            List<Department> deparment1List = entry.getValue();
            //封装大科室
            DepartmentVo departmentVo=new DepartmentVo();
            departmentVo.setDepcode(bigCode);
            //这里已经是分好组的，不管你获取多少个，都会是当前对应的大科室信息
            departmentVo.setDepname(deparment1List.get(0).getBigname());
            //封装小科室
            List<DepartmentVo> children=new ArrayList<>();
            for (Department department : deparment1List) {
                DepartmentVo departmentVo1=new DepartmentVo();
                departmentVo1.setDepcode(department.getDepcode());
                departmentVo1.setDepname(department.getDepname());
                children.add(departmentVo1);
            }
            departmentVo.setChildren(children);
            result.add(departmentVo);
        }

        return result;
    }

    @Override
    public String getDeptName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(null != department){
            return department.getDepname();
        }
        return null;
    }


}
