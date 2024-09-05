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

package org.dromara.hutool.core.thread;

import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture工具类测试
 *
 * @author <achao1441470436@gmail.com>
 * @since 2021/11/10 0010 21:15
 */
public class AsyncUtilTest {

	@Test
	@Disabled
	public void waitAndGetTest() {
		final CompletableFuture<String> hutool = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(1, TimeUnit.SECONDS);
			return "hutool";
		});
		final CompletableFuture<String> sweater = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(2, TimeUnit.SECONDS);
			return "卫衣";
		});
		final CompletableFuture<String> warm = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
			return "真暖和";
		});
		// 等待完成
		AsyncUtil.waitAll(hutool, sweater, warm);
		// 获取结果
		Assertions.assertEquals("hutool卫衣真暖和", AsyncUtil.get(hutool) + AsyncUtil.get(sweater) + AsyncUtil.get(warm));
	}

	@Test
	@Disabled
	public void allGetTest() {
		final CompletableFuture<String> hutool = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(1, TimeUnit.SECONDS);
			return "hutool";
		});
		final CompletableFuture<String> sweater = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(2, TimeUnit.SECONDS);
			return "卫衣";
		});
		final CompletableFuture<String> warm = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
			return "真暖和";
		});
		// 等待完成
		final List<String> list = AsyncUtil.allOfGet(ListUtil.of(hutool, sweater, warm));
		// 获取结果
		Assertions.assertEquals(Arrays.asList("hutool", "卫衣", "真暖和"), list);
	}

	@Test
	@Disabled
	public void allGetTestException() {
		final CompletableFuture<String> hutool = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(1, TimeUnit.SECONDS);
			return "hutool";
		});
		final CompletableFuture<String> sweater = CompletableFuture.supplyAsync(() -> {
			final int a = 1 / 0;
			ThreadUtil.sleep(2, TimeUnit.SECONDS);
			return "卫衣";
		});
		final CompletableFuture<String> warm = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
			return "真暖和";
		});
		// 等待完成
		final List<String> list = AsyncUtil.allOfGet(ListUtil.of(hutool, sweater, warm), (e) -> "出错了");
		// 获取结果
		Assertions.assertEquals(Arrays.asList("hutool", "卫衣", "真暖和"), list);
	}

	@Test
	@Disabled
	public void parallelAllOfGetTest() {
		final CompletableFuture<String> hutool = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(1, TimeUnit.SECONDS);
			return "hutool";
		});
		final CompletableFuture<String> sweater = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(2, TimeUnit.SECONDS);
			return "卫衣";
		});
		final CompletableFuture<String> warm = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
			return "真暖和";
		});
		// 等待完成
		final List<String> list = AsyncUtil.parallelAllOfGet(ListUtil.of(hutool, sweater, warm));
		// 获取结果
		Assertions.assertEquals(Arrays.asList("hutool", "卫衣", "真暖和"), list);
	}

	@Test
	@Disabled
	public void parallelAllOfGetTestException() {
		final CompletableFuture<String> hutool = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(1, TimeUnit.SECONDS);
			return "hutool";
		});
		final CompletableFuture<String> sweater = CompletableFuture.supplyAsync(() -> {
			final int a = 1 / 0;
			ThreadUtil.sleep(2, TimeUnit.SECONDS);
			return "卫衣";
		});
		final CompletableFuture<String> warm = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
			return "真暖和";
		});
		// 等待完成
		final List<String> list = AsyncUtil.parallelAllOfGet(ListUtil.of(hutool, sweater, warm), (e) -> "出错了");
		Assertions.assertEquals(Arrays.asList("hutool", "出错了", "真暖和"), list);
	}
}
