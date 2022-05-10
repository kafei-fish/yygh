package com.lxl.yygh.hosp.service;

import com.lxl.yygh.model.hosp.Department;
import com.lxl.yygh.model.hosp.Schedule;
import com.lxl.yygh.vo.hosp.DepartmentQueryVo;
import com.lxl.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/6 20:31
 * @PackageName:com.lxl.yygh.hosp.service
 * @ClassName: ScheduleService
 * @Description: TODO
 * @Version 1.0
 */
public interface ScheduleService {
    /**
     * savesaveSchedule
     * @param map map
     */
    void savesaveSchedule(Map<String, Object> map);
    /**
     *
     * @param page page
     * @param limit limit
     * @param scheduleQueryVo departmentQueryVo
     * @return pageList
     */
    Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    /**
     * 删除 Department
     * @param hoscode 给医院分配的唯一标识
     * @param hosScheduleId 排班编号（医院自己的排班主键）
     */
    void removeSchedule(String hoscode,String hosScheduleId);

    /**
     * 查询排版规则数据
     * @param page 当前页
     * @param limit 当前记录数
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @return map
     */
    Map<String, Object> getRuleSchedule(Long page, Long limit, String hoscode, String depcode);

    /**
     * 排班详情
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @param workDate 工作时间
     * @return 排班详情
     */
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);
}
