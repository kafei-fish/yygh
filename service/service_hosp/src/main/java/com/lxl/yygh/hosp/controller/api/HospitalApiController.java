package com.lxl.yygh.hosp.controller.api;

import com.lxl.yygh.common.R;
import com.lxl.yygh.common.ResultCode;
import com.lxl.yygh.common.exception.GuliException;
import com.lxl.yygh.common.helper.HttpRequestHelper;
import com.lxl.yygh.common.result.Result;
import com.lxl.yygh.common.result.ResultCodeEnum;
import com.lxl.yygh.common.uitis.MD5;
import com.lxl.yygh.hosp.service.DepartmentSrvice;
import com.lxl.yygh.hosp.service.HospitalService;
import com.lxl.yygh.hosp.service.HospitalSetService;
import com.lxl.yygh.hosp.service.ScheduleService;
import com.lxl.yygh.model.hosp.Department;
import com.lxl.yygh.model.hosp.Hospital;
import com.lxl.yygh.model.hosp.Schedule;
import com.lxl.yygh.vo.hosp.DepartmentQueryVo;
import com.lxl.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/6 14:19
 * @PackageName:com.lxl.yygh.hosp.config
 * @ClassName: HospitalController
 * @Description: TODO
 * @Version 1.0
 */
@Api("医院管理API接口")
@CrossOrigin
@RestController
@RequestMapping("api/hosp")
public class HospitalApiController {
    @Resource
    private HospitalService hospitalService;
    @Resource
    private HospitalSetService hospitalSetService;
    @Resource
    private DepartmentSrvice departmentSrvice;
    @Resource
    private ScheduleService scheduleService;

    @ApiOperation(value = "上传医院")
    @PostMapping("saveHospital")
    public R saveHospital(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());


        //获取医院传递过来的签名
        String sign = (String) map.get("sign");
        String hoscode = (String) map.get("hoscode");
        //获取到hospset里面的签名
        String sginKey = hospitalSetService.sginKey(hoscode);
        String sginKeyMd5 = MD5.encrypt(sginKey);
        if(!sign.equals(sginKeyMd5)){
                throw new GuliException(ResultCode.ERROR,"签名错误");
        }
        //进行比较
        String logoData= (String) map.get("logoData");
        logoData=logoData.replaceAll(" ","+");
        map.put("logoData",logoData);


        hospitalService.saveHosp(map);
        return R.ok();
    }

    /**
     * 查询医院接口
     * @return R
     *
     */
    @ApiOperation(value = "查询医院")
    @PostMapping("hospital/show")
    public Result showHosp(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //获取医院传递过来的签名
        String sign = (String) map.get("sign");
        String hoscode = (String) map.get("hoscode");
        //获取到hospset里面的签名
        String sginKey = hospitalSetService.sginKey(hoscode);
        String sginKeyMd5 = MD5.encrypt(sginKey);
        if(StringUtils.isEmpty(hoscode)){
            throw new GuliException(ResultCode.ERROR,"参数不能未空");
        }
        if(!sign.equals(sginKeyMd5)){
            throw new GuliException(ResultCode.ERROR,"签名错误");
        }
        Hospital hospital=hospitalService.showHospByHoscode(hoscode);
        return Result.ok(hospital);
    }
    @ApiOperation(value = "上传")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String) map.get("hoscode");
        //获取医院传递过来的签名
        String sign = (String) map.get("sign");
        //获取到hospset里面的签名
        String sginKey = hospitalSetService.sginKey(hoscode);
        String sginKeyMd5 = MD5.encrypt(sginKey);
        if("".equals(hoscode)){
            throw new GuliException(ResultCode.ERROR,"error");
        }
        if(!sign.equals(sginKeyMd5)){
            throw new GuliException(ResultCode.ERROR,"签名错误");
        }
        departmentSrvice.saveDepartment(map);
        return Result.ok();
    }
    @PostMapping("department/list")
    public Result departmentList(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //非必要
        String hoscode = (String) map.get("hoscode");
        //必要
        String depcode = (String)map.get("depcode");
        int page= StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String) map.get("page"));
        int limit= StringUtils.isEmpty(map.get("limit")) ? 10 : Integer.parseInt((String) map.get("limit"));

        //判空
        if ("".equals(hoscode) || hoscode==null){
            throw new GuliException(ResultCode.ERROR,"error");
        }

        //获取医院传递过来的签名
        String sign = (String) map.get("sign");
        //获取到hospset里面的签名
        String sginKey = hospitalSetService.sginKey(hoscode);
        String sginKeyMd5 = MD5.encrypt(sginKey);
        if(!sign.equals(sginKeyMd5)){
            throw new GuliException(ResultCode.ERROR,"签名错误");
        }
        // Pageable pageable = PageRequest.of(0, 10);
        //create exampleMather
        DepartmentQueryVo departmentQueryVo=new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        departmentQueryVo.setDepcode(depcode);

        Page<Department> pageModel=departmentSrvice.selectPage(page,limit,departmentQueryVo);

        return Result.ok(pageModel);
    }
    @ApiOperation(value = "saveSchedule")
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //验证参数

        //获取医院传递过来的签名
        String sign = (String) map.get("sign");
        //获取到hospset里面的签名
        String hoscode = (String) map.get("hoscode");
        if("".equals(hoscode) || hoscode==null){
            throw new GuliException(ResultCode.ERROR,"error");
        }
        String sginKey = hospitalSetService.sginKey(hoscode);
        String sginKeyMd5 = MD5.encrypt(sginKey);
        if(!sign.equals(sginKeyMd5)){
            throw new GuliException(ResultCode.ERROR,"签名错误");
        }
        //上传
        scheduleService.savesaveSchedule(map);
        return Result.ok();

    }

    @PostMapping("schedule/list")
    public Result scheduleList(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //非必要
        String hoscode = (String) map.get("hoscode");
        //必要
        String depcode = (String)map.get("depcode");
        int page= StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String) map.get("page"));
        int limit= StringUtils.isEmpty(map.get("limit")) ? 10 : Integer.parseInt((String) map.get("limit"));

        //判空
        if ("".equals(hoscode) || hoscode==null){
            throw new GuliException(ResultCode.ERROR,"error");
        }

        //获取医院传递过来的签名
        String sign = (String) map.get("sign");
        //获取到hospset里面的签名
        String sginKey = hospitalSetService.sginKey(hoscode);
        String sginKeyMd5 = MD5.encrypt(sginKey);
        if(!sign.equals(sginKeyMd5)){
            throw new GuliException(ResultCode.ERROR,"签名错误");
        }
        ScheduleQueryVo scheduleQueryVo=new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> schedules = scheduleService.selectPage(page, limit, scheduleQueryVo);
        return Result.ok(schedules);
    }
    @ApiOperation(value = "scheduleRemove")
    @PostMapping("schedule/remove")
    public Result scheduleRemove(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //验证参数

        //获取医院传递过来的签名
        String sign = (String) map.get("sign");
        //获取到hospset里面的签名
        String hoscode = (String) map.get("hoscode");
        String hosScheduleId = (String) map.get("hosScheduleId");
        if("".equals(hoscode) || hoscode==null){
            throw new GuliException(ResultCode.ERROR,"error");
        }
        String sginKey = hospitalSetService.sginKey(hoscode);
        String sginKeyMd5 = MD5.encrypt(sginKey);
        if(!sign.equals(sginKeyMd5)){
            throw new GuliException(ResultCode.ERROR,"签名错误");
        }
        scheduleService.removeSchedule(hoscode,hosScheduleId);
        return Result.ok();
    }
    @ApiOperation(value = "departmentRemove")
    @PostMapping("department/remove")
    public Result departmentRemove(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //验证参数

        //获取医院传递过来的签名
      //  String sign = (String) map.get("sign");
        //获取到hospset里面的签名
        String hoscode = (String) map.get("hoscode");
        String depcode = (String) map.get("depcode");
//        if("".equals(hoscode) || hoscode==null){
//            throw new GuliException(ResultCode.ERROR,"error");
//        }
//        String sginKey = hospitalSetService.sginKey(hoscode);
//        String sginKeyMd5 = MD5.encrypt(sginKey);
//        if(!sign.equals(sginKeyMd5)){
//            throw new GuliException(ResultCode.ERROR,"签名错误");
//        }
        //判断医院的id是否未空
        if("".equals(hoscode) || hoscode==null){
           throw new GuliException(ResultCode.ERROR,"医院id为空");
        }
        if(this.isSginkey(map)){
            throw new GuliException(ResultCode.ERROR,"签名错误");
        }
        //删除
        departmentSrvice.removeDepartment(hoscode,depcode);
        return Result.ok();
    }



    //判断签名是否正确
    public  boolean isSginkey( Map<String, Object> map) throws GuliException{
        String sign = (String) map.get("sign");
        //通过医院的唯一ID查询出密钥
        String hoscode = (String) map.get("hoscode");
        String sginKeyMd5 = MD5.encrypt(hospitalSetService.sginKey(hoscode));
        return sign.equals(sginKeyMd5);
    }
}
