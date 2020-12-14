package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 分页单元测试
 * @author Looly
 *
 */
public class PageUtilTest {
	
	@Test
	public void transToStartEndTest(){
		PageUtil.setFirstPageNo(0);
		int[] startEnd1 = PageUtil.transToStartEnd(0, 10);
		Assert.assertEquals(0, startEnd1[0]);
		Assert.assertEquals(10, startEnd1[1]);
		
		int[] startEnd2 = PageUtil.transToStartEnd(1, 10);
		Assert.assertEquals(10, startEnd2[0]);
		Assert.assertEquals(20, startEnd2[1]);
	}
	
	@Test
	public void totalPage(){
		int totalPage = PageUtil.totalPage(20, 3);
		Assert.assertEquals(7, totalPage);
	}
	
	@Test
	public void rainbowTest() {
		int[] rainbow = PageUtil.rainbow(5, 20, 6);
		Assert.assertArrayEquals(new int[] {3, 4, 5, 6, 7, 8}, rainbow);
	}
}
