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

package org.dromara.hutool.http.html;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI8YV0KTest {
	@Test
	public void removeHtmlAttrTest(){
		final String str = "<content styleCode=\"xmChange yes\">";
		Assertions.assertEquals("<content>", HtmlUtil.removeHtmlAttr(str, "styleCode"));
	}

	@Test
	public void removeHtmlAttrTest2(){
		final String str = "<content styleCode=\"xmChange\"/>";
		Assertions.assertEquals("<content/>", HtmlUtil.removeHtmlAttr(str, "styleCode"));
	}

	@Test
	public void removeHtmlAttrTest3(){
		final String str = "<content styleCode=\"dada ada\" data=\"dsad\" >";
		Assertions.assertEquals("<content data=\"dsad\">", HtmlUtil.removeHtmlAttr(str, "styleCode"));
	}
}
