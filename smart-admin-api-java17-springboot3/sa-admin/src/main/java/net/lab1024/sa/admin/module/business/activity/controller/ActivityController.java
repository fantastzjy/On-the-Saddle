package net.lab1024.sa.admin.module.business.activity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.activity.domain.form.ActivityAddForm;
import net.lab1024.sa.admin.module.business.activity.domain.form.ActivityQueryForm;
import net.lab1024.sa.admin.module.business.activity.domain.form.ActivityUpdateForm;
import net.lab1024.sa.admin.module.business.activity.domain.vo.ActivityDetailVO;
import net.lab1024.sa.admin.module.business.activity.domain.vo.ActivityListVO;
import net.lab1024.sa.admin.module.business.activity.service.ActivityService;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 活动管理Controller
 *
 * @Author 1024创新实验室
 * @Date 2025-08-28
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Tag(name = "活动管理")
@RestController
@RequestMapping("/api/admin/business/activity")
public class ActivityController extends SupportBaseController {

    @Autowired
    private ActivityService activityService;

    @Operation(summary = "活动列表查询")
    @PostMapping("/query")
    public ResponseDTO<PageResult<ActivityListVO>> queryActivityList(@RequestBody @Valid ActivityQueryForm queryForm) {
        return activityService.queryActivityList(queryForm);
    }

    @Operation(summary = "活动详情查询")
    @GetMapping("/detail/{productId}")
    public ResponseDTO<ActivityDetailVO> queryActivityDetail(@PathVariable Long productId) {
        return activityService.queryActivityDetail(productId);
    }

    @Operation(summary = "活动新增")
    @PostMapping("/add")
    public ResponseDTO<String> addActivity(@RequestBody @Valid ActivityAddForm addForm) {
        // TODO: 临时写死俱乐部ID为1，后续需要从用户信息或权限中获取
        addForm.setClubId(1L);
        return activityService.addActivity(addForm, SmartRequestUtil.getRequestUserId());
    }

    @Operation(summary = "活动更新")
    @PostMapping("/update")
    public ResponseDTO<String> updateActivity(@RequestBody @Valid ActivityUpdateForm updateForm) {
        return activityService.updateActivity(updateForm, SmartRequestUtil.getRequestUserId());
    }

    @Operation(summary = "活动删除")
    @PostMapping("/delete/{productId}")
    public ResponseDTO<String> deleteActivity(@PathVariable Long productId) {
        return activityService.deleteActivity(productId, SmartRequestUtil.getRequestUserId());
    }
}