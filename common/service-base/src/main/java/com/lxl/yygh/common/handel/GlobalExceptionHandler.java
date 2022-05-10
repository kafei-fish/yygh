package com.lxl.yygh.common.handel;


import com.lxl.yygh.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 * @Author LiXiaoLong
 * @Date 2022/4/28 20:23
 * @PackageName:com.lxl.yygh.common.handel
 * @ClassName: GlobalExceptionHandler
 * @Description: TODO
 * @Version 1.0
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        log.error(e.getMessage());
        return R.error();
    }
}
