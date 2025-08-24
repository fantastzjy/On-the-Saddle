package net.lab1024.sa.admin.module.business.member.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员小程序登录响应VO
 *
 */
@Data
@Schema(description = "会员小程序登录响应")
public class MemberAppLoginVO {

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "会员编号")
    private String memberNo;
}
