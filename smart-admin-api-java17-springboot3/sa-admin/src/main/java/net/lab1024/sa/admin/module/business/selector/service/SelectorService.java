package net.lab1024.sa.admin.module.business.selector.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.horse.dao.HorseDao;
import net.lab1024.sa.admin.module.business.selector.domain.form.*;
import net.lab1024.sa.admin.module.business.selector.domain.vo.*;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择器服务
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-12-19
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室
 */
@Service
@Slf4j
public class SelectorService {

    @Autowired
    private HorseDao horseDao;

    @Autowired
    private CoachDao coachDao;

    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 马匹选择器查询
     */
    public List<HorseSelectorVO> queryHorseSelector(HorseSelectorQueryForm form) {
        // TODO: 实现马匹查询逻辑
        List<HorseSelectorVO> result = new ArrayList<>();
        
        // 这里先返回模拟数据，后续实现具体查询逻辑
        HorseSelectorVO horse1 = new HorseSelectorVO();
        horse1.setHorseId(1L);
        horse1.setHorseName("黑风");
        horse1.setChipNo("CH001234567890");
        horse1.setDisplayLabel("黑风 (CH001234567890)");
        result.add(horse1);
        
        return result;
    }

    /**
     * 会员选择器查询
     */
    public List<MemberSelectorVO> queryMemberSelector(MemberSelectorQueryForm form) {
        // TODO: 实现会员查询逻辑
        List<MemberSelectorVO> result = new ArrayList<>();
        
        // 这里先返回模拟数据，后续实现具体查询逻辑
        MemberSelectorVO member1 = new MemberSelectorVO();
        member1.setMemberId(1L);
        member1.setActualName("张三");
        member1.setPhone("13888888888");
        member1.setMaskedPhone("138****8888");
        member1.setDisplayLabel("张三 (138****8888)");
        result.add(member1);
        
        return result;
    }

    /**
     * 教练选择器查询
     */
    public List<CoachSelectorVO> queryCoachSelector(CoachSelectorQueryForm form) {
        // TODO: 实现教练查询逻辑
        List<CoachSelectorVO> result = new ArrayList<>();
        
        // 这里先返回模拟数据，后续实现具体查询逻辑
        CoachSelectorVO coach1 = new CoachSelectorVO();
        coach1.setCoachId(1L);
        coach1.setUserName("李教练");
        coach1.setLevel("ADVANCED");
        coach1.setLevelText("高级");
        coach1.setDisplayLabel("李教练 (高级)");
        result.add(coach1);
        
        return result;
    }

    /**
     * 员工选择器查询
     */
    public List<EmployeeSelectorVO> queryEmployeeSelector(EmployeeSelectorQueryForm form) {
        // TODO: 实现员工查询逻辑
        List<EmployeeSelectorVO> result = new ArrayList<>();
        
        // 这里先返回模拟数据，后续实现具体查询逻辑
        EmployeeSelectorVO employee1 = new EmployeeSelectorVO();
        employee1.setEmployeeId(1L);
        employee1.setActualName("王小明");
        employee1.setDisplayLabel("王小明");
        result.add(employee1);
        
        return result;
    }

    /**
     * 俱乐部选择器查询
     */
    public List<ClubSelectorVO> queryClubSelector(ClubSelectorQueryForm form) {
        // TODO: 实现俱乐部查询逻辑
        List<ClubSelectorVO> result = new ArrayList<>();
        
        // 这里先返回模拟数据，后续实现具体查询逻辑
        ClubSelectorVO club1 = new ClubSelectorVO();
        club1.setClubId(1L);
        club1.setClubName("阳光马术俱乐部");
        club1.setCity("北京");
        club1.setDisplayLabel("阳光马术俱乐部 (北京)");
        result.add(club1);
        
        return result;
    }

    /**
     * 课程选择器查询
     */
    public List<CourseSelectorVO> queryCourseSelector(CourseSelectorQueryForm form) {
        // TODO: 实现课程查询逻辑
        List<CourseSelectorVO> result = new ArrayList<>();
        
        // 这里先返回模拟数据，后续实现具体查询逻辑
        CourseSelectorVO course1 = new CourseSelectorVO();
        course1.setCourseId(1L);
        course1.setCourseName("基础骑行课程");
        course1.setCourseType("EXPERIENCE");
        course1.setCourseTypeName("体验课");
        course1.setDisplayLabel("基础骑行课程 (体验课)");
        result.add(course1);
        
        return result;
    }

    /**
     * 格式化手机号脱敏
     */
    private String maskPhone(String phone) {
        if (!StringUtils.hasText(phone) || phone.length() < 7) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}