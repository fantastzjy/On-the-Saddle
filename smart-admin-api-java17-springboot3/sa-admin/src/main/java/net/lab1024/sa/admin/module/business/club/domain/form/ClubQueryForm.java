package net.lab1024.sa.admin.module.business.club.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * 俱乐部查询表单
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class ClubQueryForm extends PageParam {

    @Schema(description = "关键字(俱乐部名称、联系人、电话)")
    private String keywords;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "区县")
    private String district;

    @Schema(description = "创建开始日期")
    private String startDate;

    @Schema(description = "创建结束日期")
    private String endDate;

    @Schema(description = "是否删除")
    private Boolean isDelete;

    @Schema(description = "是否有效")
    private Boolean isValid;
}
