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

package org.dromara.hutool.core.compress;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;

/**
 * Excel兼容的ZIP64 OutputStream实现<br>
 * 来自并见： https://github.com/rzymek/opczip
 *
 * @author rzymek
 */
public class OpcZipOutputStream extends ZipOutputStream {

	private final Zip64 spec;
	private final List<Zip64.Entry> entries = new ArrayList<>();
	private final CRC32 crc = new CRC32();
	private Zip64.Entry current;
	private int written = 0;
	private boolean finished = false;

	/**
	 * 构造
	 *
	 * @param out 写出压缩数据额输出流
	 */
	public OpcZipOutputStream(final OutputStream out) {
		super(out);
		this.spec = new Zip64(out);
	}

	@Override
	public void putNextEntry(final ZipEntry e) throws IOException {
		if (current != null) {
			closeEntry();
		}
		current = new Zip64.Entry(e.getName());
		current.offset = written;
		written += spec.writeLFH(current);
		entries.add(current);
	}

	@Override
	public void closeEntry() throws IOException {
		if (current == null) {
			throw new IllegalStateException("not current zip current");
		}
		def.finish();
		while (!def.finished()) {
			deflate();
		}

		current.size = def.getBytesRead();
		current.compressedSize = (int) def.getBytesWritten();
		current.crc = crc.getValue();

		written += current.compressedSize;
		written += spec.writeDAT(current);
		current = null;
		def.reset();
		crc.reset();
	}

	@Override
	public void finish() throws IOException {
		if (finished) {
			return;
		}
		if (current != null) {
			closeEntry();
		}
		final int offset = written;
		for (final Zip64.Entry entry : entries) {
			written += spec.writeCEN(entry);
		}
		written += spec.writeEND(entries.size(), offset, written - offset);
		finished = true;
	}

	/**
	 * @see ZipOutputStream#write(byte[], int, int)
	 */
	@Override
	public synchronized void write(final byte[] b, final int off, final int len) throws IOException {
		if (off < 0 || len < 0 || off > b.length - len) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		super.write(b, off, len);
		crc.update(b, off, len);
	}

	@Override
	public void close() throws IOException {
		finish();
		out.close();
	}
}
