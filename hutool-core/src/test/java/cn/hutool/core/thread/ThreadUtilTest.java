package cn.hutool.core.thread;

import cn.hutool.core.date.TimeUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtilTest {

	@Test
	public void executeTest() {
		final boolean isValid = true;

		ThreadUtil.execute(() -> Assert.assertTrue(isValid));
	}

	@Test
	@Ignore
	public void phaserTest(){
		LocalDateTime now = TimeUtil.parse("2022-08-04T22:59:59+08:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		Assert.assertNotNull(now);

		int repeat = 30; // 执行的轮数配置
		Phaser phaser = new Phaser() {  // 进行一些处理方法的覆写
			//返回ture: 移相器终止，false: 移相器继续执行
			@Override
			protected boolean onAdvance(int phase, int registeredParties) { // 回调处理
				System.out.printf("【onAdvance()处理】进阶处理操作，phase = %s、registeredParties = %s%n",
						phase, registeredParties);
				return phase + 1 >= repeat || registeredParties == 0; // 终止处理
			}
		};
		for (int x = 0; x < 2; x++) {   // 循环创建2个线程
			phaser.register(); // 注册参与者的线程
			new Thread(()->{ // 每一个线程都在持续的执行之中
				while (!phaser.isTerminated()) { // 现在没有终止Phaser执行
					try {
						TimeUnit.SECONDS.sleep(RandomUtil.randomInt(1, 10)); // 增加操作延迟,模拟各个线程执行时间不多。阿超、阿珍准备爱果的时间不同
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					phaser.arriveAndAwaitAdvance(); // 等待其他的线程就位； 准备就绪，并等待其他线程就绪; 阿超、阿珍准备好了爱果，相互等待见面共度美好的一天
					String date = TimeUtil.formatNormal(now.plusYears(phaser.getPhase() - 1)); // 增加一年
					System.out.printf("【%s】%s 阿超和阿珍共度了一个美好的七夕。%n", date, Thread.currentThread().getName());
					ThreadUtil.sleep(3, TimeUnit.SECONDS);
				}
			}, "子线程 - " + (x == 0 ? "阿超" : "阿珍")).start();
		}

		ThreadUtil.waitForDie();
	}

	@Test
	public void cyclicBarrierTest(){
		//示例：7个同学，集齐7个龙珠，7个同学一起召唤神龙；前后集齐了2次
		AtomicInteger times = new AtomicInteger();
		CyclicBarrier barrier = new CyclicBarrier(7, ()->{
			System.out.println("");
			System.out.println("");
			System.out.println("【循环栅栏业务处理】7个子线程 都收集了一颗龙珠，七颗龙珠已经收集齐全，开始召唤神龙。" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			times.getAndIncrement();
		}); // 现在设置的栅栏的数量为2
		for (int x = 0; x < 7; x++) {   // 创建7个线程, 当然也可以使用线程池替换。
			new Thread(() -> {
				while (times.get() < 2) {
					try {
						System.out.printf("【Barrier - 收集龙珠】当前的线程名称：%s%n", Thread.currentThread().getName());
						int time = ThreadLocalRandom.current().nextInt(1, 10); // 等待一段时间，模拟线程的执行时间
						TimeUnit.SECONDS.sleep(time); // 模拟业务延迟，收集龙珠的时间
						barrier.await(); // 等待，凑够了7个等待的线程
						System.err.printf("〖Barrier - 举起龙珠召唤神龙〗当前的线程名称：%s\t%s%n", Thread.currentThread().getName(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
						if (barrier.getParties() >= 7) {
							barrier.reset(); // 重置栅栏，等待下一次的召唤。
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, "线程 - " + x).start();
		}
	}
}
