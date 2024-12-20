/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.http.useragent;

import org.dromara.hutool.core.text.StrUtil;

/**
 * User-Agent解析器
 *
 * @author looly
 * @since 4.2.1
 */
public class UserAgentParser {

	/**
	 * 解析User-Agent
	 *
	 * @param userAgentString User-Agent字符串
	 * @return {@link UserAgent}
	 */
	public static UserAgent parse(final String userAgentString) {
		if(StrUtil.isBlank(userAgentString)){
			return null;
		}
		final UserAgent userAgent = new UserAgent();

		// 浏览器
		final Browser browser = parseBrowser(userAgentString);
		userAgent.setBrowser(browser);
		userAgent.setVersion(browser.getVersion(userAgentString));

		// 浏览器引擎
		final BrowserEngine engine = parseEngine(userAgentString);
		userAgent.setEngine(engine);
		userAgent.setEngineVersion(engine.getVersion(userAgentString));

		// 操作系统
		final OS os = parseOS(userAgentString);
		userAgent.setOs(os);
		userAgent.setOsVersion(os.getVersion(userAgentString));

		// 平台
		final Platform platform = parsePlatform(userAgentString);
		userAgent.setPlatform(platform);

		// issue#IA74K2 MACOS下的微信不属于移动平台
		if(platform.isMobile() || browser.isMobile()){
			if(!os.isMacOS()){
				userAgent.setMobile(true);
			}
		}

		return userAgent;
	}

	/**
	 * 解析浏览器类型
	 *
	 * @param userAgentString User-Agent字符串
	 * @return 浏览器类型
	 */
	private static Browser parseBrowser(final String userAgentString) {
		for (final Browser browser : Browser.browers) {
			if (browser.isMatch(userAgentString)) {
				return browser;
			}
		}
		return Browser.Unknown;
	}

	/**
	 * 解析引擎类型
	 *
	 * @param userAgentString User-Agent字符串
	 * @return 引擎类型
	 */
	private static BrowserEngine parseEngine(final String userAgentString) {
		for (final BrowserEngine engine : BrowserEngine.engines) {
			if (engine.isMatch(userAgentString)) {
				return engine;
			}
		}
		return BrowserEngine.Unknown;
	}

	/**
	 * 解析系统类型
	 *
	 * @param userAgentString User-Agent字符串
	 * @return 系统类型
	 */
	private static OS parseOS(final String userAgentString) {
		for (final OS os : OS.oses) {
			if (os.isMatch(userAgentString)) {
				return os;
			}
		}
		return OS.Unknown;
	}

	/**
	 * 解析平台类型
	 *
	 * @param userAgentString User-Agent字符串
	 * @return 平台类型
	 */
	private static Platform parsePlatform(final String userAgentString) {
		for (final Platform platform : Platform.platforms) {
			if (platform.isMatch(userAgentString)) {
				return platform;
			}
		}
		return Platform.Unknown;
	}
}
