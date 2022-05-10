package com.lxl.yygh.hosp.controller;

import com.lxl.yygh.common.R;
import com.lxl.yygh.hosp.service.DictService;
import com.lxl.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/2 15:06
 * @PackageName:com.lxl.yygh.hosp.controller
 * @ClassName: DictController
 * @Description: TODO
 * @Version 1.0
 */
@Api("数据字典接口")
@RestController
@CrossOrigin
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 通过节点id查询出子节点信息，并且进行判断字节点下是否还有节点。
     * @param id
     * @return
     */
    @GetMapping("findChildData/{id}")
    public R findChildData(@PathVariable("id") Long id){
        List<Dict> chlidData = dictService.findChlidData(id);
        return R.ok().data("list",chlidData);
    }
    @ApiOperation(value="导出")
    @GetMapping(value = "/exportData")
    public void exportTemplate(HttpServletResponse response){
        dictService.exportData(response);
    }
    @ApiOperation(value = "导入")
    @PostMapping("importData")
    public R importData(MultipartFile file){
        dictService.importDictData(file);
        return R.ok();
    }


    @ApiOperation(value = "获取数据字典名称")
    @GetMapping("/getName/{parentDictCode}/{value}")
    public String getName(@PathVariable("parentDictCode") String parentDictCode,
                          @PathVariable("value") String value){
        return dictService.getNameByParentDictCodeAndValue(parentDictCode,value);
    }
    @ApiOperation(value = "获取数据字典名称")
    @GetMapping("/getName/{value}")
    public String getName(
                          @PathVariable("value") String value){
        return dictService.getNameByParentDictCodeAndValue("",value);
    }

    /**
     * 根据dictCode获取下级节点，这个直接获取DictCode获取到DICT的CODE的节点
     * @return
     */
    @GetMapping("findByDictCode/{dictCode}")
    public R findByDictCode(@PathVariable("dictCode") String dictCode){
        List<Dict> list=dictService.findDictCodeList(dictCode);
        return R.ok().data("list",list);
    }
}
