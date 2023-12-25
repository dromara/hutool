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
