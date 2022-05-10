package com.lxl.yygh.common.handel;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author LiXiaoLong
 * @Date 2022/4/28 15:42
 * @PackageName:com.lxl.yygh.common.handel
 * @ClassName: MyMetaObjectHandler
 * @Description: TODO
 * @Version 1.0
 */
@Component
public class MyMetaObjectHandler  implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        this.setFieldValByName("creat_time",new Date(),metaObject);
        this.setFieldValByName("update_time",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("update_time",new Date(),metaObject);
    }
}
