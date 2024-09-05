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

package org.dromara.hutool.db;

import org.dromara.hutool.db.sql.QuoteWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author bwcx_jzy
 * @since 24/3/28 028
 */
public class QuoteWrapperTest {

	@Test
	@Disabled
	public void test() {
		final QuoteWrapper wrapper = new QuoteWrapper('`');
		final String originalName = "name";
		final String wrapName = wrapper.wrap(originalName);
		final String unWrapName = wrapper.unWrap(wrapName);
		Assertions.assertEquals(unWrapName, originalName);
	}

	@Test
	@Disabled
	public void testDotWrap() {
		final QuoteWrapper wrapper = new QuoteWrapper('`');
		final String originalName = "name.age";
		final String wrapName = wrapper.wrap(originalName);
		final String unWrapName = wrapper.unWrap(wrapName);
		Assertions.assertEquals(unWrapName, originalName);
	}

	@Test
	@Disabled
	public void testError() {
		final QuoteWrapper wrapper = new QuoteWrapper('`');
		final String originalName = "name.age*";
		final String wrapName = wrapper.wrap(originalName);
		final String unWrapName = wrapper.unWrap(wrapName);
		Assertions.assertEquals(unWrapName, originalName);
	}
}
