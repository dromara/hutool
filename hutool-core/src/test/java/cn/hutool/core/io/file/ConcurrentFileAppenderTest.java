package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author gxz gongxuanzhang@foxmail.com
 **/
public class ConcurrentFileAppenderTest {

	@Test
	@Ignore
	public void a() throws Exception{
		File file = new File("aaa.txt");
		ConcurrentFileAppender cfa = new ConcurrentFileAppender(file,10000,true);
		//  创建100个线程同时插入
		final int threadCount = 100;
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);
		for (int i = 0; i < 100; i++) {
			new Thread(()->{
				//  每个线程插入1000条
				//  真实情况此处可能有计算逻辑，需要等待时间，这也是类ConcurrentFileAppender存在的意义
				for (int i1 = 0; i1 < 1000; i1++) {
					cfa.append(UUID.randomUUID().toString());
				}
				countDownLatch.countDown();
			}).start();
		}
		countDownLatch.await();
		cfa.flush();
		List<String> fileContext = FileUtil.readUtf8Lines(file);
		file.delete();
		Assert.assertEquals(fileContext.size(),100*1000);
	}
}
