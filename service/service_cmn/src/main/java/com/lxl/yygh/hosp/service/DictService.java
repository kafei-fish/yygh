package com.lxl.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.lxl.yygh.model.cmn.Dict;
import com.lxl.yygh.model.hosp.HospitalSet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/2 14:25
 * @PackageName:com.lxl.yygh.hosp.service
 * @ClassName: DictService
 * @Description: TODO
 * @Version 1.0
 */
public interface DictService extends IService<Dict> {
    /**
     * 根据数据id查询子数据列表
     */
    List<Dict> findChlidData(Long id);

    /**
     * 导出模板接口
     * @param response res
     */
    void exportData(HttpServletResponse response);

    /**
     * 向数据库导入数据
     * @param file 文件
     */
    void importDictData(MultipartFile file);

    /**
     * 根据上机数据字典编码与值获取数据字典名称
     * @param parentDictCode
     * @param value
     * @return
     */
    String getNameByParentDictCodeAndValue(String parentDictCode,String value);

    /**
     * 根据dictCode获取下级节点数据
     * @param dictCode
     * @return
     */
    List<Dict> findDictCodeList(String dictCode);
}
