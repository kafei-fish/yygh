package com.lxl.yygh.hosp.controller;

import com.lxl.yygh.common.R;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/1 13:27
 * @PackageName:com.lxl.yygh.hosp.controller
 * @ClassName: LoginController
 * @Description: TODO
 * @Version 1.0
 */
@RestController
@RequestMapping("user")
@CrossOrigin
public class LoginController {
    @GetMapping("login")
    public R login(){
        Map map=new HashMap();
        map.put("token","admin-token");
        return R.ok().code(20000).data(map);
    }
    @GetMapping("info")
    public R info(){
        List<String> list=new ArrayList<>();
        list.add("admin");
        return R.ok().code(20000).data("roles",list).data("introduction","I am a super administrator")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
                .data("name","Super Admin");
    }
}
