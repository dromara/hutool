package cn.hutool.core.thread;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import org.junit.Ignore;
import org.junit.Test;

public class ConcurrencyTesterTest {

	@Test
	@Ignore
	public void concurrencyTesterTest() {
		ConcurrencyTester tester = ThreadUtil.concurrencyTest(100, () -> {
			long delay = RandomUtil.randomLong(100, 1000);
			ThreadUtil.sleep(delay);
			Console.log("{} test finished, delay: {}", Thread.currentThread().getName(), delay);
		});
		Console.log(tester.getInterval());
	}

	@Test
	@Ignore
	public void multiTest(){
		ConcurrencyTester ct = new ConcurrencyTester(5);
		for(int i=0;i<3;i++){
			Console.log("开始执行第{}个",i);
			ct.test(() -> {
				// 需要并发测试的业务代码
				Console.log("当前执行线程：" + Thread.currentThread().getName()+" 产生时间 "+ DateUtil.now());
				ThreadUtil.sleep(RandomUtil.randomInt(1000, 3000));
			});
		}
		Console.log("全部线程执行完毕 "+DateUtil.now());
	}
}
