package org.dromara.hutool.lang.page;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PageInfoTest {
	@Test
	public void pagesTest() {
		PageInfo pageInfo = new PageInfo(20, 3);
		Assertions.assertEquals(7, pageInfo.getPageCount());

		pageInfo = new PageInfo(20, 4);
		Assertions.assertEquals(5, pageInfo.getPageCount());
	}

	@Test
	public void getSegmentTest() {
		final PageInfo page = PageInfo.of(20, 10);
		Assertions.assertEquals("[0, 9]", page.getSegment().toString());
		Assertions.assertEquals("[10, 19]", page.nextPage().getSegment().toString());
		Assertions.assertEquals("[20, 20]", page.nextPage().getSegment().toString());
	}

	@Test
	public void getSegmentTest2() {
		final PageInfo page = PageInfo.of(20, 10);
		page.setFirstPageNo(0).setPageNo(0);
		Assertions.assertEquals("[0, 9]", page.getSegment().toString());
		Assertions.assertEquals("[10, 19]", page.nextPage().getSegment().toString());
		Assertions.assertEquals("[20, 20]", page.nextPage().getSegment().toString());
	}
}
