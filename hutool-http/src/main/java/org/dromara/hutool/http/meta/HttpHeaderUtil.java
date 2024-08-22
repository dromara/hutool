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

package org.dromara.hutool.http.meta;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.map.CaseInsensitiveMap;
import org.dromara.hutool.core.net.url.UrlDecoder;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * HTTP头相关方法<br>
 * 相关规范见：https://www.rfc-editor.org/rfc/rfc5987
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
	 * 从Content-Disposition头中获取文件名。<br>
	 * 参考标准：https://datatracker.ietf.org/doc/html/rfc6266#section-4.1<br>
	 * 以参数名为`filename`为例，规则为：
	 * <ul>
	 *     <li>首先按照RFC5987规范检查`filename*`参数对应的值，即：`filename*="example.txt"`，则获取`example.txt`</li>
	 *     <li>如果找不到`filename*`参数，则检查`filename`参数对应的值，即：`filename="example.txt"`，则获取`example.txt`</li>
	 * </ul>
	 * 按照规范，`Content-Disposition`可能返回多个，此处遍历所有返回头，并且`filename*`始终优先获取，即使`filename`存在并更靠前。<br>
	 * 参考：https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Content-Disposition
	 *
	 * @param headers   头列表
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
		// 正则转义
		paramName = StrUtil.replace(paramName, "*", "\\*");
		String fileName = null;
		for (final String disposition : dispositions) {
			fileName = ReUtil.getGroup1(paramName + "\\s*=\\s*([^;]+)", disposition);
			if (StrUtil.isNotBlank(fileName)) {
				break;
			}
		}
		return getRfc5987Value(fileName);
	}

	/**
	 * 获取rfc5987标准的值，标准见：https://www.rfc-editor.org/rfc/rfc5987#section-3.2.1<br>
	 * 包括：
	 *
	 * <ul>
	 *     <li>Non-extended：无双引号包裹的值</li>
	 *     <li>Non-extended：双引号包裹的值</li>
	 *     <li>Extended notation：编码'语言'值</li>
	 * </ul>
	 *
	 * @param value 值
	 * @return 结果值
	 */
	private static String getRfc5987Value(final String value) {
		return ExtendedValue.of(value).getDecodeValue();
	}

	/**
	 * 根据rfc5987的扩展值，格式为：
	 * <pre>{@code
	 *     编码'语言'值
	 * }</pre>
	 */
	public static class ExtendedValue implements Serializable {
		private static final long serialVersionUID = 1L;

		/**
		 * 创建扩展值
		 *
		 * @param value 扩展值或普通值
		 * @return 扩展值
		 */
		public static ExtendedValue of(final String value) {
			return new ExtendedValue(value);
		}

		private Charset charset;
		private String language;
		private final String value;

		/**
		 * 构造
		 *
		 * @param value 扩展值或普通值
		 */
		public ExtendedValue(final String value) {
			final List<String> split = SplitUtil.split(value, "'");
			if (3 == split.size()) {
				this.charset = CharsetUtil.charset(split.get(0));
				this.language = split.get(1);
				this.value = split.get(2);
			} else {
				this.value = StrUtil.unWrap(value, '"');
				;
			}
		}

		/**
		 * 获取编码，无定义为{@code null}
		 *
		 * @return 编码，无定义为{@code null}
		 */
		public Charset getCharset() {
			return charset;
		}

		/**
		 * 获取语言
		 *
		 * @return 语言
		 */
		public String getLanguage() {
			return language;
		}

		/**
		 * 获取原始值
		 *
		 * @return 原始值
		 */
		public String getValue() {
			return value;
		}

		/**
		 * 获取解码后的值
		 *
		 * @return 解码后的值
		 */
		public String getDecodeValue() {
			// 非application/x-www-form-urlencoded环境+都不做解码
			return UrlDecoder.decodeForPath(value, charset);
		}
	}
}
