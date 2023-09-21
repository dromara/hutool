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

package org.dromara.hutool.core.text;

import org.dromara.hutool.core.text.placeholder.PlaceholderParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * test for {@link PlaceholderParser}
 *
 * @author huangchengxing
 */
public class PlaceholderParserTest {

	@Test
	public void testParse() {
		String text = "i {a}{m} a {jvav} programmer";
		PlaceholderParser parser = new PlaceholderParser(str -> str, "{", "}");
		Assertions.assertEquals(
			"i am a jvav programmer",
			parser.apply(text)
		);

		text = "i [a][m] a [jvav] programmer";
		parser = new PlaceholderParser(str -> str, "[", "]");
		Assertions.assertEquals(
			"i am a jvav programmer",
			parser.apply(text)
		);

		text = "i \\[a][[m\\]] a [jvav] programmer";
		parser = new PlaceholderParser(str -> str, "[", "]");
		Assertions.assertEquals(
			"i [a][m] a jvav programmer",
			parser.apply(text)
		);

		text = "i /[a][[m/]] a [jvav] programmer";
		parser = new PlaceholderParser(str -> str, "[", "]", '/');
		Assertions.assertEquals(
			"i [a][m] a jvav programmer",
			parser.apply(text)
		);

		text = "select * from #[tableName] where id = #[id]";
		parser = new PlaceholderParser(str -> "?", "#[", "]");
		Assertions.assertEquals(
				"select * from ? where id = ?",
				parser.apply(text)
		);
	}

}
