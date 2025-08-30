package net.lab1024.sa.admin.module.business.selector.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.club.dao.ClubDao;
import net.lab1024.sa.admin.module.business.club.domain.entity.ClubEntity;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.horse.dao.HorseDao;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseEntity;
import net.lab1024.sa.admin.module.business.member.dao.MemberDao;
import net.lab1024.sa.admin.module.business.member.domain.entity.MemberEntity;
import net.lab1024.sa.admin.module.business.product.dao.ProductDao;
import net.lab1024.sa.admin.module.business.product.domain.entity.ProductEntity;
import net.lab1024.sa.admin.module.business.selector.domain.form.*;
import net.lab1024.sa.admin.module.business.selector.domain.vo.*;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ClubDao clubDao;

    @Autowired
    private ProductDao productDao;

    /**
     * 马匹选择器查询
     */
    public List<HorseSelectorVO> queryHorseSelector(HorseSelectorQueryForm form) {
        LambdaQueryWrapper<HorseEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HorseEntity::getClubId, form.getClubId())
                .eq(HorseEntity::getIsDelete, 0)
                .eq(HorseEntity::getIsValid, 1);

        // 关键词搜索
        if (StringUtils.hasText(form.getKeywords())) {
            wrapper.and(w -> w.like(HorseEntity::getHorseName, form.getKeywords())
                    .or().like(HorseEntity::getChipNo, form.getKeywords()));
        }

        // 马匹类型筛选
        if (form.getHorseType() != null) {
            wrapper.eq(HorseEntity::getHorseType, form.getHorseType());
        }

        // 健康状态筛选
        if (form.getHealthStatus() != null) {
            wrapper.eq(HorseEntity::getHealthStatus, form.getHealthStatus());
        }

        // 工作状态筛选
        if (form.getWorkStatus() != null) {
            wrapper.eq(HorseEntity::getWorkStatus, form.getWorkStatus());
        }

        // 分页查询
        Page<HorseEntity> page = new Page<>(form.getPageNum(), form.getPageSize());
        List<HorseEntity> horses = horseDao.selectPage(page, wrapper).getRecords();

        return horses.stream().map(horse -> {
            HorseSelectorVO vo = new HorseSelectorVO();
            vo.setHorseId(horse.getHorseId());
            vo.setHorseName(horse.getHorseName());
            vo.setChipNo(horse.getChipNo());
            vo.setDisplayLabel(horse.getHorseName() + " (" + horse.getChipNo() + ")");
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 会员选择器查询
     */
    public List<MemberSelectorVO> queryMemberSelector(MemberSelectorQueryForm form) {
        LambdaQueryWrapper<MemberEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberEntity::getClubId, form.getClubId())
                .eq(MemberEntity::getIsDelete, 0)
                .eq(MemberEntity::getIsValid, 1);

        // 关键词搜索
        if (StringUtils.hasText(form.getKeywords())) {
            wrapper.and(w -> w.like(MemberEntity::getActualName, form.getKeywords())
                    .or().like(MemberEntity::getPhone, form.getKeywords()));
        }

        // 分页查询
        Page<MemberEntity> page = new Page<>(form.getPageNum(), form.getPageSize());
        List<MemberEntity> members = memberDao.selectPage(page, wrapper).getRecords();

        return members.stream().map(member -> {
            MemberSelectorVO vo = new MemberSelectorVO();
            vo.setMemberId(member.getMemberId());
            vo.setActualName(member.getActualName());
            vo.setPhone(member.getPhone());
            vo.setMaskedPhone(maskPhone(member.getPhone()));
            vo.setDisplayLabel(member.getActualName() + " (" + maskPhone(member.getPhone()) + ")");
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 教练选择器查询
     */
    public List<CoachSelectorVO> queryCoachSelector(CoachSelectorQueryForm form) {
        LambdaQueryWrapper<CoachEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CoachEntity::getClubId, form.getClubId())
                .eq(CoachEntity::getIsDelete, 0)
                .eq(CoachEntity::getIsValid, 1);

        // 关键词搜索
        if (StringUtils.hasText(form.getKeywords())) {
            wrapper.and(w -> w.like(CoachEntity::getActualName, form.getKeywords())
                    .or().like(CoachEntity::getCoachNo, form.getKeywords()));
        }

        // 分页查询
        Page<CoachEntity> page = new Page<>(form.getPageNum(), form.getPageSize());
        List<CoachEntity> coaches = coachDao.selectPage(page, wrapper).getRecords();

        return coaches.stream().map(coach -> {
            CoachSelectorVO vo = new CoachSelectorVO();
            vo.setCoachId(coach.getCoachId());
            vo.setUserName(coach.getActualName());
            vo.setCoachNo(coach.getCoachNo());
            
            // 根据参数决定显示格式：教练费 或 教练编号
            if (Boolean.TRUE.equals(form.getShowCoachFee()) && coach.getCoachFee() != null) {
                String feeDisplay = "¥" + coach.getCoachFee().setScale(0, java.math.RoundingMode.HALF_UP);
                vo.setDisplayLabel(coach.getActualName() + " (" + feeDisplay + ")");
            } else {
                vo.setDisplayLabel(coach.getActualName() + " (" + coach.getCoachNo() + ")");
            }
            
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 员工选择器查询
     */
    public List<EmployeeSelectorVO> queryEmployeeSelector(EmployeeSelectorQueryForm form) {
        Page<EmployeeEntity> page = new Page<>(form.getPageNum(), form.getPageSize());
        
        // 如果有角色筛选，使用带角色的查询方法
        if (form.getRoleId() != null) {
            List<EmployeeVO> employees = employeeDao.queryEmployeeByRole(
                    page, 
                    form.getRoleId(), 
                    form.getKeywords(), 
                    false,  // 未禁用
                    false   // 未删除
            );
            
            return employees.stream().map(employee -> {
                EmployeeSelectorVO vo = new EmployeeSelectorVO();
                vo.setEmployeeId(employee.getEmployeeId());
                vo.setActualName(employee.getActualName());
                vo.setDisplayLabel(employee.getActualName());
                return vo;
            }).collect(Collectors.toList());
        } else {
            // 普通员工查询
            LambdaQueryWrapper<EmployeeEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(EmployeeEntity::getDisabledFlag, false)
                    .eq(EmployeeEntity::getDeletedFlag, false);

            // 关键词搜索
            if (StringUtils.hasText(form.getKeywords())) {
                wrapper.like(EmployeeEntity::getActualName, form.getKeywords());
            }

            List<EmployeeEntity> employees = employeeDao.selectPage(page, wrapper).getRecords();
            
            return employees.stream().map(employee -> {
                EmployeeSelectorVO vo = new EmployeeSelectorVO();
                vo.setEmployeeId(employee.getEmployeeId());
                vo.setActualName(employee.getActualName());
                vo.setDisplayLabel(employee.getActualName());
                return vo;
            }).collect(Collectors.toList());
        }
    }

    /**
     * 俱乐部选择器查询
     */
    public List<ClubSelectorVO> queryClubSelector(ClubSelectorQueryForm form) {
        LambdaQueryWrapper<ClubEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClubEntity::getIsDelete, 0)
                .eq(ClubEntity::getIsValid, 1);

        // 关键词搜索
        if (StringUtils.hasText(form.getKeywords())) {
            wrapper.and(w -> w.like(ClubEntity::getClubName, form.getKeywords())
                    .or().like(ClubEntity::getCity, form.getKeywords()));
        }

        // 省份筛选
        if (StringUtils.hasText(form.getProvince())) {
            wrapper.eq(ClubEntity::getProvince, form.getProvince());
        }

        // 城市筛选
        if (StringUtils.hasText(form.getCity())) {
            wrapper.eq(ClubEntity::getCity, form.getCity());
        }

        // 状态筛选
        if (form.getStatus() != null) {
            wrapper.eq(ClubEntity::getIsValid, form.getStatus());
        }

        // 分页查询
        Page<ClubEntity> page = new Page<>(form.getPageNum(), form.getPageSize());
        List<ClubEntity> clubs = clubDao.selectPage(page, wrapper).getRecords();

        return clubs.stream().map(club -> {
            ClubSelectorVO vo = new ClubSelectorVO();
            vo.setClubId(club.getClubId());
            vo.setClubName(club.getClubName());
            vo.setCity(club.getCity());
            vo.setDisplayLabel(club.getClubName() + " (" + club.getCity() + ")");
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 课程选择器查询
     */
    public List<CourseSelectorVO> queryCourseSelector(CourseSelectorQueryForm form) {
        LambdaQueryWrapper<ProductEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductEntity::getClubId, form.getClubId())
                .eq(ProductEntity::getProductType, 2) // 2-课程产品
                .eq(ProductEntity::getIsDelete, 0)
                .eq(ProductEntity::getIsValid, 1);

        // 关键词搜索
        if (StringUtils.hasText(form.getKeywords())) {
            wrapper.like(ProductEntity::getProductName, form.getKeywords());
        }

        // 分页查询
        Page<ProductEntity> page = new Page<>(form.getPageNum(), form.getPageSize());
        List<ProductEntity> products = productDao.selectPage(page, wrapper).getRecords();

        return products.stream().map(product -> {
            CourseSelectorVO vo = new CourseSelectorVO();
            vo.setCourseId(product.getProductId());
            vo.setCourseName(product.getProductName());
            vo.setCourseType(product.getSubType());
            // 这里需要根据实际的课程类型映射逻辑来设置courseTy peName
            vo.setCourseTypeName(getCourseTypeName(product.getSubType()));
            vo.setDisplayLabel(product.getProductName() + " (" + vo.getCourseTypeName() + ")");
            return vo;
        }).collect(Collectors.toList());
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

    /**
     * 获取课程类型名称
     */
    private String getCourseTypeName(String courseType) {
        // TODO: 根据实际业务逻辑实现课程类型映射
        if (courseType == null) {
            return "未分类";
        }
        switch (courseType) {
            case "EXPERIENCE":
                return "体验课";
            case "BASIC":
                return "基础课";
            case "ADVANCED":
                return "进阶课";
            default:
                return courseType;
        }
    }
}