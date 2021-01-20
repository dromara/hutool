package cn.hutool.core.io;

import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;

/**
 * ClassPath资源读取测试
 * @author Looly
 *
 */
public class ClassPathResourceTest {
	
	@Test
	public void readStringTest() throws IOException{
		ClassPathResource resource = new ClassPathResource("test.properties");
		String content = resource.readUtf8Str();
		Assert.assertTrue(StrUtil.isNotEmpty(content));
	}
	
	@Test
	public void readStringTest2() throws IOException{
		ClassPathResource resource = new ClassPathResource("/");
		String content = resource.readUtf8Str();
		Assert.assertTrue(StrUtil.isNotEmpty(content));
	}
	
	@Test
	public void readTest() throws IOException{
		ClassPathResource resource = new ClassPathResource("test.properties");
		Properties properties = new Properties();
		properties.load(resource.getStream());
		
		Assert.assertEquals("1", properties.get("a"));
		Assert.assertEquals("2", properties.get("b"));
	}
	
	@Test
	public void readFromJarTest() throws IOException{
		//测试读取junit的jar包下的LICENSE-junit.txt文件
		final ClassPathResource resource = new ClassPathResource("LICENSE-junit.txt");
		
		String result = resource.readUtf8Str();
		Assert.assertNotNull(result);
		
		//二次读取测试，用于测试关闭流对再次读取的影响
		result = resource.readUtf8Str();
		Assert.assertNotNull(result);
	}
	
	@Test
	public void getAbsTest() {
		final ClassPathResource resource = new ClassPathResource("LICENSE-junit.txt");
		String absPath = resource.getAbsolutePath();
		Assert.assertTrue(absPath.contains("LICENSE-junit.txt"));
	}
}
