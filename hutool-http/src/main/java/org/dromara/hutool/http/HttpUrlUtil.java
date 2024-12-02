/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.http;

import org.dromara.hutool.core.net.url.UrlBuilder;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;

import java.util.List;

public class HttpUrlUtil {
	/**
	 * 获取转发的新的URL
	 *
	 * @param parentUrl 上级请求的URL
	 * @param location  获取的Location
	 * @return 新的URL
	 */
	public static UrlBuilder getLocationUrl(final UrlBuilder parentUrl, String location) {
		final UrlBuilder redirectUrl;
		if (!HttpUtil.isHttp(location) && !HttpUtil.isHttps(location)) {
			// issue#I5TPSY
			// location可能为相对路径
			if (!location.startsWith("/")) {
				location = StrUtil.addSuffixIfNot(parentUrl.getPathStr(), "/") + location;
			}

			// issue#3265, 相对路径中可能存在参数，单独处理参数
			final String query;
			final List<String> split = SplitUtil.split(location, "?", 2, true, true);
			if (split.size() == 2) {
				// 存在参数
				location = split.get(0);
				query = split.get(1);
			} else {
				query = null;
			}

			redirectUrl = UrlBuilder.of(parentUrl.getScheme(), parentUrl.getHost(), parentUrl.getPort(),
				location, query, null, parentUrl.getCharset());
		} else {
			// location已经是编码过的URL
			redirectUrl = UrlBuilder.ofHttpWithoutEncode(location);
		}

		return redirectUrl;
	}
}
