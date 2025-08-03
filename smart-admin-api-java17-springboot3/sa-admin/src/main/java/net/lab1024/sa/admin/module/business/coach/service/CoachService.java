package net.lab1024.sa.admin.module.business.coach.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.coach.dao.CoachDao;
import net.lab1024.sa.admin.module.business.coach.domain.entity.CoachEntity;
import net.lab1024.sa.admin.module.business.coach.domain.form.CoachCreateForm;
import net.lab1024.sa.admin.module.business.coach.domain.form.CoachQueryForm;
import net.lab1024.sa.admin.module.business.coach.domain.form.CoachUpdateForm;
import net.lab1024.sa.admin.module.business.coach.domain.vo.CoachListVO;
import net.lab1024.sa.admin.module.business.coach.domain.vo.CoachVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

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
     * 查询教练详情
     */
    public ResponseDTO<CoachVO> getDetail(Long coachId) {
        CoachVO coachVO = coachDao.getDetail(coachId);
        if (coachVO == null) {
            return ResponseDTO.userErrorParam("教练不存在");
        }
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

        // 校验俱乐部和用户ID的唯一性
        CoachEntity existCoachByClubUser = coachDao.selectByClubAndUser(createForm.getClubId(), createForm.getUserId());
        if (existCoachByClubUser != null) {
            return ResponseDTO.userErrorParam("该用户在此俱乐部已存在教练记录");
        }

        CoachEntity coachEntity = SmartBeanUtil.copy(createForm, CoachEntity.class);
        coachEntity.setCreateTime(LocalDateTime.now());
        coachEntity.setUpdateTime(LocalDateTime.now());
        coachEntity.setIsValid(1);
        coachEntity.setIsDelete(0);
        
        // 设置默认排序
        if (coachEntity.getSortOrder() == null) {
            coachEntity.setSortOrder(0);
        }

        coachDao.insert(coachEntity);

        // 记录数据变动
        dataTracerService.insert(coachEntity.getCoachId(), DataTracerTypeEnum.CLUB_COACH);

        return ResponseDTO.ok();
    }

    /**
     * 更新教练
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> update(CoachUpdateForm updateForm) {
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

        coachDao.updateById(coachEntity);

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
}