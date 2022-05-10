package com.lxl;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/5 17:39
 * @PackageName:com.lxl
 * @ClassName: Stu
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class Stu {

    //设置表头名称
    @ExcelProperty("学生编号")
    private int sno;

    //设置表头名称
    @ExcelProperty("学生姓名")
    private String sname;
}
