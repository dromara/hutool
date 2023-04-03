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

package org.dromara.hutool.core.io.stream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 限制读取最大长度的{@link FilterInputStream} 实现<br>
 * 来自：https://github.com/skylot/jadx/blob/master/jadx-plugins/jadx-plugins-api/src/main/java/jadx/api/plugins/utils/LimitedInputStream.java
 *
 * @author jadx
 */
public class LimitedInputStream extends FilterInputStream {

	private final long maxSize;
	private long currentPos;

	/**
	 * 构造
	 *
	 * @param in      {@link InputStream}
	 * @param maxSize 限制最大读取量，单位byte
	 */
	public LimitedInputStream(final InputStream in, final long maxSize) {
		super(in);
		this.maxSize = maxSize;
	}

	@Override
	public int read() throws IOException {
		final int data = super.read();
		if (data != -1) {
			currentPos++;
			checkPos();
		}
		return data;
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public int read(final byte[] b, final int off, final int len) throws IOException {
		final int count = super.read(b, off, len);
		if (count > 0) {
			currentPos += count;
			checkPos();
		}
		return count;
	}

	@Override
	public long skip(final long n) throws IOException {
		final long skipped = super.skip(n);
		if (skipped != 0) {
			currentPos += skipped;
			checkPos();
		}
		return skipped;
	}

	private void checkPos() {
		if (currentPos > maxSize) {
			throw new IllegalStateException("Read limit exceeded");
		}
	}
}
