package cn.hutool.core.io.checksum.crc16;

/**
 * CRC16_DNP：多项式x16+x13+x12+x11+x10+x8+x6+x5+x2+1（0x3D65），初始值0x0000，低位在前，高位在后，结果与0xFFFF异或
 * 0xA6BC是0x3D65按位颠倒后的结果
 *
 * @author looly
 * @since 5.3.10
 */
public class CRC16DNP extends CRC16Checksum{

	private static final int WC_POLY = 0xA6BC;

	@Override
	public void update(byte[] b, int off, int len) {
		super.update(b, off, len);
		wCRCin ^= 0xffff;
	}

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
