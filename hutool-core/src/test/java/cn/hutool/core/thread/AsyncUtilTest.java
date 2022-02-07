package cn.hutool.core.thread;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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
	@Ignore
	public void waitAndGetTest() {
		CompletableFuture<String> hutool = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(1, TimeUnit.SECONDS);
			return "hutool";
		});
		CompletableFuture<String> sweater = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(2, TimeUnit.SECONDS);
			return "卫衣";
		});
		CompletableFuture<String> warm = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
			return "真暖和";
		});
		// 等待完成
		AsyncUtil.waitAll(hutool, sweater, warm);
		// 获取结果
		Assert.assertEquals("hutool卫衣真暖和", AsyncUtil.get(hutool) + AsyncUtil.get(sweater) + AsyncUtil.get(warm));
	}
}
