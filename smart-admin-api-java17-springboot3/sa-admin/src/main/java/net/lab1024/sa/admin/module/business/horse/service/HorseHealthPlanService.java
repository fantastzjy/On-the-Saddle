package net.lab1024.sa.admin.module.business.horse.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.horse.dao.HorseHealthPlanDao;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseHealthPlanEntity;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthPlanCreateForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthPlanQueryForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthPlanUpdateForm;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseHealthPlanListVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 马匹健康计划Service
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class HorseHealthPlanService {

    @Resource
    private HorseHealthPlanDao horseHealthPlanDao;

    @Resource
    private DataTracerService dataTracerService;

    /**
     * 分页查询健康计划
     */
    public ResponseDTO<PageResult<HorseHealthPlanListVO>> queryByPage(HorseHealthPlanQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<HorseHealthPlanListVO> planList = horseHealthPlanDao.queryByPage(page, queryForm);
        PageResult<HorseHealthPlanListVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, planList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 查询健康计划详情
     */
    public ResponseDTO<HorseHealthPlanEntity> getDetail(Long planId) {
        HorseHealthPlanEntity entity = horseHealthPlanDao.selectById(planId);
        if (entity == null || entity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("健康计划不存在");
        }
        return ResponseDTO.ok(entity);
    }

    /**
     * 新建健康计划
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> create(HorseHealthPlanCreateForm createForm) {
        HorseHealthPlanEntity entity = SmartBeanUtil.copy(createForm, HorseHealthPlanEntity.class);
        entity.setIsValid(1);
        entity.setIsDelete(0);
        horseHealthPlanDao.insert(entity);

        // 记录数据变更日志
        dataTracerService.insert(entity.getId(), DataTracerTypeEnum.CLUB_HORSE_HEALTH_PLAN);

        return ResponseDTO.ok();
    }

    /**
     * 更新健康计划
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(HorseHealthPlanUpdateForm updateForm) {
        // 验证计划是否存在
        HorseHealthPlanEntity existEntity = horseHealthPlanDao.selectById(updateForm.getId());
        if (existEntity == null || existEntity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("健康计划不存在");
        }

        HorseHealthPlanEntity entity = SmartBeanUtil.copy(updateForm, HorseHealthPlanEntity.class);
        horseHealthPlanDao.updateById(entity);

        // 记录数据变更日志
        dataTracerService.update(entity.getId(), DataTracerTypeEnum.CLUB_HORSE_HEALTH_PLAN, existEntity, entity);

        return ResponseDTO.ok();
    }

    /**
     * 删除健康计划
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long planId) {
        HorseHealthPlanEntity entity = horseHealthPlanDao.selectById(planId);
        if (entity == null || entity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("健康计划不存在");
        }

        entity.setIsDelete(1);
        horseHealthPlanDao.updateById(entity);

        // 记录数据变更日志
        dataTracerService.delete(planId, DataTracerTypeEnum.CLUB_HORSE_HEALTH_PLAN);

        return ResponseDTO.ok();
    }

    /**
     * 查询马匹的健康计划列表
     */
    public ResponseDTO<List<HorseHealthPlanListVO>> queryByHorseId(Long horseId) {
        List<HorseHealthPlanListVO> list = horseHealthPlanDao.queryByHorseId(horseId);
        return ResponseDTO.ok(list);
    }

    /**
     * 查询需要提醒的健康计划
     */
    public ResponseDTO<List<HorseHealthPlanListVO>> queryNeedReminder() {
        List<HorseHealthPlanListVO> list = horseHealthPlanDao.queryNeedReminder(LocalDateTime.now());
        return ResponseDTO.ok(list);
    }

    /**
     * 执行健康计划（更新下次执行时间）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> execute(Long planId) {
        HorseHealthPlanEntity entity = horseHealthPlanDao.selectById(planId);
        if (entity == null || entity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("健康计划不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDate = null;
        
        // 根据周期天数计算下次执行时间
        if (entity.getCycleDays() != null && entity.getCycleDays() > 0) {
            nextDate = now.plusDays(entity.getCycleDays());
        }

        horseHealthPlanDao.updateNextDate(planId, nextDate, now);

        return ResponseDTO.ok();
    }
}