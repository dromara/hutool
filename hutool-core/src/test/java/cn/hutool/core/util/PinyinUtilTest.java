package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

public class PinyinUtilTest {
	
	@Test
	public void getFirstLetterTest() {
		char firstLetter = PinyinUtil.getFirstLetter('你');
		Assert.assertEquals('n', firstLetter);
		
		firstLetter = PinyinUtil.getFirstLetter('我');
		Assert.assertEquals('w', firstLetter);
	}
	
	@Test
	public void getAllFirstLetterTest() {
		String allFirstLetter = PinyinUtil.getAllFirstLetter("会当凌绝顶");
		Assert.assertEquals("hdljd", allFirstLetter);
		
		allFirstLetter = PinyinUtil.getAllFirstLetter("一览众山小");
		Assert.assertEquals("ylzsx", allFirstLetter);
	}
}
