package net.lab1024.sa.base.enums;

/**
 * 俱乐部类型枚举
 *
 * @Author Claude Code
 * @Date 2025-08-27
 * @Copyright 马术俱乐部管理系统
 */
public enum ClubTypeEnum {

	STANDARD(1, "俱乐部"),
	COMPETITIVE_STABLE(2, "竞技马房");

	private final Integer code;
	private final String desc;

	ClubTypeEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * 根据code获取枚举
	 *
	 * @param code 编码
	 * @return 枚举值
	 */
	public static ClubTypeEnum getByCode(Integer code) {
		if (code == null) {
			return null;
		}
		for (ClubTypeEnum type : values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		return null;
	}
}
