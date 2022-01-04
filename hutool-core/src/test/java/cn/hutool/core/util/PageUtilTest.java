package cn.hutool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		Assertions.assertEquals(0, startEnd1[0]);
		Assertions.assertEquals(10, startEnd1[1]);

		int[] startEnd2 = PageUtil.transToStartEnd(1, 10);
		Assertions.assertEquals(10, startEnd2[0]);
		Assertions.assertEquals(20, startEnd2[1]);
	}

	@Test
	public void totalPage(){
		int totalPage = PageUtil.totalPage(20, 3);
		Assertions.assertEquals(7, totalPage);
	}

	@Test
	public void rainbowTest() {
		int[] rainbow = PageUtil.rainbow(5, 20, 6);
		Assertions.assertArrayEquals(new int[] {3, 4, 5, 6, 7, 8}, rainbow);
	}
}
