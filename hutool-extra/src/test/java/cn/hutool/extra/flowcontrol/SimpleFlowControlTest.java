package cn.hutool.extra.flowcontrol;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestMethod.class})
@EnableAutoConfiguration
@Component
public class SimpleFlowControlTest {

	@Autowired
	private TestMethod testMethod;

	@Test
	public void testFlowControl() {
		int pass = 0;
		int notPass = 0;
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100 ; i++) {
			try {
				testMethod.test();
				pass++;
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (FlowControlException | InterruptedException ex){
				notPass++;
				Console.log(ex.getMessage());
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Console.log(StrUtil.format("{}秒 通过:{}, 限流:{}, 通过率:{}", NumberUtil.div(System.currentTimeMillis() - start, 1000), pass, notPass, NumberUtil.mul(NumberUtil.div(pass, (pass + notPass)), 100 )) + "%");
	}


	private static AtomicInteger pass = new AtomicInteger(0);
	private static AtomicInteger notPass = new AtomicInteger(0);

	@Test
	public void testFlowControl2() throws InterruptedException {

		ExecutorService pool = Executors.newFixedThreadPool(100);
		CountDownLatch countDownLatch = new CountDownLatch(1);

		long start = System.currentTimeMillis();

		for (int i = 0; i < 100 ; i++) {
			pool.execute(() -> {
				try {
					countDownLatch.await();
					testMethod.test2();
					pass.incrementAndGet();
				} catch (FlowControlException | InterruptedException ex) {
					Console.log(ex.getMessage());
					notPass.incrementAndGet();
				}
			});
		}

		countDownLatch.countDown();

		//阻塞1秒看效果
		TimeUnit.SECONDS.sleep(1);

		//减去阻塞的1秒
		Console.log(StrUtil.format("{}秒 通过:{}, 限流:{}, 通过率:{}", NumberUtil.div(System.currentTimeMillis() - start - 1000, 1000) , pass.get(), notPass.get(), NumberUtil.mul(NumberUtil.div(pass.get(), (pass.get() + notPass.get())), 100 )) + "%");
	}

}
