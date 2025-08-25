package net.lab1024.sa.admin.module.business.report.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 马匹健康状态统计VO
 *
 * @Author 1024创新实验室
 * @Date 2024-12-07
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "马匹健康状态统计")
public class HorseHealthStatusVO {

    @Schema(description = "健康状态")
    private Integer healthStatus;

    @Schema(description = "健康状态名称")
    private String healthStatusName;

    @Schema(description = "马匹数量")
    private Integer horseCount;

    @Schema(description = "占比")
    private Double percentage;
}