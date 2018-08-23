package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * URLUtil单元测试
 * 
 * @author looly
 *
 */
public class URLUtilTest {

	@Test
	public void normalizeTest() {
		String url = "http://www.hutool.cn//aaa/bbb";
		String normalize = URLUtil.normalize(url);
		Assert.assertEquals("http://www.hutool.cn/aaa/bbb", normalize);

		url = "www.hutool.cn//aaa/bbb";
		normalize = URLUtil.normalize(url);
		Assert.assertEquals("http://www.hutool.cn/aaa/bbb", normalize);
	}
	
	@Test
	public void normalizeTest2() {
		String url = "http://www.hutool.cn//aaa/\\bbb?a=1&b=2";
		String normalize = URLUtil.normalize(url);
		Assert.assertEquals("http://www.hutool.cn/aaa/bbb?a=1&b=2", normalize);
		
		url = "www.hutool.cn//aaa/bbb?a=1&b=2";
		normalize = URLUtil.normalize(url);
		Assert.assertEquals("http://www.hutool.cn/aaa/bbb?a=1&b=2", normalize);
	}
	
	@Test
	public void formatTest() {
		String url = "//www.hutool.cn//aaa/\\bbb?a=1&b=2";
		String normalize = URLUtil.formatUrl(url);
		Assert.assertEquals("http://www.hutool.cn/aaa/bbb?a=1&b=2", normalize);
	}
}
