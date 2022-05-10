package com.lxl.yygh.hosp.repository;

import com.lxl.yygh.model.hosp.Hospital;
import com.lxl.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * @Author LiXiaoLong
 * @Date 2022/5/6 20:31
 * @PackageName:com.lxl.yygh.hosp.repository
 * @ClassName: ScheduleRepository
 * @Description: TODO
 * @Version 1.0
 */
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
    /**
     * getScheduleByHoscodeAndHosScheduleId
     * @param hsocode hsocode
     * @return getScheduleByHoscode
     */
    Schedule getScheduleByHoscodeAndHosScheduleId(String hsocode,String hosScheduleId);

    /**
     * 根据医院编号，科室编号，查询排班信息
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @param toDate 工作日期
     * @return 排班信息
     */
    List<Schedule> getScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
