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
		
//		firstLetter = PinyinUtil.getFirstLetter('怡');
//		Console.log(firstLetter);
//		Assert.assertEquals('y', firstLetter);
	}
	
	@Test
	public void getAllFirstLetterTest() {
		String allFirstLetter = PinyinUtil.getAllFirstLetter("会当凌绝顶");
		Assert.assertEquals("hdljd", allFirstLetter);
		
		allFirstLetter = PinyinUtil.getAllFirstLetter("一览众山小");
		Assert.assertEquals("ylzsx", allFirstLetter);
	}
	
	@Test
	public void getAllFirstLetterTest2() {
		String allFirstLetter = PinyinUtil.getAllFirstLetter("张三123");
		Assert.assertEquals("zs123", allFirstLetter);
	}
	
	@Test
	public void getPinyinTest() {
		String pinYin = PinyinUtil.getPinYin("会当凌绝顶");
		Assert.assertEquals("huidanglingjueding", pinYin);
		
		pinYin = PinyinUtil.getPinYin("一览众山小");
		Assert.assertEquals("yilanzhongshanxiao", pinYin);
		
//		pinYin = PinyinUtil.getPinYin("怡");
//		Assert.assertEquals("yi", pinYin);
		
		String cnStr = "中文(进一步说明)-（OK）@gitee.com";
		pinYin = PinyinUtil.getPinYin(cnStr);
		Assert.assertEquals("zhongwen(jinyibushuoming)-OK@gitee.com", pinYin);
	}
}
