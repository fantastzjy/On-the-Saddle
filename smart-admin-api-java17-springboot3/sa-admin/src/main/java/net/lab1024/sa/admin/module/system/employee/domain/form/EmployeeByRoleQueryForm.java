package net.lab1024.sa.admin.module.system.employee.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

/**
 * 按角色查询员工表单
 *
 * @Author Claude Code
 * @Date 2025-01-19
 */
@Data
public class EmployeeByRoleQueryForm extends PageParam {

    @Schema(description = "搜索词")
    @Length(max = 20, message = "搜索词最多20字符")
    private String keyword;

    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    @Schema(description = "是否禁用")
    private Boolean disabledFlag;

    @Schema(description = "删除标识", hidden = true)
    private Boolean deletedFlag;
}
