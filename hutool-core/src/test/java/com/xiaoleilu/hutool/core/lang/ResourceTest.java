package com.xiaoleilu.hutool.core.lang;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.io.resource.ClassPathResource;
import com.xiaoleilu.hutool.util.CharsetUtil;

/**
 * 资源单元测试
 * @author Looly
 *
 */
public class ResourceTest {
	
	@Test
	public void ClassPathResourceTest() throws IOException{
		//测试读取junit的jar包下的LICENSE-junit.txt文件
		final ClassPathResource resource = new ClassPathResource("LICENSE-junit.txt");
		String result = IoUtil.read(resource.getStream(), CharsetUtil.CHARSET_UTF_8);
		Assert.assertNotNull(result);
	}
}
