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

package org.dromara.hutool.core.lang.ansi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnsiEncoderTest {

	@Test
	public void encodeTest(){
		final String encode = AnsiEncoder.encode(Ansi4BitColor.GREEN, "Hutool test");
		Assertions.assertEquals("\u001B[32mHutool test\u001B[0;39m", encode);
	}
}
