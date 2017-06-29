package com.xiaoleilu.hutool.core.io;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.io.resource.ClassPathResource;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.CharsetUtil;

/**
 * ClassPath资源读取测试
 * @author Looly
 *
 */
public class ClassPathResourceTest {
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
		
		File file = resource.getFile();
		Console.log(file);
		
		String result = IoUtil.read(resource.getStream(), CharsetUtil.CHARSET_UTF_8);
		Assert.assertNotNull(result);
	}
}
