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

package org.dromara.hutool.core.io.checksum.crc16;

/**
 * CRC16_IBM：多项式x16+x15+x2+1（0x8005），初始值0x0000，低位在前，高位在后，结果与0x0000异或
 * 0xA001是0x8005按位颠倒后的结果
 *
 * @author looly
 * @since 5.3.10
 */
public class CRC16IBM extends CRC16Checksum{
	private static final long serialVersionUID = 1L;

	private static final int WC_POLY = 0xa001;

	@Override
	public void update(final int b) {
		wCRCin ^= (b & 0x00ff);
		for (int j = 0; j < 8; j++) {
			if ((wCRCin & 0x0001) != 0) {
				wCRCin >>= 1;
				wCRCin ^= WC_POLY;
			} else {
				wCRCin >>= 1;
			}
		}
	}
}
