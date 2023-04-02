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

package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;

/**
 * Deflate算法<br>
 * Deflate是同时使用了LZ77算法与哈夫曼编码（Huffman Coding）的一个无损数据压缩算法。
 *
 * @author looly
 * @since 5.7.8
 */
public class Deflate implements Closeable {

	private final InputStream source;
	private OutputStream target;
	private final boolean nowrap;

	/**
	 * 创建Deflate
	 *
	 * @param source 源流
	 * @param target 目标流
	 * @param nowrap {@code true}表示兼容Gzip压缩
	 * @return this
	 */
	public static Deflate of(final InputStream source, final OutputStream target, final boolean nowrap) {
		return new Deflate(source, target, nowrap);
	}

	/**
	 * 构造
	 *
	 * @param source 源流
	 * @param target 目标流
	 * @param nowrap {@code true}表示兼容Gzip压缩
	 */
	public Deflate(final InputStream source, final OutputStream target, final boolean nowrap) {
		this.source = source;
		this.target = target;
		this.nowrap = nowrap;
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
	 * @param level 压缩级别，0~9
	 * @return this
	 */
	public Deflate deflater(final int level) {
		target= (target instanceof DeflaterOutputStream) ?
				(DeflaterOutputStream) target : new DeflaterOutputStream(target, new Deflater(level, nowrap));
		IoUtil.copy(source, target);
		try {
			((DeflaterOutputStream)target).finish();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 将压缩流解压到target中
	 *
	 * @return this
	 */
	public Deflate inflater() {
		target = (target instanceof InflaterOutputStream) ?
				(InflaterOutputStream) target : new InflaterOutputStream(target, new Inflater(nowrap));
		IoUtil.copy(source, target);
		try {
			((InflaterOutputStream)target).finish();
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
