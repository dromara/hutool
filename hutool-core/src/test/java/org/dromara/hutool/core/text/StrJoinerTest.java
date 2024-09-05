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

package org.dromara.hutool.core.text;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class StrJoinerTest {

	@Test
	public void joinIntArrayTest(){
		final int[] a = {1,2,3,4,5};
		final StrJoiner append = StrJoiner.of(",").append(a);
		Assertions.assertEquals("1,2,3,4,5", append.toString());
	}

	@Test
	public void joinEmptyTest(){
		final List<String> list = new ArrayList<>();
		final StrJoiner append = StrJoiner.of(",").append(list);
		Assertions.assertEquals("", append.toString());
	}

	@Test
	public void noJoinTest(){
		final StrJoiner append = StrJoiner.of(",");
		Assertions.assertEquals("", append.toString());
	}

	@Test
	public void joinMultiArrayTest(){
		final StrJoiner append = StrJoiner.of(",");
		append.append(new Object[]{ListUtil.view("1", "2"),
				SetUtil.ofLinked("3", "4")
		});
		Assertions.assertEquals("1,2,3,4", append.toString());
	}

	@Test
	public void joinNullModeTest(){
		StrJoiner append = StrJoiner.of(",")
				.setNullMode(StrJoiner.NullMode.IGNORE)
				.append("1")
				.append((Object)null)
				.append("3");
		Assertions.assertEquals("1,3", append.toString());

		append = StrJoiner.of(",")
				.setNullMode(StrJoiner.NullMode.TO_EMPTY)
				.append("1")
				.append((Object)null)
				.append("3");
		Assertions.assertEquals("1,,3", append.toString());

		append = StrJoiner.of(",")
				.setNullMode(StrJoiner.NullMode.NULL_STRING)
				.append("1")
				.append((Object)null)
				.append("3");
		Assertions.assertEquals("1,null,3", append.toString());
	}

	@Test
	public void joinWrapTest(){
		StrJoiner append = StrJoiner.of(",", "[", "]")
				.append("1")
				.append("2")
				.append("3");
		Assertions.assertEquals("[1,2,3]", append.toString());

		append = StrJoiner.of(",", "[", "]")
				.setWrapElement(true)
				.append("1")
				.append("2")
				.append("3");
		Assertions.assertEquals("[1],[2],[3]", append.toString());
	}

	@Test
	public void lengthTest(){
		final StrJoiner joiner = StrJoiner.of(",", "[", "]");
		Assertions.assertEquals(joiner.toString().length(), joiner.length());

		joiner.append("123");
		Assertions.assertEquals(joiner.toString().length(), joiner.length());
	}

	@Test
	public void mergeTest(){
		final StrJoiner joiner1 = StrJoiner.of(",", "[", "]");
		joiner1.append("123");
		final StrJoiner joiner2 = StrJoiner.of(",", "[", "]");
		joiner1.append("456");
		joiner1.append("789");

		final StrJoiner merge = joiner1.merge(joiner2);
		Assertions.assertEquals("[123,456,789]", merge.toString());
	}

	@Test
	void issue3444Test() {
		final StrJoiner strJoinerEmpty = StrJoiner.of(",");
		Assertions.assertEquals(0, strJoinerEmpty.length());

		final StrJoiner strJoinerWithContent = StrJoiner.of(",").append("haha");
		Assertions.assertEquals(4, strJoinerWithContent.length());
	}
}
