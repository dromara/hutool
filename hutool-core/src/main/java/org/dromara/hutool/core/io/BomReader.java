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

package org.dromara.hutool.core.io;

import org.dromara.hutool.core.io.stream.BOMInputStream;
import org.dromara.hutool.core.lang.Assert;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 读取带BOM头的流内容的Reader，如果非bom的流或无法识别的编码，则默认UTF-8<br>
 * BOM定义：http://www.unicode.org/unicode/faq/utf_bom.html
 *
 * <ul>
 * <li>00 00 FE FF = UTF-32, big-endian</li>
 * <li>FF FE 00 00 = UTF-32, little-endian</li>
 * <li>EF BB BF = UTF-8</li>
 * <li>FE FF = UTF-16, big-endian</li>
 * <li>FF FE = UTF-16, little-endian</li>
 * </ul>
 * 使用： <br>
 * <code>
 * FileInputStream fis = new FileInputStream(file); <br>
 * BomReader uin = new BomReader(fis); <br>
 * </code>
 *
 * @author looly
 * @since 5.7.14
 */
public class BomReader extends ReaderWrapper {

	/**
	 * 构造
	 *
	 * @param in 流
	 */
	public BomReader(final InputStream in) {
		super(initReader(in));
	}

	/**
	 * 初始化为{@link InputStreamReader}，将给定流转换为{@link BOMInputStream}
	 *
	 * @param in {@link InputStream}
	 * @return {@link InputStreamReader}
	 */
	private static InputStreamReader initReader(final InputStream in) {
		Assert.notNull(in, "InputStream must be not null!");
		final BOMInputStream bin = (in instanceof BOMInputStream) ? (BOMInputStream) in : new BOMInputStream(in);
		try {
			return new InputStreamReader(bin, bin.getCharset());
		} catch (final UnsupportedEncodingException e) {
			throw new IORuntimeException(e);
		}
	}
}
