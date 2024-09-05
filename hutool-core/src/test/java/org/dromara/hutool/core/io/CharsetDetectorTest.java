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

package org.dromara.hutool.core.io;

import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

public class CharsetDetectorTest {

	@Test
	public void detectTest(){
		// 测试多个Charset对同一个流的处理是否有问题
		final Charset detect = CharsetDetector.detect(ResourceUtil.getStream("test.xml"),
				CharsetUtil.GBK, CharsetUtil.UTF_8);
		Assertions.assertEquals(CharsetUtil.UTF_8, detect);
	}
}
