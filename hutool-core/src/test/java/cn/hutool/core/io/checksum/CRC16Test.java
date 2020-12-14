package cn.hutool.core.io.checksum;

import cn.hutool.core.io.checksum.crc16.CRC16Ansi;
import cn.hutool.core.io.checksum.crc16.CRC16CCITT;
import cn.hutool.core.io.checksum.crc16.CRC16CCITTFalse;
import cn.hutool.core.io.checksum.crc16.CRC16DNP;
import cn.hutool.core.io.checksum.crc16.CRC16IBM;
import cn.hutool.core.io.checksum.crc16.CRC16Maxim;
import cn.hutool.core.io.checksum.crc16.CRC16Modbus;
import cn.hutool.core.io.checksum.crc16.CRC16USB;
import cn.hutool.core.io.checksum.crc16.CRC16X25;
import cn.hutool.core.io.checksum.crc16.CRC16XModem;
import org.junit.Assert;
import org.junit.Test;

public class CRC16Test {

	private final String data = "QN=20160801085857223;ST=23;CN=2011;PW=123456;MN=010000A8900016F000169DC0;Flag=5;CP=&&DataTime=20160801085857; LA-Rtd=50.1&&";

	@Test
	public void ccittTest(){
		final CRC16CCITT crc16 = new CRC16CCITT();
		crc16.update(data.getBytes());
		Assert.assertEquals("c852", crc16.getHexValue());
	}

	@Test
	public void ccittFalseTest(){
		final CRC16CCITTFalse crc16 = new CRC16CCITTFalse();
		crc16.update(data.getBytes());
		Assert.assertEquals("a5e4", crc16.getHexValue());
	}

	@Test
	public void xmodemTest(){
		final CRC16XModem crc16 = new CRC16XModem();
		crc16.update(data.getBytes());
		Assert.assertEquals("5a8d", crc16.getHexValue());
	}

	@Test
	public void x25Test(){
		final CRC16X25 crc16 = new CRC16X25();
		crc16.update(data.getBytes());
		Assert.assertEquals("a152", crc16.getHexValue());
	}

	@Test
	public void modbusTest(){
		final CRC16Modbus crc16 = new CRC16Modbus();
		crc16.update(data.getBytes());
		Assert.assertEquals("25fb", crc16.getHexValue());
	}

	@Test
	public void ibmTest(){
		final CRC16IBM crc16 = new CRC16IBM();
		crc16.update(data.getBytes());
		Assert.assertEquals("18c", crc16.getHexValue());
	}

	@Test
	public void maximTest(){
		final CRC16Maxim crc16 = new CRC16Maxim();
		crc16.update(data.getBytes());
		Assert.assertEquals("fe73", crc16.getHexValue());
	}

	@Test
	public void usbTest(){
		final CRC16USB crc16 = new CRC16USB();
		crc16.update(data.getBytes());
		Assert.assertEquals("da04", crc16.getHexValue());
	}

	@Test
	public void dnpTest(){
		final CRC16DNP crc16 = new CRC16DNP();
		crc16.update(data.getBytes());
		Assert.assertEquals("3d1a", crc16.getHexValue());
	}

	@Test
	public void ansiTest(){
		final CRC16Ansi crc16 = new CRC16Ansi();
		crc16.update(data.getBytes());
		Assert.assertEquals("1e00", crc16.getHexValue());

		crc16.reset();
		String str2 = "QN=20160801085857223;ST=32;CN=1062;PW=100000;MN=010000A8900016F000169DC0;Flag=5;CP=&&RtdInterval=30&&";
		crc16.update(str2.getBytes());
		Assert.assertEquals("1c80", crc16.getHexValue());
	}
}
