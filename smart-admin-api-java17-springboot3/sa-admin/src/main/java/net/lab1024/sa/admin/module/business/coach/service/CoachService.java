package net.lab1024.sa.admin.module.business.coach.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.coach.constant.CoachCertificateConstant;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.coach.domain.form.CoachCreateForm;
import net.lab1024.sa.admin.module.business.coach.domain.form.CoachQueryForm;
import net.lab1024.sa.admin.module.business.coach.domain.form.CoachUpdateForm;
import net.lab1024.sa.admin.module.business.coach.domain.vo.CertificateVO;
import net.lab1024.sa.admin.module.business.coach.domain.vo.CoachListVO;
import net.lab1024.sa.admin.module.business.coach.domain.vo.CoachVO;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.sa.admin.module.system.employee.domain.form.EmployeeByRoleQueryForm;
import net.lab1024.sa.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.sa.admin.module.system.role.service.RoleEmployeeService;
import net.lab1024.sa.admin.module.system.role.domain.form.RoleEmployeeUpdateForm;
import java.util.Set;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.apache.commons.lang3.StringUtils;
import net.lab1024.sa.base.module.support.securityprotect.service.SecurityPasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 教练管理服务
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class CoachService {

    @Resource
    private CoachDao coachDao;

    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private EmployeeDao employeeDao;

    @Resource
    private RoleEmployeeService roleEmployeeService;

    @Resource
    private SecurityPasswordService securityPasswordService;

    private static final Long COACH_DEPARTMENT_ID = 100L; // 教练部门ID
    private static final Long COACH_ROLE_ID = 36L; // 教练角色ID

    /**
     * 分页查询教练
     */
    public ResponseDTO<PageResult<CoachListVO>> queryByPage(CoachQueryForm queryForm) {
        queryForm.setIsDelete(0);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<CoachListVO> coachList = coachDao.queryPage(page, queryForm);
        PageResult<CoachListVO> pageResult = SmartPageUtil.convert2PageResult(page, coachList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询教练详情（扁平化结构版）
     */
    public ResponseDTO<CoachVO> getDetail(Long coachId) {
        log.info("=== 查询教练详情开始 === coachId={}", coachId);

        CoachVO coachVO = coachDao.getDetail(coachId);
        if (coachVO == null) {
            return ResponseDTO.userErrorParam("教练不存在");
        }

        // 获取完整的教练实体信息（包含扁平化证书字段）
        CoachEntity coachEntity = coachDao.selectById(coachId);
        if (coachEntity != null) {
            log.info("从数据库查询到的扁平化证书数据: coachStarLevel={}, riderShowJumpingLevel={}",
                    coachEntity.getCoachStarLevel(), coachEntity.getRiderShowJumpingLevel());

            coachVO.setCoachStarLevel(coachEntity.getCoachStarLevel());
            coachVO.setCoachStarCertImages(coachEntity.getCoachStarCertImages());
            coachVO.setCoachShowJumpingLevel(coachEntity.getCoachShowJumpingLevel());
            coachVO.setCoachShowJumpingImages(coachEntity.getCoachShowJumpingImages());
            coachVO.setCoachDressageLevel(coachEntity.getCoachDressageLevel());
            coachVO.setCoachDressageImages(coachEntity.getCoachDressageImages());
            coachVO.setCoachEventingLevel(coachEntity.getCoachEventingLevel());
            coachVO.setCoachEventingImages(coachEntity.getCoachEventingImages());

            coachVO.setRiderShowJumpingLevel(coachEntity.getRiderShowJumpingLevel());
            coachVO.setRiderShowJumpingImages(coachEntity.getRiderShowJumpingImages());
            coachVO.setRiderDressageLevel(coachEntity.getRiderDressageLevel());
            coachVO.setRiderDressageImages(coachEntity.getRiderDressageImages());
            coachVO.setRiderEventingLevel(coachEntity.getRiderEventingLevel());
            coachVO.setRiderEventingImages(coachEntity.getRiderEventingImages());

            // 设置身份证照片
            coachVO.setIdCardFrontImg(coachEntity.getIdCardFrontImg());
            coachVO.setIdCardBackImg(coachEntity.getIdCardBackImg());

            // 设置证号
            coachVO.setCoachCertNo(coachEntity.getCoachCertNo());
            coachVO.setRiderCertNo(coachEntity.getRiderCertNo());
        }

        log.info("最终返回的coachVO扁平化证书数据设置完成");

        return ResponseDTO.ok(coachVO);
    }

    /**
     * 新建教练
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> create(CoachCreateForm createForm) {
        // 校验教练编号是否重复
        if (StringUtils.isNotBlank(createForm.getCoachNo())) {
            CoachEntity existCoach = coachDao.selectByCoachNo(createForm.getCoachNo());
            if (existCoach != null) {
                return ResponseDTO.userErrorParam("教练编号已存在");
            }
        }

        try {
            // 1. 创建简单的员工占位记录（仅用于ID绑定）
            EmployeeEntity employee = createPlaceholderEmployee(createForm);
            employeeDao.insert(employee);

            // 2. 创建教练记录（包含冗余的基础信息）
            CoachEntity coachEntity = SmartBeanUtil.copy(createForm, CoachEntity.class);
            coachEntity.setUserId(employee.getEmployeeId());

            // 设置冗余字段
            coachEntity.setActualName(createForm.getActualName());
            coachEntity.setPhone(createForm.getPhone());
            coachEntity.setEmail(createForm.getEmail());
            coachEntity.setGender(createForm.getGender());
            coachEntity.setBirthDate(createForm.getBirthDate());
            coachEntity.setIdCard(createForm.getIdCard());
            coachEntity.setDepartmentId(COACH_DEPARTMENT_ID);

            coachEntity.setCreateTime(LocalDateTime.now());
            coachEntity.setUpdateTime(LocalDateTime.now());
            coachEntity.setIsValid(1);
            coachEntity.setIsDelete(0);

            // 设置默认排序
            if (coachEntity.getSortOrder() == null) {
                coachEntity.setSortOrder(0);
            }

            coachDao.insert(coachEntity);

            // 3. 分配教练角色
            if (COACH_ROLE_ID != null) {
                RoleEmployeeUpdateForm roleForm = new RoleEmployeeUpdateForm();
                roleForm.setRoleId(COACH_ROLE_ID);
                roleForm.setEmployeeIdList(Set.of(employee.getEmployeeId()));
                roleEmployeeService.batchAddRoleEmployee(roleForm);
            }

            // 记录数据变动
            dataTracerService.insert(coachEntity.getCoachId(), DataTracerTypeEnum.CLUB_COACH);

            return ResponseDTO.ok();

        } catch (Exception e) {
            log.error("创建教练失败", e);
            return ResponseDTO.userErrorParam("创建教练失败: " + e.getMessage());
        }
    }

    /**
     * 更新教练（扁平化结构版）
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> update(CoachUpdateForm updateForm) {
        log.info("=== 教练更新开始（扁平化结构版） ===");
        log.info("更新表单数据: coachId={}, coachStarLevel={}, riderShowJumpingLevel={}",
                updateForm.getCoachId(), updateForm.getCoachStarLevel(), updateForm.getRiderShowJumpingLevel());

        CoachEntity oldCoachEntity = coachDao.selectById(updateForm.getCoachId());
        if (oldCoachEntity == null || oldCoachEntity.getIsDelete().equals(1)) {
            return ResponseDTO.userErrorParam("教练不存在");
        }

        // 校验教练编号是否重复
        if (StringUtils.isNotBlank(updateForm.getCoachNo())) {
            CoachEntity existCoach = coachDao.selectByCoachNo(updateForm.getCoachNo());
            if (existCoach != null && !existCoach.getCoachId().equals(updateForm.getCoachId())) {
                return ResponseDTO.userErrorParam("教练编号已存在");
            }
        }

        // 校验俱乐部和用户ID的唯一性
        CoachEntity existCoachByClubUser = coachDao.selectByClubAndUser(updateForm.getClubId(), updateForm.getUserId());
        if (existCoachByClubUser != null && !existCoachByClubUser.getCoachId().equals(updateForm.getCoachId())) {
            return ResponseDTO.userErrorParam("该用户在此俱乐部已存在教练记录");
        }

        CoachEntity coachEntity = SmartBeanUtil.copy(updateForm, CoachEntity.class);
        coachEntity.setUpdateTime(LocalDateTime.now());

        log.info("复制后的扁平化证书数据: coachStarLevel={}, coachStarCertImages={}, riderShowJumpingLevel={}, riderShowJumpingImages={}",
                coachEntity.getCoachStarLevel(), coachEntity.getCoachStarCertImages(),
                coachEntity.getRiderShowJumpingLevel(), coachEntity.getRiderShowJumpingImages());

        coachDao.updateById(coachEntity);

        // 验证更新后的数据
        CoachEntity updatedEntity = coachDao.selectById(updateForm.getCoachId());
        log.info("数据库更新后的扁平化数据: coachStarLevel={}, riderShowJumpingLevel={}",
                updatedEntity.getCoachStarLevel(), updatedEntity.getRiderShowJumpingLevel());

        // 记录数据变动
        dataTracerService.update(coachEntity.getCoachId(), DataTracerTypeEnum.CLUB_COACH, oldCoachEntity, coachEntity);

        return ResponseDTO.ok();
    }

    /**
     * 删除教练
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> delete(Long coachId) {
        CoachEntity coachEntity = coachDao.selectById(coachId);
        if (coachEntity == null || coachEntity.getIsDelete().equals(1)) {
            return ResponseDTO.userErrorParam("教练不存在");
        }

        coachEntity.setIsDelete(1);
        coachEntity.setUpdateTime(LocalDateTime.now());
        coachDao.updateById(coachEntity);

        // 记录数据变动
        dataTracerService.delete(coachId, DataTracerTypeEnum.CLUB_COACH);

        return ResponseDTO.ok();
    }

    /**
     * 教练列表查询
     */
    public ResponseDTO<List<CoachListVO>> queryList(Integer isValid, Long clubId) {
        List<CoachListVO> coachList = coachDao.queryList(isValid, clubId);
        return ResponseDTO.ok(coachList);
    }

    /**
     * 教练作为员工查询 - 直接查教练表
     */
    public ResponseDTO<PageResult<EmployeeVO>> queryCoachAsEmployees(EmployeeByRoleQueryForm queryForm) {
        try {
            // 直接查询教练表，不联表员工表
            LambdaQueryWrapper<CoachEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CoachEntity::getIsDelete, 0)
                   .eq(CoachEntity::getIsValid, 1);

            // 关键字搜索（搜索教练表的冗余字段）
            if (SmartStringUtil.isNotBlank(queryForm.getKeyword())) {
                wrapper.and(w -> w.like(CoachEntity::getActualName, queryForm.getKeyword())
                               .or().like(CoachEntity::getPhone, queryForm.getKeyword())
                               .or().like(CoachEntity::getCoachNo, queryForm.getKeyword()));
            }

            wrapper.orderByAsc(CoachEntity::getSortOrder)
                   .orderByDesc(CoachEntity::getCreateTime);

            // 分页查询
            Page<CoachEntity> page = new Page<>(queryForm.getPageNum(), queryForm.getPageSize());
            IPage<CoachEntity> pageResult = coachDao.selectPage(page, wrapper);

            // 转换为EmployeeVO格式
            List<EmployeeVO> employeeVOList = pageResult.getRecords().stream()
                .map(this::convertCoachToEmployeeVO)
                .collect(Collectors.toList());

            PageResult<EmployeeVO> result = new PageResult<>();
            result.setList(employeeVOList);
            result.setTotal(pageResult.getTotal());
            result.setPageNum(pageResult.getCurrent());
            result.setPageSize(pageResult.getSize());

            return ResponseDTO.ok(result);

        } catch (Exception e) {
            log.error("查询教练作为员工失败", e);
            return ResponseDTO.userErrorParam("查询教练员工失败");
        }
    }

    /**
     * 将教练转换为员工VO
     */
    private EmployeeVO convertCoachToEmployeeVO(CoachEntity coach) {
        EmployeeVO vo = new EmployeeVO();

        // 基础信息来自教练表的冗余字段
        vo.setEmployeeId(coach.getUserId()); // 绑定的员工ID
        vo.setActualName(coach.getActualName());
        vo.setPhone(coach.getPhone());
        vo.setGender(coach.getGender());
        vo.setBirthDate(coach.getBirthDate());
        vo.setIdCard(coach.getIdCard());
        vo.setDepartmentId(coach.getDepartmentId());

        // 教练专业信息 - 扩展字段
        vo.setCoachId(coach.getCoachId());
        vo.setCoachNo(coach.getCoachNo());
        // vo.setCoachLevel(coach.getCoachLevel());//todo
        vo.setSpecialties(coach.getSpecialties());

        // 角色信息
        vo.setRoleNameList(Arrays.asList("教练"));

        // 备注显示教练专业信息
        StringBuilder remark = new StringBuilder(); //todo
        // if (SmartStringUtil.isNotBlank(coach.getCoachLevel())) {
        //     remark.append("等级: ").append(coach.getCoachLevel());
        // }
        if (SmartStringUtil.isNotBlank(coach.getSpecialties())) {
            if (remark.length() > 0) remark.append(" | ");
            remark.append("专长: ").append(coach.getSpecialties());
        }
        vo.setRemark(remark.toString());

        // 状态信息
        vo.setDisabledFlag(coach.getIsValid() == 0);
        vo.setDeletedFlag(coach.getIsDelete() == 1);

        return vo;
    }

    /**
     * 创建占位员工记录（最简化）
     */
    private EmployeeEntity createPlaceholderEmployee(CoachCreateForm form) {
        EmployeeEntity employee = new EmployeeEntity();

        // 最基本的必填信息
        employee.setLoginName("coach_" + form.getCoachNo());
        employee.setLoginPwd(SecurityPasswordService.getEncryptPwd("temp_pass_" + System.currentTimeMillis()));
        employee.setActualName(form.getActualName()); // 同步姓名，保持一致性
        employee.setDepartmentId(COACH_DEPARTMENT_ID);

        // 标记为占位员工
        employee.setEmployeeType(2);
        employee.setDisabledFlag(true); // 禁用登录
        employee.setDeletedFlag(false);
        employee.setAdministratorFlag(false);

        return employee;
    }
}
