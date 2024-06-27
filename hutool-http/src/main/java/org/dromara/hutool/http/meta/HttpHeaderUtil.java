/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.meta;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.map.CaseInsensitiveMap;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.util.List;
import java.util.Map;

/**
 * HTTP头相关方法
 *
 * @author Looly
 * @since 6.0.0
 */
public class HttpHeaderUtil {

	/**
	 * 根据name获取对应的头信息列表
	 *
	 * @param headers 头列表
	 * @param name    Header名
	 * @return Header值
	 */
	public static List<String> headerList(final Map<String, List<String>> headers, final String name) {
		if (StrUtil.isBlank(name)) {
			return null;
		}

		final CaseInsensitiveMap<String, List<String>> headersIgnoreCase = new CaseInsensitiveMap<>(headers);
		return headersIgnoreCase.get(name.trim());
	}

	/**
	 * 从Content-Disposition头中获取文件名，以参数名为`filename`为例，规则为：
	 * <ul>
	 *     <li>首先按照RFC5987规范检查`filename*`参数对应的值，即：`filename*="example.txt"`，则获取`example.txt`</li>
	 *     <li>如果找不到`filename*`参数，则检查`filename`参数对应的值，即：`filename="example.txt"`，则获取`example.txt`</li>
	 * </ul>
	 * 按照规范，`Content-Disposition`可能返回多个，此处遍历所有返回头，并且`filename*`始终优先获取，即使`filename`存在并更靠前。<br>
	 * 参考：https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Content-Disposition
	 *
	 * @param headers 头列表
	 * @param paramName 文件参数名，如果为{@code null}则使用默认的`filename`
	 * @return 文件名，empty表示无
	 */
	public static String getFileNameFromDisposition(final Map<String, List<String>> headers, String paramName) {
		paramName = ObjUtil.defaultIfNull(paramName, "filename");
		final List<String> dispositions = headerList(headers, HeaderName.CONTENT_DISPOSITION.getValue());
		String fileName = null;
		if (CollUtil.isNotEmpty(dispositions)) {

			// filename* 采用了 RFC 5987 中规定的编码方式，优先读取
			fileName = getFileNameFromDispositions(dispositions, StrUtil.addSuffixIfNot(paramName, "*"));
			if ((!StrUtil.endWith(fileName, "*")) && StrUtil.isBlank(fileName)) {
				fileName = getFileNameFromDispositions(dispositions, paramName);
			}
		}

		return fileName;
	}

	/**
	 * 从Content-Disposition头中获取文件名
	 *
	 * @param dispositions Content-Disposition头列表
	 * @param paramName    文件参数名
	 * @return 文件名，empty表示无
	 */
	private static String getFileNameFromDispositions(final List<String> dispositions, String paramName) {
		String fileName = null;
		for (final String disposition : dispositions) {
			fileName = ReUtil.getGroup1(paramName + "=\"(.*?)\"", disposition);
			if (StrUtil.isNotBlank(fileName)) {
				break;
			}
		}
		return fileName;
	}
}
