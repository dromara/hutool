/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.net.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * https 域名校验，信任所有域名<br>
 * 注意此类慎用，信任全部可能会有中间人攻击风险
 *
 * @author Looly
 */
public class TrustAnyHostnameVerifier implements HostnameVerifier {

	/**
	 * 单例对象
	 */
	public static final TrustAnyHostnameVerifier INSTANCE = new TrustAnyHostnameVerifier();

	@Override
	public boolean verify(final String hostname, final SSLSession session) {
		return true;// 直接返回true
	}
}
