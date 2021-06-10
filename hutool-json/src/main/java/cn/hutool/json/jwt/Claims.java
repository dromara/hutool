package cn.hutool.json.jwt;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Claims 认证
 *
 * @author looly
 */
public class Claims implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Map<String, Object> claimMap;

	public Claims() {
		this.claimMap = new HashMap<>();
	}

	/**
	 * 增加Claims属性
	 *
	 * @param name  属性名
	 * @param value 属性值
	 */
	protected void setClaim(String name, Object value) {
		Assert.notNull(name, "Name must be not null!");
		if (value == null) {
			claimMap.remove(name);
			return;
		}
		claimMap.put(name, value);
	}

	/**
	 * 获取Claims数据Map
	 *
	 * @return map
	 */
	protected Map<String, Object> getClaimMap() {
		return this.claimMap;
	}

	/**
	 * 获取Claims的JSON字符串形式
	 *
	 * @return JSON字符串
	 */
	public String getClaimsJson() {
		return JSONUtil.toJsonStr(getClaimMap());
	}
}
