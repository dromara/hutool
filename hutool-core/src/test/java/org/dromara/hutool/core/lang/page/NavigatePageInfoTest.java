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

public class NavigatePageInfoTest {

	@Test
	public void naviTest1(){
		// 首页
		final NavigatePageInfo navigatePageInfo = new NavigatePageInfo(10, 2, 6);
		Assertions.assertEquals("[1] 2 3 4 5 >>", navigatePageInfo.toString());

		// 中间页
		navigatePageInfo.nextPage();
		Assertions.assertEquals("<< 1 [2] 3 4 5 >>", navigatePageInfo.toString());

		// 尾页
		navigatePageInfo.setPageNo(5);
		Assertions.assertEquals("<< 1 2 3 4 [5]", navigatePageInfo.toString());
	}

	@Test
	public void naviTest2(){
		// 首页
		final NavigatePageInfo navigatePageInfo = new NavigatePageInfo(10, 2, 4);
		Assertions.assertEquals("[1] 2 3 4 >>", navigatePageInfo.toString());

		// 中间页
		navigatePageInfo.nextPage();
		Assertions.assertEquals("<< 1 [2] 3 4 >>", navigatePageInfo.toString());

		// 尾页
		navigatePageInfo.setPageNo(5);
		Assertions.assertEquals("<< 2 3 4 [5]", navigatePageInfo.toString());
	}
}
