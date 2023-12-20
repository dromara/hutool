/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.html;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HTMLFilterTest {
	@Test
	void issue3433Test() {
		final String p1 = "<p>a</p>";
		final String p2 = "<p onclick=\"bbbb\">a</p>";

		final HTMLFilter htmlFilter = new HTMLFilter();
		String filter = htmlFilter.filter(p1);
		Assertions.assertEquals("<p>a</p>", filter);

		filter = htmlFilter.filter(p2);
		Assertions.assertEquals("<p>a</p>", filter);
	}
}
