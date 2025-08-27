package net.lab1024.sa.admin.module.business.stablerental.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 马房租赁实体
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("m_stable_rental")
public class StableRentalEntity {

    @TableId(type = IdType.AUTO)
    private Long rentalId;

    @DataTracerFieldLabel("租赁单号")
    private String rentalNo;

    @DataTracerFieldLabel("出租人ID")
    private Long lessorId;

    @DataTracerFieldLabel("租赁人ID")
    private Long lesseeId;

    @DataTracerFieldLabel("租赁目标俱乐部ID")
    private Long targetClubId;

    @DataTracerFieldLabel("租赁开始时间")
    private LocalDateTime rentalStartTime;

    @DataTracerFieldLabel("租赁结束时间")
    private LocalDateTime rentalEndTime;

    @DataTracerFieldLabel("租赁金额")
    private BigDecimal rentalAmount;

    @DataTracerFieldLabel("租赁状态")
    private Integer rentalStatus;

    @DataTracerFieldLabel("备注")
    private String remark;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Integer isValid;

    private Integer isDelete;
}