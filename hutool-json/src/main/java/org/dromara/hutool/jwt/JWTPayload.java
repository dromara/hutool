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

import java.util.Map;

/**
 * JWT载荷信息<br>
 * 载荷就是存放有效信息的地方。这个名字像是特指飞机上承载的货品，这些有效信息包含三个部分:
 *
 * <ul>
 *     <li>标准中注册的声明</li>
 *     <li>公共的声明</li>
 *     <li>私有的声明</li>
 * </ul>
 * <p>
 * 详细介绍见：<a href="https://www.jianshu.com/p/576dbf44b2ae">https://www.jianshu.com/p/576dbf44b2ae</a>
 *
 * @author looly
 * @since 5.7.0
 */
public class JWTPayload extends Claims implements RegisteredPayload<JWTPayload>{
	private static final long serialVersionUID = 1L;

	/**
	 * 增加自定义JWT认证载荷信息
	 *
	 * @param payloadClaims 载荷信息
	 * @return this
	 */
	public JWTPayload addPayloads(final Map<String, ?> payloadClaims) {
		putAll(payloadClaims);
		return this;
	}

	@Override
	public JWTPayload setPayload(final String name, final Object value) {
		setClaim(name, value);
		return this;
	}
}
