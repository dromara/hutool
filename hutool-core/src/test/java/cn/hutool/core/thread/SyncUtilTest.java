package cn.hutool.core.thread;

import cn.hutool.core.lang.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture工具类测试
 *
 * @author <achao1441470436@gmail.com>
 * @since 2021/11/10 0010 21:15
 */
public class SyncUtilTest {

	@Test
	public void waitAndGetTest() {
		CompletableFuture<String> hutool = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(3, TimeUnit.SECONDS);
			return "hutool";
		});
		CompletableFuture<String> sweater = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(4, TimeUnit.SECONDS);
			return "卫衣";
		});
		CompletableFuture<String> warm = CompletableFuture.supplyAsync(() -> {
			ThreadUtil.sleep(5, TimeUnit.SECONDS);
			return "真暖和";
		});
		// 等待完成
		SyncUtil.wait(hutool, sweater, warm);
		// 获取结果
		Assert.isTrue("hutool卫衣真暖和".equals(SyncUtil.get(hutool) + SyncUtil.get(sweater) + SyncUtil.get(warm)));
	}
}
