package com.lxl.yygh.hosp.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.processor.AnalysisEventProcessor;
import com.lxl.yygh.hosp.mapper.DictMapper;
import com.lxl.yygh.model.cmn.Dict;
import com.lxl.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/5 22:46
 * @PackageName:com.lxl.yygh.hosp.Listener
 * @ClassName: ExcelListener
 * @Description: TODO
 * @Version 1.0
 */
@Component
public class ExcelListener extends AnalysisEventListener<DictEeVo> {
    @Resource
    private DictMapper dictMapper;

    /**
     * 一行行读取
     * @param dictEeVo dictEeVo
     * @param analysisContext analysisContext
     */
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict=new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
