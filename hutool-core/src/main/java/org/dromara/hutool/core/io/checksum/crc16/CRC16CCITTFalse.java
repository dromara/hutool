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
