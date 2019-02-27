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
	public void normalizeTest3() {
		String url = "http://www.hutool.cn//aaa/\\bbb?a=1&b=2";
		String normalize = URLUtil.normalize(url, true);
		Assert.assertEquals("http://www.hutool.cn/aaa/bbb?a=1&b=2", normalize);
		
		url = "www.hutool.cn//aaa/bbb?a=1&b=2";
		normalize = URLUtil.normalize(url, true);
		Assert.assertEquals("http://www.hutool.cn/aaa/bbb?a=1&b=2", normalize);
	}
	
	@Test
	public void formatTest() {
		String url = "//www.hutool.cn//aaa/\\bbb?a=1&b=2";
		String normalize = URLUtil.normalize(url);
		Assert.assertEquals("http://www.hutool.cn/aaa/bbb?a=1&b=2", normalize);
	}
	
	@Test
	public void encodeTest() {
		String body = "366466 - 副本.jpg";
		String encode = URLUtil.encode(body);
		Assert.assertEquals("366466%20-%20%E5%89%AF%E6%9C%AC.jpg", encode);
		Assert.assertEquals(body, URLUtil.decode(encode));
		
		String encode2 = URLUtil.encodeQuery(body);
		Assert.assertEquals("366466+-+%E5%89%AF%E6%9C%AC.jpg", encode2);
	}
}
