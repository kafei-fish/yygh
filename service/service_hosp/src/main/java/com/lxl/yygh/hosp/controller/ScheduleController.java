package com.lxl.yygh.hosp.controller;

import com.lxl.yygh.common.R;
import com.lxl.yygh.hosp.service.ScheduleService;
import com.lxl.yygh.model.hosp.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/9 13:56
 * @PackageName:com.lxl.yygh.hosp.controller
 * @ClassName: ScheduleController
 * @Description: TODO
 * @Version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    @Resource
    private ScheduleService scheduleService;
    @ApiOperation(value = "查询排版规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public R getScheduleRule(@PathVariable("page") Long page,
                             @PathVariable("limit") Long limit,
                             @PathVariable("hoscode") String hoscode,
                             @PathVariable("depcode") String depcode){
        Map<String,Object> map=scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return R.ok().data(map);
    }
    @ApiOperation(value = "排班详情")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public R getScheduleDetail(@PathVariable("hoscode") String hoscode,
                               @PathVariable("depcode") String depcode,
                               @PathVariable("workDate") String workDate){
        List<Schedule> list=scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return R.ok().data("list",list);
    }
}
