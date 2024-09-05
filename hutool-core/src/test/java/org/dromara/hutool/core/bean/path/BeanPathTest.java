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

package org.dromara.hutool.core.bean.path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BeanPathTest {

	@Test
	void parseDotTest() {
		BeanPath beanPath = new BeanPath("userInfo.examInfoDict[0].id");
		Assertions.assertEquals("userInfo", beanPath.getNode().toString());
		Assertions.assertEquals("examInfoDict[0].id", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("examInfoDict", beanPath.getNode().toString());
		Assertions.assertEquals("[0].id", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("0", beanPath.getNode().toString());
		Assertions.assertEquals(".id", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("id", beanPath.getNode().toString());
		Assertions.assertNull(beanPath.getChild());
	}

	@Test
	void parseDotWithQuoteTest() {
		BeanPath beanPath = new BeanPath("'userInfo'.examInfoDict[0].'id'");
		Assertions.assertEquals("userInfo", beanPath.getNode().toString());
		Assertions.assertEquals("examInfoDict[0].'id'", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("examInfoDict", beanPath.getNode().toString());
		Assertions.assertEquals("[0].'id'", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("0", beanPath.getNode().toString());
		Assertions.assertEquals(".'id'", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("id", beanPath.getNode().toString());
		Assertions.assertNull(beanPath.getChild());
	}

	@Test
	void parseDotWithQuoteTest2() {
		BeanPath beanPath = new BeanPath("userInfo.'examInfoDict'[0].id");
		Assertions.assertEquals("userInfo", beanPath.getNode().toString());
		Assertions.assertEquals("'examInfoDict'[0].id", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("examInfoDict", beanPath.getNode().toString());
		Assertions.assertEquals("[0].id", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("0", beanPath.getNode().toString());
		Assertions.assertEquals(".id", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("id", beanPath.getNode().toString());
		Assertions.assertNull(beanPath.getChild());
	}

	@Test
	void parseBucketTest() {
		BeanPath beanPath = new BeanPath("[userInfo][examInfoDict][0][id]");
		Assertions.assertEquals("userInfo", beanPath.getNode().toString());
		Assertions.assertEquals("[examInfoDict][0][id]", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("examInfoDict", beanPath.getNode().toString());
		Assertions.assertEquals("[0][id]", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("0", beanPath.getNode().toString());
		Assertions.assertEquals("[id]", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("id", beanPath.getNode().toString());
		Assertions.assertNull(beanPath.getChild());
	}

	@Test
	void parseBucketWithQuoteTest() {
		BeanPath beanPath = new BeanPath("['userInfo']['examInfoDict'][0][id]");
		Assertions.assertEquals("userInfo", beanPath.getNode().toString());
		Assertions.assertEquals("['examInfoDict'][0][id]", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("examInfoDict", beanPath.getNode().toString());
		Assertions.assertEquals("[0][id]", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("0", beanPath.getNode().toString());
		Assertions.assertEquals("[id]", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("id", beanPath.getNode().toString());
		Assertions.assertNull(beanPath.getChild());
	}

	@Test
	void parseBucketWithQuoteTest2() {
		BeanPath beanPath = new BeanPath("[userInfo][examInfoDict][0]['id']");
		Assertions.assertEquals("userInfo", beanPath.getNode().toString());
		Assertions.assertEquals("[examInfoDict][0]['id']", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("examInfoDict", beanPath.getNode().toString());
		Assertions.assertEquals("[0]['id']", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("0", beanPath.getNode().toString());
		Assertions.assertEquals("['id']", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("id", beanPath.getNode().toString());
		Assertions.assertNull(beanPath.getChild());
	}

	@Test
	void rangePathTest() {
		BeanPath beanPath = new BeanPath("[userInfo][2:3]");
		Assertions.assertEquals("userInfo", beanPath.getNode().toString());
		Assertions.assertEquals("[2:3]", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("[2:3:1]", beanPath.getNode().toString());
		Assertions.assertNull(beanPath.getChild());
	}

	@Test
	void listPathTest() {
		BeanPath beanPath = new BeanPath("[userInfo][1,2,3]");
		Assertions.assertEquals("userInfo", beanPath.getNode().toString());
		Assertions.assertEquals("[1,2,3]", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("[1, 2, 3]", beanPath.getNode().toString());
		Assertions.assertNull(beanPath.getChild());
	}

	@Test
	void listKeysPathTest() {
		BeanPath beanPath = new BeanPath("[userInfo]['a', 'b', 'c']");
		Assertions.assertEquals("userInfo", beanPath.getNode().toString());
		Assertions.assertEquals("['a', 'b', 'c']", beanPath.getChild());

		beanPath = beanPath.next();
		Assertions.assertEquals("[a, b, c]", beanPath.getNode().toString());
		Assertions.assertNull(beanPath.getChild());
	}
}
