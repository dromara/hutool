package cn.hutool.jwt;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;

import java.io.Serializable;

/**
 * Claims 认证，简单的JSONObject包装
 *
 * @author looly
 */
public class Claims implements Serializable {
	private static final long serialVersionUID = 1L;

	private final JSONObject claimJSON;

	public Claims() {
		this.claimJSON = new JSONObject();
	}

	/**
	 * 增加Claims属性，如果属性值为{@code null}，则移除这个属性
	 *
	 * @param name  属性名
	 * @param value 属性值
	 */
	protected void setClaim(String name, Object value) {
		Assert.notNull(name, "Name must be not null!");
		if (value == null) {
			claimJSON.remove(name);
			return;
		}
		claimJSON.set(name, value);
	}

	/**
	 * 获取Claims的JSON字符串形式
	 *
	 * @return JSON字符串
	 */
	public String getClaimsJson() {
		return this.claimJSON.toString();
	}
}
