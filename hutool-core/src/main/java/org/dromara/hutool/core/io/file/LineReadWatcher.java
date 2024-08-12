/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.core.io.file;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.watch.watchers.SimpleWatcher;
import org.dromara.hutool.core.func.SerConsumer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

/**
 * 行处理的Watcher实现
 *
 * @author looly
 * @since 4.5.2
 */
public class LineReadWatcher extends SimpleWatcher implements Runnable {

	private final RandomAccessFile randomAccessFile;
	private final Charset charset;
	private final SerConsumer<String> lineHandler;

	/**
	 * 构造
	 *
	 * @param randomAccessFile {@link RandomAccessFile}
	 * @param charset 编码
	 * @param lineHandler 行处理器{@link SerConsumer}实现
	 */
	public LineReadWatcher(final RandomAccessFile randomAccessFile, final Charset charset, final SerConsumer<String> lineHandler) {
		this.randomAccessFile = randomAccessFile;
		this.charset = charset;
		this.lineHandler = lineHandler;
	}

	@Override
	public void run() {
		onModify(null, null);
	}

	@Override
	public void onModify(final WatchEvent<?> event, final WatchKey key) {
		final RandomAccessFile randomAccessFile = this.randomAccessFile;
		final Charset charset = this.charset;
		final SerConsumer<String> lineHandler = this.lineHandler;

		try {
			final long currentLength = randomAccessFile.length();
			final long position = randomAccessFile.getFilePointer();
			if (position == currentLength) {
				// 内容长度不变时忽略此次事件
				return;
			} else if (currentLength < position) {
				// 如果内容变短或变0，说明文件做了删改或清空，回到内容末尾或0
				randomAccessFile.seek(currentLength);
				return;
			}

			// 读取行
			FileUtil.readLines(randomAccessFile, charset, lineHandler);

			// 记录当前读到的位置
			randomAccessFile.seek(currentLength);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
