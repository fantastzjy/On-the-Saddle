package net.lab1024.sa.admin.module.business.stablerental.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 马房租赁创建表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class StableRentalCreateForm {

    @Schema(description = "出租人ID")
    @NotNull(message = "出租人ID不能为空")
    private Long lessorId;

    @Schema(description = "租赁人ID")
    @NotNull(message = "租赁人ID不能为空")
    private Long lesseeId;

    @Schema(description = "租赁目标俱乐部ID")
    @NotNull(message = "租赁目标俱乐部ID不能为空")
    private Long targetClubId;

    @Schema(description = "租赁开始时间")
    @NotNull(message = "租赁开始时间不能为空")
    private LocalDateTime rentalStartTime;

    @Schema(description = "租赁结束时间")
    @NotNull(message = "租赁结束时间不能为空")
    private LocalDateTime rentalEndTime;

    @Schema(description = "租赁金额")
    @NotNull(message = "租赁金额不能为空")
    private BigDecimal rentalAmount;

    @Schema(description = "备注")
    private String remark;
}
