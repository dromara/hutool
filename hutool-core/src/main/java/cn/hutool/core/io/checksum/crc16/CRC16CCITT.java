package cn.hutool.core.io.checksum.crc16;

/**
 * CRC16_CCITT：多项式x16+x12+x5+1（0x1021），初始值0x0000，低位在前，高位在后，结果与0x0000异或
 * 0x8408是0x1021按位颠倒后的结果。
 *
 * @author looly
 * @since 5.3.10
 */
public class CRC16CCITT extends CRC16Checksum{

	private static final int WC_POLY = 0x8408;

	@Override
	public void update(int b) {
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
