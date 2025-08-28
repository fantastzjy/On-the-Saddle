package net.lab1024.sa.admin.module.business.horse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseHealthRecordEntity;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthRecordCreateForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthRecordQueryForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseHealthRecordUpdateForm;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseHealthRecordListVO;
import net.lab1024.sa.admin.module.business.horse.service.HorseHealthRecordService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 马匹健康记录控制器
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@RestController
@Tag(name = AdminSwaggerTagConst.Business.CLUB_HORSE_HEALTH_RECORD)
@OperateLog
public class HorseHealthRecordController {

    @Resource
    private HorseHealthRecordService horseHealthRecordService;

    @Operation(summary = "分页查询健康记录")
    @PostMapping("/club/horse/health/record/page/query")
    @SaCheckPermission("club:horse:health:record:query")
    public ResponseDTO<PageResult<HorseHealthRecordListVO>> queryByPage(@RequestBody @Valid HorseHealthRecordQueryForm queryForm) {
        return horseHealthRecordService.queryByPage(queryForm);
    }

    @Operation(summary = "查询健康记录详情")
    @GetMapping("/club/horse/health/record/get/{recordId}")
    @SaCheckPermission("club:horse:health:record:query")
    public ResponseDTO<HorseHealthRecordEntity> getDetail(@PathVariable Long recordId) {
        return horseHealthRecordService.getDetail(recordId);
    }

    @Operation(summary = "新建健康记录")
    @PostMapping("/club/horse/health/record/create")
    @SaCheckPermission("club:horse:health:record:add")
    public ResponseDTO<String> create(@RequestBody @Valid HorseHealthRecordCreateForm createForm) {
        return horseHealthRecordService.create(createForm);
    }

    @Operation(summary = "更新健康记录")
    @PostMapping("/club/horse/health/record/update")
    @SaCheckPermission("club:horse:health:record:update")
    public ResponseDTO<String> update(@RequestBody @Valid HorseHealthRecordUpdateForm updateForm) {
        return horseHealthRecordService.update(updateForm);
    }

    @Operation(summary = "删除健康记录")
    @GetMapping("/club/horse/health/record/delete/{recordId}")
    @SaCheckPermission("club:horse:health:record:delete")
    public ResponseDTO<String> delete(@PathVariable Long recordId) {
        return horseHealthRecordService.delete(recordId);
    }

    @Operation(summary = "查询马匹的健康记录列表")
    @GetMapping("/club/horse/health/record/query/horse/{horseId}")
    @SaCheckPermission("club:horse:health:record:query")
    public ResponseDTO<List<HorseHealthRecordListVO>> queryByHorseId(@PathVariable Long horseId) {
        return horseHealthRecordService.queryByHorseId(horseId);
    }

    @Operation(summary = "查询计划的健康记录列表")
    @GetMapping("/club/horse/health/record/query/plan/{planId}")
    @SaCheckPermission("club:horse:health:record:query")
    public ResponseDTO<List<HorseHealthRecordListVO>> queryByPlanId(@PathVariable Long planId) {
        return horseHealthRecordService.queryByPlanId(planId);
    }

    @Operation(summary = "根据健康计划快速创建记录")
    @PostMapping("/club/horse/health/record/create/from-plan/{planId}")
    @SaCheckPermission("club:horse:health:record:add")
    public ResponseDTO<String> createFromPlan(@PathVariable Long planId,
                                              @RequestParam(required = false) String content,
                                              @RequestParam(required = false) String imgUrl,
                                              @RequestParam(required = false) String recordData) {
        return horseHealthRecordService.createFromPlan(planId, content, imgUrl, recordData);
    }
}
