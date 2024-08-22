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

package org.dromara.hutool.core.convert.impl.stringer;

import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.InputStream;
import java.sql.Blob;
import java.util.function.Function;

/**
 * Blob转String
 *
 * @author looly
 * @since 6.0.0
 */
public class BlobStringer implements Function<Object, String> {

	/**
	 * 单例
	 */
	public static ClobStringer INSTANCE = new ClobStringer();


	@Override
	public String apply(final Object o) {
		return blobToStr((Blob) o);
	}

	/**
	 * Blob字段值转字符串
	 *
	 * @param blob    {@link java.sql.Blob}
	 * @return 字符串
	 * @since 5.4.5
	 */
	private static String blobToStr(final java.sql.Blob blob) {
		InputStream in = null;
		try {
			in = blob.getBinaryStream();
			return IoUtil.read(in, CharsetUtil.UTF_8);
		} catch (final java.sql.SQLException e) {
			throw new ConvertException(e);
		} finally {
			IoUtil.closeQuietly(in);
		}
	}
}
