/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang.page;

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
