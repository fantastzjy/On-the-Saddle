package net.lab1024.sa.admin.module.business.booking.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 批量预约操作表单
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Schema(description = "批量预约操作表单")
public class BatchBookingForm {

    @Schema(description = "预约ID列表")
    private List<Long> bookingIds;
}