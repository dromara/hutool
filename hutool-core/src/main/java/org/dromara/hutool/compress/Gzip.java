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

package org.dromara.hutool.compress;

import org.dromara.hutool.io.IORuntimeException;
import org.dromara.hutool.io.IoUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP是用于Unix系统的文件压缩<br>
 * gzip的基础是DEFLATE
 *
 * @author looly
 * @since 5.7.8
 */
public class Gzip implements Closeable {

	private InputStream source;
	private OutputStream target;

	/**
	 * 创建Gzip
	 *
	 * @param source 源流
	 * @param target 目标流
	 * @return Gzip
	 */
	public static Gzip of(final InputStream source, final OutputStream target) {
		return new Gzip(source, target);
	}

	/**
	 * 构造
	 *
	 * @param source 源流
	 * @param target 目标流
	 */
	public Gzip(final InputStream source, final OutputStream target) {
		this.source = source;
		this.target = target;
	}

	/**
	 * 获取目标流
	 *
	 * @return 目标流
	 */
	public OutputStream getTarget() {
		return this.target;
	}

	/**
	 * 将普通数据流压缩
	 *
	 * @return Gzip
	 */
	public Gzip gzip() {
		try {
			target = (target instanceof GZIPOutputStream) ?
					(GZIPOutputStream) target : new GZIPOutputStream(target);
			IoUtil.copy(source, target);
			((GZIPOutputStream) target).finish();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 将压缩流解压到target中
	 *
	 * @return Gzip
	 */
	public Gzip unGzip() {
		try {
			source = (source instanceof GZIPInputStream) ?
					(GZIPInputStream) source : new GZIPInputStream(source);
			IoUtil.copy(source, target);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(this.target);
		IoUtil.closeQuietly(this.source);
	}
}
