package org.dromara.hutool.core.thread;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.Console;
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
		List<String> list = AsyncUtil.allOfGet(ListUtil.of(hutool, sweater, warm));
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
			int a = 1 / 0;
			ThreadUtil.sleep(2, TimeUnit.SECONDS);
			return "卫衣";
		});
		final CompletableFuture<String> warm = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
			return "真暖和";
		});
		// 等待完成
		List<String> list = AsyncUtil.allOfGet(ListUtil.of(hutool, sweater, warm), (e) -> "出错了");
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
		List<String> list = AsyncUtil.parallelAllOfGet(ListUtil.of(hutool, sweater, warm));
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
			int a = 1 / 0;
			ThreadUtil.sleep(2, TimeUnit.SECONDS);
			return "卫衣";
		});
		final CompletableFuture<String> warm = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
			return "真暖和";
		});
		// 等待完成
		List<String> list = AsyncUtil.parallelAllOfGet(ListUtil.of(hutool, sweater, warm), (e) -> "出错了");
		Assertions.assertEquals(Arrays.asList("hutool", "出错了", "真暖和"), list);
	}
}
