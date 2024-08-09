package cn.hutool.core.io.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ResourceUtilTest {

	@Test
	public void readXmlTest(){
		final String str = ResourceUtil.readUtf8Str("test.xml");
		assertNotNull(str);

		Resource resource = new ClassPathResource("test.xml");
		final String xmlStr = resource.readUtf8Str();

		assertEquals(str, xmlStr);
	}

	@Test
	public void stringResourceTest(){
		final StringResource stringResource = new StringResource("testData", "test");
		assertEquals("test", stringResource.getName());
		assertArrayEquals("testData".getBytes(), stringResource.readBytes());
		assertArrayEquals("testData".getBytes(), IoUtil.readBytes(stringResource.getStream()));
	}

	@Test
	public void fileResourceTest(){
		final FileResource resource = new FileResource(FileUtil.file("test.xml"));
		assertEquals("test.xml", resource.getName());
		assertTrue(StrUtil.isNotEmpty(resource.readUtf8Str()));
	}
}
