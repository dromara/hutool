/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
