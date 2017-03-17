package com.xiaoleilu.hutool.core.io;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import com.xiaoleilu.hutool.io.resource.ClassPathResource;
import com.xiaoleilu.hutool.lang.Console;

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
		
		Console.log("Properties: {}", properties);
	}
}
