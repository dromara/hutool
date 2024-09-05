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

package org.dromara.hutool.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author 温良恭
 * @Date 2024/8/21 22:04
 */
public class BitStatusUtilTest {


	@Test
	public void test() {
		int states = 0;
		states = BitStatusUtil.add(states, 2);   // 添加“已读”状态
		states  = BitStatusUtil.add(states, 4);   // 添加“已写”状态
//		此时，states 的值为 6，二进制为 110，这表示同时具有“已读”和“已写”状态
		boolean hasRead = BitStatusUtil.has(states, 2);  // 检查“已读”状态
		boolean hasWrite = BitStatusUtil.has(states, 4);  // 检查“已写”状态
		Assertions.assertEquals(true, hasRead);
		Assertions.assertEquals(true, hasWrite);
	}
}
