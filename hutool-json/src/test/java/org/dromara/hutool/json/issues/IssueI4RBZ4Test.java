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

/**
 * https://gitee.com/dromara/hutool/issues/I4RBZ4
 */
public class IssueI4RBZ4Test {

	@Test
	public void sortTest(){
		final String jsonStr = "{\"id\":\"123\",\"array\":[1,2,3],\"outNum\":356,\"body\":{\"ava1\":\"20220108\",\"use\":1,\"ava2\":\"20230108\"},\"name\":\"John\"}";

		final JSONObject jsonObject = JSONUtil.parseObj(jsonStr, JSONConfig.of().setNatureKeyComparator());
		Assertions.assertEquals("{\"array\":[1,2,3],\"body\":{\"ava1\":\"20220108\",\"ava2\":\"20230108\",\"use\":1},\"id\":\"123\",\"name\":\"John\",\"outNum\":356}", jsonObject.toString());
	}
}
