package cn.hutool.core.io;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassPath资源读取测试
 *
 * @author Looly
 */
public class ClassPathResourceTest {

	@Test
	public void readStringTest() {
		ClassPathResource resource = new ClassPathResource("test.properties");
		String content = resource.readUtf8Str();
		assertTrue(StrUtil.isNotEmpty(content));
	}

	@Test
	public void readStringTest2() {
		// 读取classpath根目录测试
		ClassPathResource resource = new ClassPathResource("/");
		String content = resource.readUtf8Str();
		assertTrue(StrUtil.isNotEmpty(content));
	}

	@Test
	public void readTest() throws IOException {
		ClassPathResource resource = new ClassPathResource("test.properties");
		Properties properties = new Properties();
		properties.load(resource.getStream());

		assertEquals("1", properties.get("a"));
		assertEquals("2", properties.get("b"));
	}

	@Test
	public void readFromJarTest() {
		//测试读取junit的jar包下的LICENSE.md文件
		final ClassPathResource resource = new ClassPathResource("META-INF/LICENSE.md");

		String result = resource.readUtf8Str();
		assertNotNull(result);

		//二次读取测试，用于测试关闭流对再次读取的影响
		result = resource.readUtf8Str();
		assertNotNull(result);
	}

	@Test
	public void getAbsTest() {
		// lombok 文件
		final ClassPathResource resource = new ClassPathResource("changelog.txt");
		String absPath = resource.getAbsolutePath();
		assertTrue(absPath.contains("changelog.txt"));
	}
}
