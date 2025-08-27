package net.lab1024.sa.admin.module.business.stablerental.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.business.stablerental.domain.form.StableRentalCreateForm;
import net.lab1024.sa.admin.module.business.stablerental.domain.form.StableRentalQueryForm;
import net.lab1024.sa.admin.module.business.stablerental.domain.form.StableRentalUpdateForm;
import net.lab1024.sa.admin.module.business.stablerental.domain.vo.StableRentalDetailVO;
import net.lab1024.sa.admin.module.business.stablerental.domain.vo.StableRentalListVO;
import net.lab1024.sa.admin.module.business.stablerental.service.StableRentalService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 马房租赁Controller
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Tag(name = "马房租赁")
@RequestMapping("/business/stable-rental")
public class StableRentalController {

    @Autowired
    private StableRentalService stableRentalService;

    @Operation(summary = "分页查询马房租赁列表")
    @PostMapping("/page")
    @SaCheckPermission("business:stable-rental:query")
    public ResponseDTO<PageResult<StableRentalListVO>> queryPage(@RequestBody @Valid StableRentalQueryForm queryForm) {
        return ResponseDTO.ok(stableRentalService.queryPage(queryForm));
    }

    @Operation(summary = "根据ID查询马房租赁详情")
    @GetMapping("/{rentalId}")
    @SaCheckPermission("business:stable-rental:query")
    public ResponseDTO<StableRentalDetailVO> getDetailById(@PathVariable Long rentalId) {
        return ResponseDTO.ok(stableRentalService.getDetailById(rentalId));
    }

    @Operation(summary = "创建马房租赁记录")
    @PostMapping
    @SaCheckPermission("business:stable-rental:add")
    public ResponseDTO<String> create(@RequestBody @Valid StableRentalCreateForm createForm) {
        return stableRentalService.create(createForm);
    }

    @Operation(summary = "更新马房租赁记录")
    @PostMapping("/update")
    @SaCheckPermission("business:stable-rental:edit")
    public ResponseDTO<String> update(@RequestBody @Valid StableRentalUpdateForm updateForm) {
        return stableRentalService.update(updateForm);
    }

    @Operation(summary = "删除马房租赁记录")
    @DeleteMapping("/{rentalId}")
    @SaCheckPermission("business:stable-rental:delete")
    public ResponseDTO<String> delete(@PathVariable Long rentalId) {
        return stableRentalService.delete(rentalId);
    }

    @Operation(summary = "批量删除马房租赁记录")
    @PostMapping("/batch/delete")
    @SaCheckPermission("business:stable-rental:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> rentalIdList) {
        for (Long rentalId : rentalIdList) {
            ResponseDTO<String> result = stableRentalService.delete(rentalId);
            if (!result.getOk()) {
                return result;
            }
        }
        return ResponseDTO.ok();
    }

    @Operation(summary = "更新租赁状态")
    @PostMapping("/{rentalId}/status/{status}")
    @SaCheckPermission("business:stable-rental:edit")
    public ResponseDTO<String> updateStatus(@PathVariable Long rentalId, @PathVariable Integer status) {
        return stableRentalService.updateStatus(rentalId, status);
    }
}