package net.lab1024.sa.admin.module.business.stablerental.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 马房租赁详情VO
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class StableRentalDetailVO {

    @Schema(description = "租赁记录ID")
    private Long rentalId;

    @Schema(description = "租赁单号")
    private String rentalNo;

    @Schema(description = "出租人ID")
    private Long lessorId;

    @Schema(description = "出租人姓名")
    private String lessorName;

    @Schema(description = "租赁人ID")
    private Long lesseeId;

    @Schema(description = "租赁人姓名")
    private String lesseeName;

    @Schema(description = "租赁目标俱乐部ID")
    private Long targetClubId;

    @Schema(description = "俱乐部名称")
    private String clubName;

    @Schema(description = "租赁开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime rentalStartTime;

    @Schema(description = "租赁结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime rentalEndTime;

    @Schema(description = "租赁金额")
    private BigDecimal rentalAmount;

    @Schema(description = "租赁状态")
    private Integer rentalStatus;

    @Schema(description = "租赁状态描述")
    private String rentalStatusDesc;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}