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

package org.dromara.hutool.core.io.checksum.crc16;

/**
 * CRC16_X25：多项式x16+x12+x5+1（0x1021），初始值0xffff，低位在前，高位在后，结果与0xFFFF异或
 * 0x8408是0x1021按位颠倒后的结果。
 *
 * @author looly
 * @since 5.3.10
 */
public class CRC16X25 extends CRC16Checksum{
	private static final long serialVersionUID = 1L;

	private static final int WC_POLY = 0x8408;

	@Override
	public void reset(){
		this.wCRCin = 0xffff;
	}

	@Override
	public void update(final byte[] b, final int off, final int len) {
		super.update(b, off, len);
		wCRCin ^= 0xffff;
	}

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
