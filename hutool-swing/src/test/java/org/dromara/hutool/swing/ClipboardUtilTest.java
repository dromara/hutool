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

package org.dromara.hutool.swing;

import org.dromara.hutool.swing.clipboard.ClipboardUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 剪贴板工具类单元测试
 *
 * @author looly
 *
 */
public class ClipboardUtilTest {

	@Test
	public void setAndGetStrTest() {
		try {
			ClipboardUtil.setStr("test");

			final String test = ClipboardUtil.getStr();
			Assertions.assertEquals("test", test);
		} catch (final java.awt.HeadlessException e) {
			// 忽略 No X11 DISPLAY variable was set, but this program performed an operation which requires it.
			// ignore
		}
	}
}
