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

import org.dromara.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 控制台单元测试
 * @author Looly
 *
 */
public class ConsoleTest {

	@Test
	public void logTest(){
		Console.log();

		final String[] a = {"abc", "bcd", "def"};
		Console.log(a);

		Console.log("This is Console log for {}.", "test");
	}

	@Test
	public void logTest2(){
		Console.log("a", "b", "c");
		Console.log((Object) "a", "b", "c");
	}

	@Test
	public void printTest(){
		final String[] a = {"abc", "bcd", "def"};
		Console.print(a);

		Console.log("This is Console print for {}.", "test");
	}

	@Test
	public void printTest2(){
		Console.print("a", "b", "c");
		Console.print((Object) "a", "b", "c");
	}

	@Test
	public void errorTest(){
		Console.error();

		final String[] a = {"abc", "bcd", "def"};
		Console.error(a);

		Console.error("This is Console error for {}.", "test");
	}

	@Test
	public void errorTest2(){
		Console.error("a", "b", "c");
		Console.error((Object) "a", "b", "c");
	}

	@Test
	@Disabled
	public void inputTest() {
		Console.log("Please input something: ");
		final String input = Console.input();
		Console.log(input);
	}

	@Test
	@Disabled
	public void printProgressTest() {
		for(int i = 0; i < 100; i++) {
			Console.printProgress('#', 100, i / 100D);
			ThreadUtil.sleep(200);
		}
	}

	@Test
	public void printColorTest(){
		System.out.print("\33[30;1m A \u001b[31;2m B \u001b[32;1m C \u001b[33;1m D \u001b[0m");
	}

}
