package org.dromara.hutool.net;

import org.dromara.hutool.net.url.URLUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * URLUtil单元测试
 *
 * @author looly
 *
 */
public class URLUtilTest {

	@Test
	public void normalizeTest() {
		// issue#I25MZL，多个/被允许
		String url = "http://www.hutool.cn//aaa/bbb";
		String normalize = URLUtil.normalize(url);
		Assertions.assertEquals("http://www.hutool.cn//aaa/bbb", normalize);

		url = "www.hutool.cn//aaa/bbb";
		normalize = URLUtil.normalize(url);
		Assertions.assertEquals("http://www.hutool.cn//aaa/bbb", normalize);
	}

	@Test
	public void normalizeTest2() {
		String url = "http://www.hutool.cn//aaa/\\bbb?a=1&b=2";
		String normalize = URLUtil.normalize(url);
		Assertions.assertEquals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);

		url = "www.hutool.cn//aaa/bbb?a=1&b=2";
		normalize = URLUtil.normalize(url);
		Assertions.assertEquals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);
	}

	@Test
	public void normalizeTest3() {
		String url = "http://www.hutool.cn//aaa/\\bbb?a=1&b=2";
		String normalize = URLUtil.normalize(url, true);
		Assertions.assertEquals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);

		url = "www.hutool.cn//aaa/bbb?a=1&b=2";
		normalize = URLUtil.normalize(url, true);
		Assertions.assertEquals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);

		url = "\\/www.hutool.cn//aaa/bbb?a=1&b=2";
		normalize = URLUtil.normalize(url, true);
		Assertions.assertEquals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);
	}

	@Test
	public void normalizeIpv6Test() {
		final String url = "http://[fe80::8f8:2022:a603:d180]:9439";
		final String normalize = URLUtil.normalize("http://[fe80::8f8:2022:a603:d180]:9439", true);
		Assertions.assertEquals(url, normalize);
	}

	@Test
	public void formatTest() {
		final String url = "//www.hutool.cn//aaa/\\bbb?a=1&b=2";
		final String normalize = URLUtil.normalize(url);
		Assertions.assertEquals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);
	}

	@Test
	public void getHostTest() throws MalformedURLException {
		final String url = "https://www.hutool.cn//aaa/\\bbb?a=1&b=2";
		final String normalize = URLUtil.normalize(url);
		final URI host = URLUtil.getHost(new URL(normalize));
		Assertions.assertEquals("https://www.hutool.cn", host.toString());
	}

	@Test
	public void getPathTest(){
		final String url = " http://www.aaa.bbb/search?scope=ccc&q=ddd";
		final String path = URLUtil.getPath(url);
		Assertions.assertEquals("/search", path);
	}
}
