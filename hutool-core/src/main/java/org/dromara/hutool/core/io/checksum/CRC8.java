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

package org.dromara.hutool.core.io.checksum;

import java.io.Serializable;
import java.util.zip.Checksum;

/**
 * CRC8 循环冗余校验码（Cyclic Redundancy Check）实现<br>
 * 代码来自：<a href="https://github.com/BBSc0der">https://github.com/BBSc0der</a>
 *
 * @author Bolek, Looly
 * @since 4.4.1
 */
public class CRC8 implements Checksum, Serializable {
	private static final long serialVersionUID = 1L;

	private final short init;
	private final short[] crcTable = new short[256];
	private short value;

	/**
	 * 构造<br>
	 *
	 * @param polynomial Polynomial, typically one of the POLYNOMIAL_* constants.
	 * @param init Initial value, typically either 0xff or zero.
	 */
	public CRC8(final int polynomial, final short init) {
		this.value = this.init = init;
		for (int dividend = 0; dividend < 256; dividend++) {
			int remainder = dividend;// << 8;
			for (int bit = 0; bit < 8; ++bit) {
				if ((remainder & 0x01) != 0) {
					remainder = (remainder >>> 1) ^ polynomial;
				} else {
					remainder >>>= 1;
				}
			}
			crcTable[dividend] = (short) remainder;
		}
	}

	@Override
	public void update(final byte[] buffer, final int offset, final int len) {
		for (int i = 0; i < len; i++) {
			final int data = buffer[offset + i] ^ value;
			value = (short) (crcTable[data & 0xff] ^ (value << 8));
		}
	}

	/**
	 * Updates the current checksum with the specified array of bytes. Equivalent to calling {@code update(buffer, 0, buffer.length)}.
	 *
	 * @param buffer the byte array to update the checksum with
	 */
	public void update(final byte[] buffer) {
		update(buffer, 0, buffer.length);
	}

	@Override
	public void update(final int b) {
		update(new byte[] { (byte) b }, 0, 1);
	}

	@Override
	public long getValue() {
		return value & 0xff;
	}

	@Override
	public void reset() {
		value = init;
	}
}
