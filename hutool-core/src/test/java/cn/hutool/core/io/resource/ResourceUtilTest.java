package cn.hutool.core.io.resource;

import cn.hutool.core.io.IoUtil;
import org.junit.Assert;
import org.junit.Test;

public class ResourceUtilTest {

	@Test
	public void readXmlTest(){
		final String str = ResourceUtil.readUtf8Str("test.xml");
		Assert.assertNotNull(str);
	}

	@Test
	public void stringResourceTest(){
		final StringResource stringResource = new StringResource("testData", "test");
		Assert.assertEquals("test", stringResource.getName());
		Assert.assertArrayEquals("testData".getBytes(), stringResource.readBytes());
		Assert.assertArrayEquals("testData".getBytes(), IoUtil.readBytes(stringResource.getStream()));
	}
}
