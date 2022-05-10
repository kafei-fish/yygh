package com.lxl.yygh.hosp.controller;

import com.lxl.yygh.common.R;
import com.lxl.yygh.hosp.service.HospitalService;
import com.lxl.yygh.model.hosp.Hospital;
import com.lxl.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.omg.CORBA.StringHolder;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/8 17:35
 * @PackageName:com.lxl.yygh.hosp.controller
 * @ClassName: HospitalController
 * @Description: TODO
 * @Version 1.0
 */
@Api("医院接口")
@RestController
@CrossOrigin
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {
    @Resource
    private HospitalService hospitalService;
    @ApiOperation(value = "分页查询")
    @GetMapping("{page}/{limit}")
    public R indexPage(@PathVariable("page") Integer page, @PathVariable("limit") Integer limit,@RequestBody(required = false) HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospitals = hospitalService.selectPage(page, limit, hospitalQueryVo);
        return R.ok().data("pages",hospitals);
    }
    @ApiOperation(value = "医院上下线")
    @GetMapping("updateStatus/{id}/{status}")
    public R updateStatus(@PathVariable("id") String id,@PathVariable("status") Integer status){
        hospitalService.updateStatus(id,status);
        return R.ok();
    }
    @ApiOperation(value = "医院详情")
    @GetMapping("show/{id}")
    public R showHospDetails(@PathVariable("id") String id){
        Map<String,Object> map=hospitalService.showHospDetails(id);
        return R.ok().data("hospital",map);
    }
}
