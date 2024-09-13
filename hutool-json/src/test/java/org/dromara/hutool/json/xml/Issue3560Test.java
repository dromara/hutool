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

package org.dromara.hutool.json.xml;

import org.dromara.hutool.json.OldJSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3560Test {
	@Test
	public void toJSONObjectTest() {
		final String inPara= "<ROOT><ID>002317479934367853</ID><CONTENT><![CDATA[asdfadf&amp;21sdgzxv&amp;aasfasf]]></CONTENT></ROOT>";
		final OldJSONObject json = JSONXMLUtil.toJSONObject(inPara, ParseConfig.of().setKeepStrings(true));
		Assertions.assertEquals("{\"ROOT\":{\"ID\":\"002317479934367853\",\"CONTENT\":\"asdfadf&amp;21sdgzxv&amp;aasfasf\"}}", json.toString());
	}
}
