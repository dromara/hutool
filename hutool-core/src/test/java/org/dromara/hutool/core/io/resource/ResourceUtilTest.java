package org.dromara.hutool.core.io.resource;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResourceUtilTest {

	@Test
	public void readXmlTest(){
		final String str = ResourceUtil.readUtf8Str("test.xml");
		Assertions.assertNotNull(str);

		final Resource resource = new ClassPathResource("test.xml");
		final String xmlStr = resource.readUtf8Str();

		Assertions.assertEquals(str, xmlStr);
	}

	@Test
	public void stringResourceTest(){
		final StringResource stringResource = new StringResource("testData", "test");
		Assertions.assertEquals("test", stringResource.getName());
		Assertions.assertArrayEquals("testData".getBytes(), stringResource.readBytes());
		Assertions.assertArrayEquals("testData".getBytes(), IoUtil.readBytes(stringResource.getStream()));
	}

	@Test
	public void fileResourceTest(){
		final FileResource resource = new FileResource(FileUtil.file("test.xml"));
		Assertions.assertEquals("test.xml", resource.getName());
		Assertions.assertTrue(StrUtil.isNotEmpty(resource.readUtf8Str()));
	}
}
