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

package org.dromara.hutool.core.convert.impl.stringer;

import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.io.IoUtil;

import java.io.Reader;
import java.sql.Clob;
import java.util.function.Function;

/**
 * Clob转String
 *
 * @author looly
 * @since 6.0.0
 */
public class ClobStringer implements Function<Object, String> {

	/**
	 * 单例
	 */
	public static ClobStringer INSTANCE = new ClobStringer();

	@Override
	public String apply(final Object o) {
		return clobToStr((Clob) o);
	}

	/**
	 * Clob字段值转字符串
	 *
	 * @param clob {@link java.sql.Clob}
	 * @return 字符串
	 * @since 5.4.5
	 */
	private static String clobToStr(final Clob clob) {
		Reader reader = null;
		try {
			reader = clob.getCharacterStream();
			return IoUtil.read(reader);
		} catch (final java.sql.SQLException e) {
			throw new ConvertException(e);
		} finally {
			IoUtil.closeQuietly(reader);
		}
	}
}
