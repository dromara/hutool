package cn.hutool.core.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * 分页单元测试
 *
 * @author Looly
 */
public class PageUtilTest {

	@Test
	public void transToStartEndTest() {
		final int[] startEnd1 = PageUtil.transToStartEnd(0, 10);
		assertEquals(0, startEnd1[0]);
		assertEquals(10, startEnd1[1]);

		final int[] startEnd2 = PageUtil.transToStartEnd(1, 10);
		assertEquals(10, startEnd2[0]);
		assertEquals(20, startEnd2[1]);
	}

	@Test
	public void totalPage() {
		final int totalPage = PageUtil.totalPage(20, 3);
		assertEquals(7, totalPage);
	}

	@Test
	public void rainbowTest() {
		final int[] rainbow = PageUtil.rainbow(5, 20, 6);
		assertArrayEquals(new int[]{3, 4, 5, 6, 7, 8}, rainbow);
	}
}
