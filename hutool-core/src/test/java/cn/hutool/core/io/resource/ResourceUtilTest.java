package cn.hutool.core.io.resource;

import org.junit.Assert;
import org.junit.Test;

public class ResourceUtilTest {

	@Test
	public void readXmlTest(){
		final String str = ResourceUtil.readUtf8Str("test.xml");
		Assert.assertNotNull(str);
	}
}
