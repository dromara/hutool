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
