/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
