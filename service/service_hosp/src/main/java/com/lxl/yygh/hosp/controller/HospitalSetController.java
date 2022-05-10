package com.lxl.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxl.yygh.common.MD5;
import com.lxl.yygh.common.R;
import com.lxl.yygh.hosp.service.HospitalSetService;
import com.lxl.yygh.model.hosp.HospitalSet;
import com.lxl.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * 医院设置控制器
 * @Author LiXiaoLong
 * @Date 2022/4/28 9:57
 * @PackageName:com.lxl.yygh.hosp.controller
 * @ClassName: HospitalSetController
 * @Description: TODO
 * @Version 1.0
 */
@Api("医院接口设置")
@CrossOrigin
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 查询全部
     * @return
     */
    @ApiOperation(value = "医院设置列表")
    @GetMapping("findAll")
    public R findAll(){
        List<HospitalSet> list = hospitalSetService.list();
        return R.ok().data("list",list);

    }
    @ApiOperation(value = "医院设置删除")
    @DeleteMapping("{id}")
    public R removeById(@ApiParam(name = "id",value = "讲师ID",required = true) @PathVariable  String id){
        hospitalSetService.removeById(id);
        return R.ok();
    }
    @ApiOperation(value = "分页医院设置列表")
    @GetMapping("{page}/{limit}")
    public R pageList(@ApiParam(name = "page",value = "当前页面",required = true)
                      @PathVariable Long page,
                      @ApiParam(name = "limit",value = "每页记录数",required = true)
                      @PathVariable Long limit){
        Page<HospitalSet> pageParam=new Page<>(page,limit);
        Page<HospitalSet> pageList = hospitalSetService.page(pageParam, null);
        //页面记录
        List<HospitalSet> records = pageList.getRecords();
        //总记录数
        long total = pageList.getTotal();
        return R.ok().data("rows",records).data("total",total);
    }
    @ApiOperation(value = "条件查询医院设置列表")
    @PostMapping("pageQueryList/{page}/{limit}")
    public R pageListQuery(@ApiParam(name = "page",value = "当前页面",required = true) @PathVariable Long page,
                           @ApiParam(name = "limit",value = "每页记录数",required = true) @PathVariable Long limit,
                           @ApiParam(name = "HospitalQueryVo",value = "查询条件",required = false)
                           @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> pageParam=new Page<>(page,limit);
        Page<HospitalSet> pageList=hospitalSetService.pageList(pageParam,hospitalSetQueryVo);
        List<HospitalSet> records = pageList.getRecords();
        long total = pageList.getTotal();
        return R.ok().data("rows",records).data("total",total);
    }
    @ApiOperation(value = "新增医院设置")
    @PostMapping("save")
    public R saveHospSet(@ApiParam(name = "hospital",required = true) @RequestBody HospitalSet hospitalSet){
        //设置状态
        hospitalSet.setStatus(0);
        //设置签名
        Random random=new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        //进行保存
        boolean save = hospitalSetService.save(hospitalSet);
        return R.ok().data("flag",save);
    }
    @ApiOperation(value = "根据ID查询")
    @GetMapping("getById/{id}")
    public R getById(@ApiParam(name = "id",value = "id",required = true) @PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return R.ok().data("hospitalSet",hospitalSet);
    }
    @ApiOperation(value = "根据ID修改")
    @PostMapping("updateById")
    public R updateById(@ApiParam(name = "hospitalSet",value = "hospitalSet",required = true) @RequestBody HospitalSet hospitalSet){
        System.out.println("hospitalSet = " + hospitalSet);
        boolean b = hospitalSetService.updateById(hospitalSet);
        return R.ok().data("flag",b);

    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("batchDelete")
    public R bathDelete(@RequestBody List<Long> idList){
        boolean b = hospitalSetService.removeByIds(idList);
        if(b) {
            return R.ok();
        }
        return R.error();
    }
    @ApiOperation(value = "锁定与删除接口")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public R lockHospitalSet(@PathVariable("id") Long id,@PathVariable("status") Integer status){
        //根据id查询医院信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        boolean b = hospitalSetService.updateById(hospitalSet);
        if(b){
            return R.ok();
        }else {
            return R.error();
        }
    }
}
