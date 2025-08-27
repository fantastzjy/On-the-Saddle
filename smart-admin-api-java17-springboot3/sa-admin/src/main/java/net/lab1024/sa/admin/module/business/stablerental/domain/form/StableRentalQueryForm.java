package net.lab1024.sa.admin.module.business.stablerental.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

/**
 * 马房租赁查询表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class StableRentalQueryForm extends PageParam {

    @Schema(description = "租赁单号")
    private String rentalNo;

    @Schema(description = "出租人ID")
    private Long lessorId;

    @Schema(description = "租赁人ID")
    private Long lesseeId;

    @Schema(description = "租赁目标俱乐部ID")
    private Long targetClubId;

    @Schema(description = "租赁状态")
    private Integer rentalStatus;

    @Schema(description = "租赁开始时间-开始")
    private LocalDateTime rentalStartTimeBegin;

    @Schema(description = "租赁开始时间-结束")
    private LocalDateTime rentalStartTimeEnd;

    @Schema(description = "租赁结束时间-开始")
    private LocalDateTime rentalEndTimeBegin;

    @Schema(description = "租赁结束时间-结束")
    private LocalDateTime rentalEndTimeEnd;
}