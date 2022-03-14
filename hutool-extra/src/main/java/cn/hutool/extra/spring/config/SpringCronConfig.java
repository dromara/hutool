package cn.hutool.extra.spring.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 可自行配置任务线程池, 修改默认参数
 *
 * @author JC
 * @date 03/13
 */
@Configuration
@EnableScheduling
public class SpringCronConfig {
	@Bean
	@ConditionalOnMissingBean(value = TaskScheduler.class)
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		// 任务线程池初始化
		scheduler.setThreadNamePrefix("TaskScheduler-");
		scheduler.setPoolSize(Runtime.getRuntime().availableProcessors() / 3 + 1);

		// 保证能立刻丢弃运行中的任务
		scheduler.setRemoveOnCancelPolicy(true);
		return scheduler;
	}
}
