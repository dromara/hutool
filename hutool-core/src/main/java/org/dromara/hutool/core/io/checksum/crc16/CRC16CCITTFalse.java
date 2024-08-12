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

package org.dromara.hutool.core.io.checksum.crc16;

/**
 * CRC16_CCITT_FALSE：多项式x16+x12+x5+1（0x1021），初始值0xFFFF，低位在后，高位在前，结果与0x0000异或
 *
 * @author looly
 * @since 5.3.10
 */
public class CRC16CCITTFalse extends CRC16Checksum{
	private static final long serialVersionUID = 1L;

	private static final int WC_POLY = 0x1021;

	@Override
	public void reset() {
		this.wCRCin = 0xffff;
	}

	@Override
	public void update(final byte[] b, final int off, final int len) {
		super.update(b, off, len);
		wCRCin &= 0xffff;
	}

	@Override
	public void update(final int b) {
		for (int i = 0; i < 8; i++) {
			final boolean bit = ((b >> (7 - i) & 1) == 1);
			final boolean c15 = ((wCRCin >> 15 & 1) == 1);
			wCRCin <<= 1;
			if (c15 ^ bit)
				wCRCin ^= WC_POLY;
		}
	}
}
