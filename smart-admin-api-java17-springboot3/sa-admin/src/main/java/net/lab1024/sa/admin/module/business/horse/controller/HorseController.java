package net.lab1024.sa.admin.module.business.horse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseCreateForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseQueryForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseUpdateForm;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseListVO;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseVO;
import net.lab1024.sa.admin.module.business.horse.service.HorseService;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 马匹管理控制器
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@RestController
@Tag(name = AdminSwaggerTagConst.Business.CLUB_HORSE)
@OperateLog
public class HorseController {

    @Resource
    private HorseService horseService;

    @Operation(summary = "分页查询马匹")
    @PostMapping("/club/horse/page/query")
    @SaCheckPermission("club:horse:query")
    public ResponseDTO<PageResult<HorseListVO>> queryByPage(@RequestBody @Valid HorseQueryForm queryForm) {
        return horseService.queryByPage(queryForm);
    }

    @Operation(summary = "查询马匹详情")
    @GetMapping("/club/horse/get/{horseId}")
    @SaCheckPermission("club:horse:detail")
    public ResponseDTO<HorseVO> getDetail(@PathVariable Long horseId) {
        return horseService.getDetail(horseId);
    }

    @Operation(summary = "新建马匹")
    @PostMapping("/club/horse/create")
    @SaCheckPermission("club:horse:add")
    public ResponseDTO<String> create(@RequestBody @Valid HorseCreateForm createForm) {
        return horseService.create(createForm);
    }

    @Operation(summary = "更新马匹")
    @PostMapping("/club/horse/update")
    @SaCheckPermission("club:horse:update")
    public ResponseDTO<String> update(@RequestBody @Valid HorseUpdateForm updateForm) {
        return horseService.update(updateForm);
    }

    @Operation(summary = "删除马匹")
    @GetMapping("/club/horse/delete/{horseId}")
    @SaCheckPermission("club:horse:delete")
    public ResponseDTO<String> delete(@PathVariable Long horseId) {
        return horseService.delete(horseId);
    }

    @Operation(summary = "马匹列表查询")
    @GetMapping("/club/horse/query/list")
    @SaCheckPermission("club:horse:query")
    public ResponseDTO<List<HorseListVO>> queryList(@RequestParam(required = false) Long clubId,
                                                    @RequestParam(required = false) Integer horseType) {
        return horseService.queryList(clubId, horseType);
    }
}