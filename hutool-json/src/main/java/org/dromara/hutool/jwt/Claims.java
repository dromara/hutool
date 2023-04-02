/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.jwt;

import org.dromara.hutool.JSONConfig;
import org.dromara.hutool.JSONObject;
import org.dromara.hutool.JSONUtil;
import org.dromara.hutool.codec.binary.Base64;
import org.dromara.hutool.date.format.GlobalCustomFormat;
import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.map.MapUtil;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Claims 认证，简单的JSONObject包装
 *
 * @author looly
 * @since 5.7.0
 */
public class Claims implements Serializable {
	private static final long serialVersionUID = 1L;

	// 时间使用秒级时间戳表示
	private final JSONConfig CONFIG = JSONConfig.of().setDateFormat(GlobalCustomFormat.FORMAT_SECONDS);

	private JSONObject claimJSON;

	/**
	 * 增加Claims属性，如果属性值为{@code null}，则移除这个属性
	 *
	 * @param name  属性名
	 * @param value 属性值
	 */
	protected void setClaim(final String name, final Object value) {
		Assert.notNull(name, "Name must be not null!");
		init();
		if (value == null) {
			claimJSON.remove(name);
			return;
		}
		claimJSON.set(name, value);
	}

	/**
	 * 加入多个Claims属性
	 * @param headerClaims 多个Claims属性
	 */
	protected void putAll(final Map<String, ?> headerClaims){
		if (MapUtil.isNotEmpty(headerClaims)) {
			for (final Map.Entry<String, ?> entry : headerClaims.entrySet()) {
				setClaim(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * 获取指定名称属性
	 *
	 * @param name 名称
	 * @return 属性
	 */
	public Object getClaim(final String name) {
		init();
		return this.claimJSON.getObj(name);
	}

	/**
	 * 获取Claims的JSON字符串形式
	 *
	 * @return JSON字符串
	 */
	public JSONObject getClaimsJson() {
		init();
		return this.claimJSON;
	}

	/**
	 * 解析JWT JSON
	 *
	 * @param tokenPart JWT JSON
	 * @param charset   编码
	 */
	public void parse(final String tokenPart, final Charset charset) {
		this.claimJSON = JSONUtil.parseObj(Base64.decodeStr(tokenPart, charset), CONFIG);
	}

	@Override
	public String toString() {
		init();
		return this.claimJSON.toString();
	}

	private void init(){
		if(null == this.claimJSON){
			this.claimJSON = new JSONObject(CONFIG);
		}
	}
}
