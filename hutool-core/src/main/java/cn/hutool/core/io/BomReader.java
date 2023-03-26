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

package cn.hutool.core.io;

import cn.hutool.core.io.stream.BOMInputStream;
import cn.hutool.core.lang.Assert;

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
