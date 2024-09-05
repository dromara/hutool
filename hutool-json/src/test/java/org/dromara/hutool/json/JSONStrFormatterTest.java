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

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * JSON字符串格式化单元测试
 * @author looly
 *
 */
public class JSONStrFormatterTest {

	@Test
	public void formatTest() {
		final String json = "{'age':23,'aihao':['pashan','movies'],'name':{'firstName':'zhang','lastName':'san','aihao':['pashan','movies','name':{'firstName':'zhang','lastName':'san','aihao':['pashan','movies']}]}}";
		final String result = JSONStrFormatter.INSTANCE.format(json);
		Assertions.assertNotNull(result);
	}

	@Test
	public void formatTest2() {
		final String json = "{\"abc\":{\"def\":\"\\\"[ghi]\"}}";
		final String result = JSONStrFormatter.INSTANCE.format(json);
		Assertions.assertNotNull(result);
	}

	@Test
	public void formatTest3() {
		final String json = "{\"id\":13,\"title\":\"《标题》\",\"subtitle\":\"副标题z'c'z'xv'c'xv\",\"user_id\":6,\"type\":0}";
		final String result = JSONStrFormatter.INSTANCE.format(json);
		Assertions.assertNotNull(result);
	}

	@Test
	@Disabled
	public void formatTest4(){
		final String jsonStr = "{\"employees\":[{\"firstName\":\"Bill\",\"lastName\":\"Gates\"},{\"firstName\":\"George\",\"lastName\":\"Bush\"},{\"firstName\":\"Thomas\",\"lastName\":\"Carter\"}]}";
		Console.log(JSONStrFormatter.INSTANCE.format(jsonStr));
	}
}
