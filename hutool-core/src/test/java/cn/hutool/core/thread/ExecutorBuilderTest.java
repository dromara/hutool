package cn.hutool.core.thread;

import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ThreadPoolExecutor;

public class ExecutorBuilderTest {

	@Test
	@Ignore
	public void CallerRunsPolicyTest(){
		// https://gitee.com/dromara/hutool/pulls/660
		final ThreadPoolExecutor executor = ExecutorBuilder.create().setCorePoolSize(1).setMaxPoolSize(1).setHandler(RejectPolicy.BLOCK.getValue()).build();
		executor.execute(()-> Console.log("### 1"));
		executor.execute(()-> Console.log("### 2"));

		executor.shutdown();
		executor.execute(()-> Console.log("### 3"));
		executor.execute(()-> Console.log("### 4"));
		executor.execute(()-> Console.log("### 5"));
		executor.execute(()-> Console.log("### 6"));
		ThreadUtil.sleep(3000);
	}
}
