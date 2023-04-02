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

package org.dromara.hutool.http.useragent;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.regex.ReUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 浏览器引擎对象
 *
 * @author looly
 * @since 4.2.1
 */
public class BrowserEngine extends UserAgentInfo {
	private static final long serialVersionUID = 1L;

	/**
	 * 未知
	 */
	public static final BrowserEngine Unknown = new BrowserEngine(NameUnknown, null);

	/**
	 * 支持的引擎类型
	 */
	public static final List<BrowserEngine> engines = ListUtil.view(
			new BrowserEngine("Trident", "trident"),
			new BrowserEngine("Webkit", "webkit"),
			new BrowserEngine("Chrome", "chrome"),
			new BrowserEngine("Opera", "opera"),
			new BrowserEngine("Presto", "presto"),
			new BrowserEngine("Gecko", "gecko"),
			new BrowserEngine("KHTML", "khtml"),
			new BrowserEngine("Konqueror", "konqueror"),
			new BrowserEngine("MIDP", "MIDP")
	);

	private final Pattern versionPattern;

	/**
	 * 构造
	 *
	 * @param name  引擎名称
	 * @param regex 关键字或表达式
	 */
	public BrowserEngine(final String name, final String regex) {
		super(name, regex);
		this.versionPattern = Pattern.compile(name + "[/\\- ]([\\w.\\-]+)", Pattern.CASE_INSENSITIVE);
	}

	/**
	 * 获取引擎版本
	 *
	 * @param userAgentString User-Agent字符串
	 * @return 版本
	 * @since 5.7.4
	 */
	public String getVersion(final String userAgentString) {
		if (isUnknown()) {
			return null;
		}
		return ReUtil.getGroup1(this.versionPattern, userAgentString);
	}
}
