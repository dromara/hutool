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
