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
