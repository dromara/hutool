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
