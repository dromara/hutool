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

package org.dromara.hutool.core.lang;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ConsoleTableTest {

	@Test
	@Disabled
	public void printSBCTest() {
		ConsoleTable t = ConsoleTable.of();
		t.addHeader("姓名", "年龄");
		t.addBody("张三", "15");
		t.addBody("李四", "29");
		t.addBody("王二麻子", "37");
		t.print();

		Console.log();

		t = ConsoleTable.of();
		t.addHeader("体温", "占比");
		t.addHeader("℃", "%");
		t.addBody("36.8", "10");
		t.addBody("37", "5");
		t.print();

		Console.log();

		t = ConsoleTable.of();
		t.addHeader("标题1", "标题2");
		t.addBody("12345", "混合321654asdfcSDF");
		t.addBody("sd   e3ee  ff22", "ff值");
		t.print();
	}

	@Test
	@Disabled
	public void printDBCTest() {
		ConsoleTable t = ConsoleTable.of().setSBCMode(false);
		t.addHeader("姓名", "年龄");
		t.addBody("张三", "15");
		t.addBody("李四", "29");
		t.addBody("王二麻子", "37");
		t.print();

		Console.log();

		t = ConsoleTable.of().setSBCMode(false);
		t.addHeader("体温", "占比");
		t.addHeader("℃", "%");
		t.addBody("36.8", "10");
		t.addBody("37", "5");
		t.print();
	}
}
