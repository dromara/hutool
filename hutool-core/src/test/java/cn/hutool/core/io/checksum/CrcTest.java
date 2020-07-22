package cn.hutool.core.io.checksum;

import cn.hutool.core.util.HexUtil;
import org.junit.Assert;
import org.junit.Test;

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

	@Test
	public void crc16Test2() {
		String str = "QN=20160801085857223;ST=23;CN=2011;PW=123456;MN=010000A8900016F000169DC0;Flag=5;CP=&&DataTime=20160801085857; LA-Rtd=50.1&&";
		CRC16 crc = new CRC16();
		crc.update(str.getBytes(), 0, str.getBytes().length);
		String crc16 = HexUtil.toHex(crc.getValue());
		Assert.assertEquals("18c", crc16);
	}
}
