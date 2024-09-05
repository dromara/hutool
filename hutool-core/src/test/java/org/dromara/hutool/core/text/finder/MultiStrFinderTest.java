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

package org.dromara.hutool.core.text.finder;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author cmm
 * @date 2024/8/12 11:16
 */
public class MultiStrFinderTest {

	@Test
	public void test1(){
		ArrayList<String> strings = new ArrayList<>();
		strings.add("sha");
		strings.add("asa");
		strings.add("ha");
		strings.add("hex");
		MultiStrFinder finder = MultiStrFinder.of(strings);
		String text = "asdasahhxxeshaaahexaaasa";
		Map<String, List<Integer>> match = finder.findMatch(text);
		System.out.println(text);
		match.forEach((k,v) -> {
			System.out.println(k + ":" + v);
		});

	}


	@Test
	public void test2(){
		ArrayList<String> strings = new ArrayList<>();
		strings.add("沙漠");
		strings.add("撒");
		strings.add("哈");
		strings.add("害克斯");
		MultiStrFinder finder = MultiStrFinder.of(strings);
		String text = "撒哈拉大沙漠，你看哈哈哈。hex码中文写成海克斯科技";
		Map<String, List<Integer>> match = finder.findMatch(text);
		System.out.println(text);
		match.forEach((k,v) -> {
			System.out.println(k + ":" + v);
		});

	}

}
