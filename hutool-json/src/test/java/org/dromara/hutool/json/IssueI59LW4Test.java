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

package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI59LW4Test {
	@Test
	public void bytesTest(){
		final JSONObject jsonObject = JSONUtil.ofObj().set("bytes", new byte[]{1});
		Assertions.assertEquals("{\"bytes\":[1]}", jsonObject.toString());

		final byte[] bytes = jsonObject.getBytes("bytes");
		Assertions.assertArrayEquals(new byte[]{1}, bytes);
	}

	@Test
	public void bytesInJSONArrayTest(){
		final JSONArray jsonArray = JSONUtil.ofArray().set(new byte[]{1});
		Assertions.assertEquals("[[1]]", jsonArray.toString());

		final byte[] bytes = jsonArray.getBytes(0);
		Assertions.assertArrayEquals(new byte[]{1}, bytes);
	}
}
