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

package org.dromara.hutool.core.bean;

import lombok.Data;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI80FP4Test {
	@Test
	public void copyPropertiesTest() {
		final Dest sourceDest = new Dest();
		sourceDest.setCPF(33699);
		sourceDest.setEnderDest("abc");

		final Dest dest = new Dest();
		final CopyOptions copyOptions = CopyOptions.of()
			.setIgnoreNullValue(true)
			.setIgnoreCase(true)
			.setIgnoreProperties("enderDest");

		BeanUtil.copyProperties(sourceDest, dest, copyOptions);
		Assertions.assertNull(dest.getEnderDest());
	}

	@Test
	public void copyPropertiesTest2() {
		final Dest sourceDest = new Dest();
		sourceDest.setCPF(33699);
		sourceDest.setEnderDest("abc");

		final Dest dest = new Dest();
		final CopyOptions copyOptions = CopyOptions.of()
			.setIgnoreNullValue(true)
			.setIgnoreCase(true)
			.setIgnoreProperties("enderdest");

		BeanUtil.copyProperties(sourceDest, dest, copyOptions);
		Assertions.assertNull(dest.getEnderDest());
	}

	@Data
	static class Dest{
		private int cPF;
		private String enderDest;
	}
}
