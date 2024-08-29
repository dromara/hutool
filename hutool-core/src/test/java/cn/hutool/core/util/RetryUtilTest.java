package cn.hutool.core.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class RetryUtilTest {

	@Test
	void run() {
		assertDoesNotThrow(() -> {
			RetryUtil.run(0,1L, TimeUnit.SECONDS,()->{
				System.out.println(1);
			});
		});
		assertThrows(IllegalArgumentException.class,() -> {
			RetryUtil.run(0,1L, TimeUnit.SECONDS,()->{
				throw new IllegalArgumentException("1");
			});
		});
	}
}
