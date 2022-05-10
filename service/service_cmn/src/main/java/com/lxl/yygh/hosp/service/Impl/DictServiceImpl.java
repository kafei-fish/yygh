package com.lxl.yygh.hosp.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxl.yygh.hosp.Listener.ExcelListener;
import com.lxl.yygh.hosp.mapper.DictMapper;
import com.lxl.yygh.hosp.service.DictService;
import com.lxl.yygh.model.cmn.Dict;
import com.lxl.yygh.model.hosp.HospitalSet;
import com.lxl.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/2 14:26
 * @PackageName:com.lxl.yygh.hosp.service.Impl
 * @ClassName: DictServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private ExcelListener excelListener;
    @Cacheable(value = "dict",key = "'selectIndexList'+#id")
    @Override
    public List<Dict> findChlidData(Long id) {
        //根据数据ID查询字节点数据
        QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        List<Dict> list = this.list(queryWrapper);
        for (Dict dict : list) {
            Long dictId = dict.getId();
            boolean isChildren = this.isChildren(id);
            dict.setHasChildren(isChildren);
        }
        return list;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            List<Dict> dictsList = baseMapper.selectList(null);
            List<DictEeVo> dictEeVoList=new ArrayList<>(dictsList.size());
            for (Dict dict : dictsList) {
                DictEeVo dictVo = new DictEeVo();
                BeanUtils.copyProperties(dict,dictVo);
                dictEeVoList.add(dictVo);
            }
            EasyExcel.write(response.getOutputStream(),DictEeVo.class).sheet("数据字典").doWrite(dictEeVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,excelListener).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 首先对我们的自定就数据字典查询就要通过DictCode来进行查询
     * @param parentDictCode
     * @param value
     * @return
     */
    @Override
    public String getNameByParentDictCodeAndValue(String parentDictCode, String value) {
        //如果这个父节点地编码为空，就直接查询值就行
        if(StringUtils.isEmpty(parentDictCode)){
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>().eq("value", value));
            if(null != dict){
                return dict.getName();
            }
        }else {
            //根据Dict查询
            Dict dictByDictCode = this.getDictByDictCode(parentDictCode);
            if(null == dictByDictCode) return "";
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>()
                        .eq("parent_id", dictByDictCode.getId())
                        .eq("value", value));
            return dict.getName();

        }
        return "";
    }

    @Override
    public List<Dict> findDictCodeList(String dictCode) {
        //首先通过dict查询出diceCode，然后在根据id查询出全部
        Dict codeDict = this.getDictByDictCode(dictCode);
        if (null == codeDict) return null;

        return this.findChlidData(codeDict.getId());
    }

    /**
     * 判断当前节点是否有子节点
     * @param id
     * @return
     */
    private boolean isChildren(Long id){
        QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        Integer integer = baseMapper.selectCount(queryWrapper);
        return integer>0;
    }

    /**
     * @param  parentDictCode 根据DictCode查询
     * @return Dict
     */
    public Dict getDictByDictCode(String parentDictCode){
        QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dict_code",parentDictCode);
        return baseMapper.selectOne(queryWrapper);
    }
}
