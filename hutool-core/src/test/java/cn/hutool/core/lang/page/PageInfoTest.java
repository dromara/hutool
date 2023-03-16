package cn.hutool.core.lang.page;

import org.junit.Assert;
import org.junit.Test;

public class PageInfoTest {
	@Test
	public void pagesTest() {
		PageInfo pageInfo = new PageInfo(20, 3);
		Assert.assertEquals(7, pageInfo.getPageCount());

		pageInfo = new PageInfo(20, 4);
		Assert.assertEquals(5, pageInfo.getPageCount());
	}

	@Test
	public void getSegmentTest() {
		final PageInfo page = PageInfo.of(20, 10);
		Assert.assertEquals("[0, 9]", page.getSegment().toString());
		Assert.assertEquals("[10, 19]", page.nextPage().getSegment().toString());
		Assert.assertEquals("[20, 20]", page.nextPage().getSegment().toString());
	}

	@Test
	public void getSegmentTest2() {
		final PageInfo page = PageInfo.of(20, 10);
		page.setFirstPageNo(0).setPageNo(0);
		Assert.assertEquals("[0, 9]", page.getSegment().toString());
		Assert.assertEquals("[10, 19]", page.nextPage().getSegment().toString());
		Assert.assertEquals("[20, 20]", page.nextPage().getSegment().toString());
	}
}
