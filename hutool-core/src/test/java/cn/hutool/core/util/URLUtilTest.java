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
}
