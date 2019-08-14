package cn.hutool.core.io.checksum;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.util.HexUtil;

/**
 * CRC校验单元测试
 * 
 * @author looly
 *
 */
public class CrcTest {

	@Test
	public void crc8Test() {
		final int CRC_POLYNOM = 0x9C;
		final byte CRC_INITIAL = (byte) 0xFF;

		final byte[] data = { 1, 56, -23, 3, 0, 19, 0, 0, 2, 0, 3, 13, 8, -34, 7, 9, 42, 18, 26, -5, 54, 11, -94, //
				-46, -128, 4, 48, 52, 0, 0, 0, 0, 0, 0, 0, 0, 4, 1, 1, -32, -80, 0, 98, -5, 71, 0, 64, 0, 0, 0, 0, -116, 1, 104, 2 };
		CRC8 crc8 = new CRC8(CRC_POLYNOM, CRC_INITIAL);
		crc8.update(data, 0, data.length);
		Assert.assertEquals(29, crc8.getValue());
	}

	@Test
	public void crc16Test() {
		CRC16 crc = new CRC16();
		crc.update(12);
		crc.update(16);
		Assert.assertEquals("cc04", HexUtil.toHex(crc.getValue()));
	}
}
