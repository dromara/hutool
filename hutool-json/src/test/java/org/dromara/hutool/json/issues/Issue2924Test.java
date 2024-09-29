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

package org.dromara.hutool.json.issues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Issue2924Test {

	@Test
	public void toListTest(){
		final String idsJsonString = "[1174,137,1172,210,1173,627,628]";
		final List<Integer> idList = JSONUtil.toList(idsJsonString,Integer.class);
		Assertions.assertEquals("[1174, 137, 1172, 210, 1173, 627, 628]", idList.toString());
	}
}
