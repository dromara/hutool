package cn.hutool.extra.redisson;

import cn.hutool.extra.redisson.config.AutoRedissonLockConfiguration;
import cn.hutool.extra.redisson.demo.RedissonDemo;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: miracle
 * Date: 2023/8/16 16:13
 */
@RunWith(SpringRunner.class)
@Import({LettuceConnectionFactory.class, RedissonDemo.class, AutoRedissonLockConfiguration.class})
@SpringBootTest(classes = LockInterceptorTest.class)
@ActiveProfiles
@EnableAspectJAutoProxy
public class LockInterceptorTest {

	@Autowired
	private RedissonDemo redissonDemo;

	private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 1000, 1000, TimeUnit.SECONDS, new SynchronousQueue<>());

	@Test
	public void test() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(100);
		List<Integer> list = Lists.newArrayList();

		for (int i = 0; i < 100; i++) {
			threadPoolExecutor.execute(() -> {
				list.add(redissonDemo.lock("test"));
				countDownLatch.countDown();
			});
		}
		countDownLatch.await();
		System.out.println(list.size());
		long count = list.stream().distinct().count();
		System.out.println("distinct:" + count);
		assertThat(list.size()).isEqualTo(count);
	}

	public static void main(String[] args) {
		SpringApplication.run(LockInterceptorTest.class, args);
	}

}
