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
import net.lab1024.sa.admin.module.business.order.service.OrderService;
import net.lab1024.sa.admin.module.business.order.domain.form.OrderAddForm;
import net.lab1024.sa.admin.module.business.order.constant.OrderSubTypeEnum;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
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

    @Resource
    private OrderService orderService;

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
        
        // 如果关联了健康计划，从计划中获取并设置计划类型
        if (createForm.getPlanId() != null) {
            HorseHealthPlanEntity plan = horseHealthPlanDao.selectById(createForm.getPlanId());
            if (plan != null && plan.getIsDelete() == 0) {
                entity.setPlanType(plan.getPlanType());
            }
        }
        
        horseHealthRecordDao.insert(entity);

        // 如果关联了健康计划且设置了下次执行日期，更新计划的下次执行时间
        if (createForm.getPlanId() != null && createForm.getNextDate() != null) {
            horseHealthPlanDao.updateNextDate(createForm.getPlanId(), createForm.getNextDate(), createForm.getRecordDate());
        }

        // 2. 如果有实际费用，自动生成健康服务订单
        if (createForm.getActualCost() != null && createForm.getActualCost().compareTo(BigDecimal.ZERO) > 0) {
            generateHealthServiceOrder(entity, createForm.getActualCost());
        }

        // 记录数据变更日志
        dataTracerService.insert(entity.getId(), DataTracerTypeEnum.CLUB_HORSE_HEALTH_RECORD);

        return ResponseDTO.ok();
    }

    /**
     * 根据健康记录自动生成健康服务订单
     */
    private void generateHealthServiceOrder(HorseHealthRecordEntity record, BigDecimal actualCost) {
        try {
            OrderAddForm orderForm = new OrderAddForm();
            orderForm.setClubId(1L); // TODO: 从当前登录用户获取俱乐部ID
            orderForm.setMemberId(getHorseOwnerId(record.getHorseId())); // 获取马主ID
            orderForm.setOrderType(6); // 健康服务订单
            orderForm.setOrderSubType(getHealthServiceSubType(record.getPlanType()));
            orderForm.setIsDirectPrice(1); // 直接定价
            orderForm.setServiceAmount(actualCost);
            orderForm.setQuantity(1);
            orderForm.setTargetHorseId(record.getHorseId());
            orderForm.setRelatedHealthRecordId(record.getId());
            orderForm.setRemark("健康服务执行完成，系统自动生成订单");
            orderForm.setPaymentMethod("cash"); // 默认现金支付
            
            ResponseDTO<Long> result = orderService.createOrder(orderForm);
            if (result.getOk()) {
                log.info("健康服务订单生成成功，订单ID：{}, 健康记录ID：{}", result.getData(), record.getId());
            } else {
                log.error("健康服务订单生成失败：{}", result.getMsg());
            }
        } catch (Exception e) {
            log.error("自动生成健康服务订单异常", e);
        }
    }

    /**
     * 根据健康计划类型获取对应的订单子类型
     */
    private String getHealthServiceSubType(String planType) {
        switch (planType) {
            case "shoeing": return OrderSubTypeEnum.HEALTH_SHOEING.getCode();
            case "deworming": return OrderSubTypeEnum.HEALTH_DEWORMING.getCode();
            case "dental": return OrderSubTypeEnum.HEALTH_DENTAL.getCode();
            case "vaccine": return OrderSubTypeEnum.HEALTH_VACCINE.getCode();
            case "medication": return OrderSubTypeEnum.HEALTH_MEDICATION.getCode();
            default: return OrderSubTypeEnum.HEALTH_MEDICATION.getCode();
        }
    }

    /**
     * 获取马主ID (暂时返回固定值，实际需要查询马匹表)
     * TODO: 实现真实的马主ID查询逻辑
     */
    private Long getHorseOwnerId(Long horseId) {
        // 暂时返回一个固定的会员ID，实际需要根据马匹查询马主
        return 1L;
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
        entity.setPlanType(plan.getPlanType());
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