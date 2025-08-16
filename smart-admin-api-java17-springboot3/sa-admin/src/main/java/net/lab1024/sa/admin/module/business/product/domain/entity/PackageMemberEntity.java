package net.lab1024.sa.admin.module.business.product.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 课时包用户关联表实体
 *
 * @Author 1024创新实验室
 * @Date 2024-08-16
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_package_member")
public class PackageMemberEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @DataTracerFieldLabel("订单明细ID")
    private Long orderItemId;

    @DataTracerFieldLabel("课时包ID")
    private Long packageId;

    @DataTracerFieldLabel("会员ID")
    private Long memberId;

    @DataTracerFieldLabel("剩余课时数")
    private Integer remainingSessions;

    @DataTracerFieldLabel("到期日期")
    private LocalDate expireDate;

    @DataTracerFieldLabel("绑定教练ID")
    private Long coachId;

    @DataTracerFieldLabel("状态")
    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}