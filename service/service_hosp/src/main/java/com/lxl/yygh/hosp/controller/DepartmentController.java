package com.lxl.yygh.hosp.controller;

import com.lxl.yygh.common.R;
import com.lxl.yygh.hosp.service.DepartmentSrvice;
import com.lxl.yygh.vo.hosp.DepartmentVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/9 15:03
 * @PackageName:com.lxl.yygh.hosp.controller
 * @ClassName: DepartmentController
 * @Description: TODO
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/hosp/department")
@CrossOrigin
public class DepartmentController {
    @Resource
    private DepartmentSrvice departmentSrvice;
    @GetMapping("getDeptList/{hoscode}")
    public R getDeptList(@PathVariable("hoscode") String hoscode){
        List<DepartmentVo> deptTree = departmentSrvice.findDeptTree(hoscode);
        return R.ok().data("list",deptTree);
    }
}
