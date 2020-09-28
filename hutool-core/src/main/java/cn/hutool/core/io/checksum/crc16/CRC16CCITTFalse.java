package cn.hutool.core.io.checksum.crc16;

/**
 * CRC16_CCITT_FALSE：多项式x16+x12+x5+1（0x1021），初始值0xFFFF，低位在后，高位在前，结果与0x0000异或
 *
 * @author looly
 * @since 5.3.10
 */
public class CRC16CCITTFalse extends CRC16Checksum{

	private static final int WC_POLY = 0x1021;

	@Override
	public void reset() {
		this.wCRCin = 0xffff;
	}

	@Override
	public void update(byte[] b, int off, int len) {
		super.update(b, off, len);
		wCRCin &= 0xffff;
	}

	@Override
	public void update(int b) {
		for (int i = 0; i < 8; i++) {
			boolean bit = ((b >> (7 - i) & 1) == 1);
			boolean c15 = ((wCRCin >> 15 & 1) == 1);
			wCRCin <<= 1;
			if (c15 ^ bit)
				wCRCin ^= WC_POLY;
		}
	}
}
