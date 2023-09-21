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

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.date.StopWatch;
import org.dromara.hutool.core.lang.Console;
import lombok.Data;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Issue2649Test {

	@Test
	@Disabled
	public void toListTest() {
		final List<View1> view1List = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			final View1 view1 = new View1();
			view1.setA(String.valueOf(i));
			view1.setB(String.valueOf(i));
			for (int j = 0; j < 2; j++) {
				final View2 view2 = new View2();
				view1.setA(String.valueOf(i));
				view1.setB(String.valueOf(i));
				view1.getViewList().add(view2);
			}
			view1List.add(view1);
		}

		final StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		for (int i = 0; i < 50; i++) {
			@SuppressWarnings("unused")
			final List<View2> view2s = BeanUtil.copyToList(view1List, View2.class);
		}
		stopWatch.stop();
		Console.log(stopWatch.getTotalTimeSeconds());
	}

	@Data
	static class View1{
		private String a;
		private String b;
		private List<View2> viewList = new ArrayList<>();
	}

	@Data
	static class View2{
		private String a;
		private String b;
		private List<View2> viewList = new ArrayList<>();
	}
}
