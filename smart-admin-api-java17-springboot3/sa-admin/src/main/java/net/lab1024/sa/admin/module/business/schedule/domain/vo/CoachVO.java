package net.lab1024.sa.admin.module.business.schedule.domain.vo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 教练视图对象
 *
 * @Author 1024创新实验室
 * @Date 2024-08-17
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "教练视图对象")
public class CoachVO {

    @Schema(description = "教练ID")
    private Long coachId;

    @Schema(description = "教练编号")
    private String coachNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "教练姓名")
    private String coachName;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "专长领域")
    private String specialties;

    @Schema(description = "教练等级")
    private String coachLevel;

    @Schema(description = "入行时间")
    private String entryDate;

    @Schema(description = "是否有效")
    private Integer isValid;
}