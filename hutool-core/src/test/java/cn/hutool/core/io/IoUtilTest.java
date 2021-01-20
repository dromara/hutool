package cn.hutool.core.io;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.Assert;
import org.junit.Test;

public class IoUtilTest {

	@Test
	public void readBytesTest(){
		final byte[] bytes = IoUtil.readBytes(ResourceUtil.getStream("hutool.jpg"));
		Assert.assertEquals(22807, bytes.length);
	}
}
