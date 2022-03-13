package cn.hutool.extra.spring;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.config.SpringCronConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * @author JC
 * @date 03/13
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SpringCronConfig.class, SpringCronUtil.class})
public class SpringCronUtilTest {
	/**
	 * 创建一个定时任务
	 * 观察日志可进行验证
	 */
	@Test
	public void registerTask() {
		String ID1 = SpringCronUtil.schedule(this::task, "0/1 * * * * ?");
		String ID2 = SpringCronUtil.schedule(888, this::task, "0/1 * * * * ?");
		log.info("taskId: {},{}", ID1, ID2);
	}

	/**
	 * 修改一个定时任务
	 */
	@Test
	@SneakyThrows
	public void updateTask() {
		SpringCronUtil.schedule(888, this::task, "0/1 * * * * ?");
		Thread.sleep(5000);
		boolean update = SpringCronUtil.update(888, "0/5 * * * * ?");
		log.info("update task result: {}", update);
	}

	/**
	 * 取消一个定时任务
	 */
	@Test
	@SneakyThrows
	public void cancelTask() {
		SpringCronUtil.schedule(888, this::task, "0/1 * * * * ?");
		Thread.sleep(5000);
		boolean cancel = SpringCronUtil.cancel(888);
		log.info("cancel task result: {}", cancel);
	}

	/**
	 * 高级用法
	 * 参考：
	 */
	@Test
	public void senior() {
		TaskScheduler scheduler = SpringCronUtil.getScheduler();
		// 给定时间 开始, 间隔时间..
		scheduler.scheduleAtFixedRate(this::task, Instant.now(), Duration.ofMinutes(10));
		// ...
	}

	/**
	 * 取消全部定时任务
	 * 查看当前所有的任务
	 */
	@After
	@SneakyThrows
	public void cancelAll() {
		Thread.sleep(10000);
		List<Serializable> allTask = SpringCronUtil.getAllTask();
		log.info("allTask: {}", allTask);

		SpringCronUtil.destroy();

		allTask = SpringCronUtil.getAllTask();
		log.info("allTask: {}", allTask);
	}

	private void task() {
		log.info("information only.. (date:{})", DateUtil.now());
	}
}
