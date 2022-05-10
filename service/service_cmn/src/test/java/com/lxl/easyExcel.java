package com.lxl;

import com.alibaba.excel.EasyExcel;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/3 21:38
 * @PackageName:com.lxl
 * @ClassName: easyExcel
 * @Description: TODO
 * @Version 1.0
 */

public class easyExcel {
    public static void main(String[] args) {
        String fileName= "D:\\11.xlsx";
        EasyExcel.write(fileName,Stu.class).sheet("写入方法1").doWrite(data());
    }
    private static List<Stu> data() {
        Stack<Integer> stack=new Stack();
        int c = 1;
        stack.push(c);
        List<Stu> list = new ArrayList<Stu>();
        for (int i = 0; i < 10; i++) {
            Stu data = new Stu();
            data.setSno(i);
            data.setSname("张三"+i);
            list.add(data);
        }
        return list;
    }
}
