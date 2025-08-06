package net.lab1024.sa.admin.module.business.horse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.horse.constant.HealthPlanTypeEnum;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseHealthPlanEntity;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthPlanCreateForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthPlanQueryForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthPlanUpdateForm;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseHealthPlanListVO;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HealthPlanTypeOptionVO;
import net.lab1024.sa.admin.module.business.horse.service.HorseHealthPlanService;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 马匹健康计划控制器
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@RestController
@Tag(name = AdminSwaggerTagConst.Business.CLUB_HORSE_HEALTH_PLAN)
@OperateLog
public class HorseHealthPlanController {

    @Resource
    private HorseHealthPlanService horseHealthPlanService;

    @Operation(summary = "分页查询健康计划")
    @PostMapping("/club/horse/health/plan/page/query")
    @SaCheckPermission("club:horse:health:plan:query")
    public ResponseDTO<PageResult<HorseHealthPlanListVO>> queryByPage(@RequestBody @Valid HorseHealthPlanQueryForm queryForm) {
        return horseHealthPlanService.queryByPage(queryForm);
    }

    @Operation(summary = "查询健康计划详情")
    @GetMapping("/club/horse/health/plan/get/{planId}")
    @SaCheckPermission("club:horse:health:plan:query")
    public ResponseDTO<HorseHealthPlanEntity> getDetail(@PathVariable Long planId) {
        return horseHealthPlanService.getDetail(planId);
    }

    @Operation(summary = "新建健康计划")
    @PostMapping("/club/horse/health/plan/create")
    @SaCheckPermission("club:horse:health:plan:add")
    public ResponseDTO<String> create(@RequestBody @Valid HorseHealthPlanCreateForm createForm) {
        return horseHealthPlanService.create(createForm);
    }

    @Operation(summary = "更新健康计划")
    @PostMapping("/club/horse/health/plan/update")
    @SaCheckPermission("club:horse:health:plan:update")
    public ResponseDTO<String> update(@RequestBody @Valid HorseHealthPlanUpdateForm updateForm) {
        return horseHealthPlanService.update(updateForm);
    }

    @Operation(summary = "删除健康计划")
    @GetMapping("/club/horse/health/plan/delete/{planId}")
    @SaCheckPermission("club:horse:health:plan:delete")
    public ResponseDTO<String> delete(@PathVariable Long planId) {
        return horseHealthPlanService.delete(planId);
    }

    @Operation(summary = "查询马匹的健康计划列表")
    @GetMapping("/club/horse/health/plan/query/horse/{horseId}")
    @SaCheckPermission("club:horse:health:plan:query")
    public ResponseDTO<List<HorseHealthPlanListVO>> queryByHorseId(@PathVariable Long horseId) {
        return horseHealthPlanService.queryByHorseId(horseId);
    }

    @Operation(summary = "查询需要提醒的健康计划")
    @GetMapping("/club/horse/health/plan/query/reminder")
    @SaCheckPermission("club:horse:health:plan:query")
    public ResponseDTO<List<HorseHealthPlanListVO>> queryNeedReminder() {
        return horseHealthPlanService.queryNeedReminder();
    }

    @Operation(summary = "执行健康计划")
    @PostMapping("/club/horse/health/plan/execute/{planId}")
    @SaCheckPermission("club:horse:health:plan:update")
    public ResponseDTO<String> execute(@PathVariable Long planId) {
        return horseHealthPlanService.execute(planId);
    }

    @Operation(summary = "获取健康计划类型选项")
    @GetMapping("/club/horse/health/plan/types")
    @SaCheckPermission("club:horse:health:plan:query")
    public ResponseDTO<List<HealthPlanTypeOptionVO>> getPlanTypes() {
        List<HealthPlanTypeOptionVO> options = Arrays.stream(HealthPlanTypeEnum.values())
                .map(type -> new HealthPlanTypeOptionVO(type.getValue(), type.getDesc()))
                .collect(Collectors.toList());
        return ResponseDTO.ok(options);
    }
}