package cn.hutool.core.io.checksum.crc16;

/**
 * CRC16_ANSI
 *
 * @author looly
 * @since 5.3.10
 */
public class CRC16Ansi extends CRC16Checksum{

	private static final int WC_POLY = 0xa001;

	@Override
	public void reset() {
		this.wCRCin = 0xffff;
	}

	@Override
	public void update(int b) {
		int hi = wCRCin >> 8;
		hi ^= b;
		wCRCin = hi;

		for (int i = 0; i < 8; i++) {
			int flag = wCRCin & 0x0001;
			wCRCin = wCRCin >> 1;
			if (flag == 1) {
				wCRCin ^= WC_POLY;
			}
		}
	}
}
