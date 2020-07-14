package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * {@link Crc16Util}单元测试
 * @author wangjian
 *
 */
public class Crc16UtilTest {
	
	
	@Test
	public void crc16Test() {
	    String str = "QN=20160801085857223;ST=23;CN=2011;PW=123456;MN=010000A8900016F000169DC0;Flag=5;CP=&&DataTime=20160801085857; LA-Rtd=50.1&&";
		String crc16 = Crc16Util.crc16(str);
		Assert.assertEquals("1E00", crc16);
		
        String str2 = "QN=20160801085857223;ST=32;CN=1062;PW=100000;MN=010000A8900016F000169DC0;Flag=5;CP=&&RtdInterval=30&&";
        String crc16String = Crc16Util.crc16(str2);
        Assert.assertEquals("1C80", crc16String);		
	}
	
}
