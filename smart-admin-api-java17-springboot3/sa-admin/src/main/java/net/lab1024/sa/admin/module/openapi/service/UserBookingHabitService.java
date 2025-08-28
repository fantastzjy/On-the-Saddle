package net.lab1024.sa.admin.module.openapi.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.openapi.domain.vo.UserBookingHabitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户约课习惯查询服务
 *
 * @Author Claude Code
 * @Date 2025-01-27
 * @Copyright 马术俱乐部管理系统
 */
@Service
@Slf4j
public class UserBookingHabitService {
    
    @Autowired
    private MemberDao memberDao;
    
    /**
     * 获取用户约课习惯信息
     */
    public UserBookingHabitVO getUserBookingHabit(Long memberId) {
        log.info("查询用户约课习惯，会员ID：{}", memberId);
        
        UserBookingHabitVO habit = new UserBookingHabitVO();
        habit.setMemberId(memberId);
        
        try {
            // 1. 查询会员基本信息
            MemberEntity member = memberDao.selectById(memberId);
            if (member != null) {
                habit.setDefaultCoachId(member.getDefaultCoachId());
                habit.setDefaultCourseLevel(member.getDefaultCourseLevel());
                habit.setClubId(member.getClubId());
                habit.setUserRole(determineUserRole(member));
                
                log.info("查询到会员信息：默认教练ID={}，默认课程级别={}，用户角色={}", 
                    member.getDefaultCoachId(), member.getDefaultCourseLevel(), habit.getUserRole());
            } else {
                log.warn("未找到会员信息，会员ID：{}", memberId);
                habit.setUserRole("新会员");
            }
            
            // 2. 设置常用教练（简化处理，后续完善）
            habit.setFrequentCoachIds(getFrequentCoachIds(memberId));
            
            // 3. 设置常用课程类型（简化处理，后续完善）
            habit.setFrequentCourseTypes(getFrequentCourseTypes());
            
            log.info("用户约课习惯查询完成：{}", habit);
            
        } catch (Exception e) {
            log.error("查询用户约课习惯失败，会员ID：{}", memberId, e);
            // 设置默认值
            habit.setUserRole("新会员");
            habit.setFrequentCoachIds(new ArrayList<>());
            habit.setFrequentCourseTypes(Arrays.asList("基础课程", "进阶课程"));
        }
        
        return habit;
    }
    
    /**
     * 确定用户角色
     */
    private String determineUserRole(MemberEntity member) {
        if (member == null) {
            return "新会员";
        }
        
        // 根据会员信息判断角色
        if (member.getIsMembership() != null && member.getIsMembership() == 1) {
            // 有会籍的会员
            if (member.getDefaultCoachId() != null && member.getDefaultCoachId() > 0) {
                return "老会员";
            } else {
                return "新会员";
            }
        } else {
            // 无会籍的会员，可能是马主
            return "马主";
        }
    }
    
    /**
     * 获取常用教练ID列表（简化处理）
     */
    private List<Long> getFrequentCoachIds(Long memberId) {
        // 简化处理，返回空列表，后续完善
        return new ArrayList<>();
    }
    
    /**
     * 获取常用课程类型列表（简化处理）
     */
    private List<String> getFrequentCourseTypes() {
        // 简化处理，返回默认课程类型
        return Arrays.asList("基础课程", "进阶课程", "专业课程");
    }
}
