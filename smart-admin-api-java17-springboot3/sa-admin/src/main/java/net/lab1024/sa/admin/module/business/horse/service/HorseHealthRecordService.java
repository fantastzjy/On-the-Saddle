package net.lab1024.sa.admin.module.business.horse.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.horse.dao.HorseHealthPlanDao;
import net.lab1024.sa.admin.module.business.horse.dao.HorseHealthRecordDao;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseHealthPlanEntity;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseHealthRecordEntity;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthRecordCreateForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthRecordQueryForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthRecordUpdateForm;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseHealthRecordListVO;
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
 * 马匹健康记录Service
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class HorseHealthRecordService {

    @Resource
    private HorseHealthRecordDao horseHealthRecordDao;

    @Resource
    private HorseHealthPlanDao horseHealthPlanDao;

    @Resource
    private DataTracerService dataTracerService;

    /**
     * 分页查询健康记录
     */
    public ResponseDTO<PageResult<HorseHealthRecordListVO>> queryByPage(HorseHealthRecordQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<HorseHealthRecordListVO> recordList = horseHealthRecordDao.queryByPage(page, queryForm);
        PageResult<HorseHealthRecordListVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, recordList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 查询健康记录详情
     */
    public ResponseDTO<HorseHealthRecordEntity> getDetail(Long recordId) {
        HorseHealthRecordEntity entity = horseHealthRecordDao.selectById(recordId);
        if (entity == null || entity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("健康记录不存在");
        }
        return ResponseDTO.ok(entity);
    }

    /**
     * 新建健康记录
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> create(HorseHealthRecordCreateForm createForm) {
        HorseHealthRecordEntity entity = SmartBeanUtil.copy(createForm, HorseHealthRecordEntity.class);
        entity.setIsValid(1);
        entity.setIsDelete(0);
        horseHealthRecordDao.insert(entity);

        // 如果关联了健康计划且设置了下次执行日期，更新计划的下次执行时间
        if (createForm.getPlanId() != null && createForm.getNextDate() != null) {
            horseHealthPlanDao.updateNextDate(createForm.getPlanId(), createForm.getNextDate(), createForm.getRecordDate());
        }

        // 记录数据变更日志
        dataTracerService.insert(entity.getId(), DataTracerTypeEnum.CLUB_HORSE_HEALTH_RECORD);

        return ResponseDTO.ok();
    }

    /**
     * 更新健康记录
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(HorseHealthRecordUpdateForm updateForm) {
        // 验证记录是否存在
        HorseHealthRecordEntity existEntity = horseHealthRecordDao.selectById(updateForm.getId());
        if (existEntity == null || existEntity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("健康记录不存在");
        }

        HorseHealthRecordEntity entity = SmartBeanUtil.copy(updateForm, HorseHealthRecordEntity.class);
        horseHealthRecordDao.updateById(entity);

        // 记录数据变更日志
        dataTracerService.update(entity.getId(), DataTracerTypeEnum.CLUB_HORSE_HEALTH_RECORD, existEntity, entity);

        return ResponseDTO.ok();
    }

    /**
     * 删除健康记录
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long recordId) {
        HorseHealthRecordEntity entity = horseHealthRecordDao.selectById(recordId);
        if (entity == null || entity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("健康记录不存在");
        }

        entity.setIsDelete(1);
        horseHealthRecordDao.updateById(entity);

        // 记录数据变更日志
        dataTracerService.delete(recordId, DataTracerTypeEnum.CLUB_HORSE_HEALTH_RECORD);

        return ResponseDTO.ok();
    }

    /**
     * 查询马匹的健康记录列表（时间线）
     */
    public ResponseDTO<List<HorseHealthRecordListVO>> queryByHorseId(Long horseId) {
        List<HorseHealthRecordListVO> list = horseHealthRecordDao.queryByHorseId(horseId);
        return ResponseDTO.ok(list);
    }

    /**
     * 查询计划的健康记录列表
     */
    public ResponseDTO<List<HorseHealthRecordListVO>> queryByPlanId(Long planId) {
        List<HorseHealthRecordListVO> list = horseHealthRecordDao.queryByPlanId(planId);
        return ResponseDTO.ok(list);
    }

    /**
     * 根据健康计划快速创建记录
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createFromPlan(Long planId, String content, String imgUrl, String recordData) {
        // 查询健康计划
        HorseHealthPlanEntity plan = horseHealthPlanDao.selectById(planId);
        if (plan == null || plan.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("健康计划不存在");
        }

        // 创建健康记录
        HorseHealthRecordEntity entity = new HorseHealthRecordEntity();
        entity.setHorseId(plan.getHorseId());
        entity.setPlanId(planId);
        entity.setRecordType(plan.getPlanType());
        entity.setRecordDate(LocalDateTime.now());
        entity.setContent(content);
        entity.setImgUrl(imgUrl);
        entity.setRecordData(recordData);
        entity.setIsValid(1);
        entity.setIsDelete(0);

        // 计算下次执行时间
        LocalDateTime nextDate = null;
        if (plan.getCycleDays() != null && plan.getCycleDays() > 0) {
            nextDate = LocalDateTime.now().plusDays(plan.getCycleDays());
            entity.setNextDate(nextDate);
        }

        horseHealthRecordDao.insert(entity);

        // 更新计划的下次执行时间
        if (nextDate != null) {
            horseHealthPlanDao.updateNextDate(planId, nextDate, LocalDateTime.now());
        }

        // 记录数据变更日志
        dataTracerService.insert(entity.getId(), DataTracerTypeEnum.CLUB_HORSE_HEALTH_RECORD);

        return ResponseDTO.ok();
    }
}