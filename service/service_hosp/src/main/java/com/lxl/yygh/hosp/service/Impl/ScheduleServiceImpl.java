package com.lxl.yygh.hosp.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.lxl.yygh.common.uitis.HttpUtil;
import com.lxl.yygh.hosp.repository.ScheduleRepository;
import com.lxl.yygh.hosp.service.DepartmentSrvice;
import com.lxl.yygh.hosp.service.HospitalService;
import com.lxl.yygh.hosp.service.ScheduleService;
import com.lxl.yygh.model.hosp.Department;
import com.lxl.yygh.model.hosp.Hospital;
import com.lxl.yygh.model.hosp.Schedule;
import com.lxl.yygh.vo.hosp.BookingScheduleRuleVo;
import com.lxl.yygh.vo.hosp.ScheduleOrderVo;
import com.lxl.yygh.vo.hosp.ScheduleQueryVo;
import javafx.concurrent.ScheduledService;
import org.bson.Document;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/6 20:32
 * @PackageName:com.lxl.yygh.hosp.service.Impl
 * @ClassName: ScheduleServiceImpl
 * @Description: TODO
 * @Version 1.0
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Resource
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private HospitalService hospitalService;
    @Resource
    private DepartmentSrvice departmentSrvice;
    @Override
    public void savesaveSchedule(Map<String, Object> map) {
        String josnString = JSONObject.toJSONString(map);
        Schedule schedule = JSONObject.parseObject(josnString, Schedule.class);
        //排版id 与 医院ID
        Schedule scheduleExist = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(),schedule.getHosScheduleId());
        if(scheduleExist!=null){
            scheduleExist.setUpdateTime(new Date());
            scheduleExist.setIsDeleted(0);
            scheduleExist.setStatus(1);
            scheduleRepository.save(scheduleExist);
        }else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        Sort sort=Sort.by(Sort.Direction.DESC, "createTime");
        //创建分页对象
        Pageable pageable = PageRequest.of(page-1, limit, sort);
        Schedule schedule=new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo,schedule);
        schedule.setIsDeleted(0);
        //查询条件
        ExampleMatcher matcher =ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Example<Schedule> example = Example.of(schedule, matcher);
        Page<Schedule> all = scheduleRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public void removeSchedule(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if(schedule!=null){
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    @Override
    public Map<String, Object> getRuleSchedule(Long page, Long limit, String hoscode, String depcode) {

        // 1.查询条件
        Criteria criteria=Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        // 2.条件分组
        Aggregation agg=Aggregation.newAggregation(
                Aggregation.match(criteria),//匹配条件
                Aggregation.group("workDate")
                .first("workDate").as("workDate") //别名
                //统计号源数量
                .count().as("docCount")
                //求和
                .sum("reservedNumber").as("reservedNumber")
                .sum("availableNumber").as("availableNumber"),
                //排序
                Aggregation.sort(Sort.Direction.DESC,"workDate"),
                //实现分页
                Aggregation.skip((page-1)*limit),
                Aggregation.limit(limit)

        );
        //最终执行
        AggregationResults<BookingScheduleRuleVo> aggResults = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggResults.getMappedResults();
        //分页查询总记录数
        Aggregation totalAgg=Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResults = mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggResults.getMappedResults().size();
        //把日期对应星期获取
        for (BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek =HttpUtil.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }
        //设置最终数据并返沪
        Map<String,Object> result=new HashMap<>();
        result.put("bookingScheduleRuleVoList",bookingScheduleRuleVoList);
        result.put("total",total);
        //获取医院名称
        Hospital hospital = hospitalService.showHospByHoscode(hoscode);
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname",hospital.getHosname());
        result.put("baseMap",baseMap);
        return result;
    }

    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
        List<Schedule> scheduleList = scheduleRepository.getScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        //把得到list集合遍历，向设置其他值：医院名称、科室名称、日期对应星期
        scheduleList.forEach(this::packageSchedule);

        return scheduleList;
    }

    private void packageSchedule(Schedule schedule) {
        //设置医院名称
        schedule.getParam().put("hosname",hospitalService.showHospByHoscode(schedule.getHoscode()).getHosname());
        //设置科室名称
        schedule.getParam().put("depname",departmentSrvice.getDeptName(schedule.getHoscode(),schedule.getDepcode()));
        //设置日期对应星期
        schedule.getParam().put("dayOfWeek",HttpUtil.getDayOfWeek(new DateTime(schedule.getWorkDate())));
    }
}
