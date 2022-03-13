package cn.hutool.extra.spring;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * Spring 动态定时任务封装
 * <ol>
 *     <li>创建定时任务</li>
 *     <li>修改定时任务</li>
 *     <li>取消定时任务</li>
 *     <li>高级操作</li>
 * </ol>
 * 参考：<a href="https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling">Spring doc</a>
 *
 * @author JC
 * @date 03/13
 */
@Component
public class SpringCronUtil {
	/**
	 * 任务调度器
	 */
	private static TaskScheduler taskScheduler;

	/**
	 * ID 与 Future 绑定
	 */
	private static final Map<Serializable, ScheduledFuture<?>> TASK_FUTURE = MapUtil.newConcurrentHashMap();

	/**
	 * ID 与 Runnable 绑定
	 */
	private static final Map<Serializable, Runnable> TASK_RUNNABLE = MapUtil.newConcurrentHashMap();

	/**
	 * 加入定时任务
	 *
	 * @param task       任务
	 * @param expression 定时任务执行时间的cron表达式
	 * @return 定时任务ID
	 */
	public static String schedule(Runnable task, String expression) {
		String id = IdUtil.fastUUID();
		return schedule(id, task, expression);
	}

	/**
	 * 加入定时任务
	 *
	 * @param id         定时任务ID
	 * @param expression 定时任务执行时间的cron表达式
	 * @param task       任务
	 * @return 定时任务ID
	 */
	public static String schedule(Serializable id, Runnable task, String expression) {
		ScheduledFuture<?> schedule = taskScheduler.schedule(task, new CronTrigger(expression));
		TASK_FUTURE.put(id, schedule);
		TASK_RUNNABLE.put(id, task);
		return id.toString();
	}

	/**
	 * 修改定时任务
	 *
	 * @param id         定时任务ID
	 * @param expression 定时任务执行时间的cron表达式
	 * @return 是否修改成功，{@code false}表示未找到对应ID的任务
	 */
	public static boolean update(Serializable id, String expression) {
		if (!TASK_FUTURE.containsKey(id)) {
			return false;
		}
		ScheduledFuture<?> future = TASK_FUTURE.get(id);
		if (future == null) {
			return false;
		}
		future.cancel(true);
		schedule(id, TASK_RUNNABLE.get(id), expression);
		return true;
	}

	/**
	 * 移除任务
	 *
	 * @param schedulerId 任务ID
	 * @return 是否移除成功，{@code false}表示未找到对应ID的任务
	 */
	public static boolean cancel(Serializable schedulerId) {
		if (!TASK_FUTURE.containsKey(schedulerId)) {
			return false;
		}
		ScheduledFuture<?> future = TASK_FUTURE.get(schedulerId);
		boolean cancel = future.cancel(false);
		if (cancel) {
			TASK_FUTURE.remove(schedulerId);
			TASK_RUNNABLE.remove(schedulerId);
		}
		return cancel;
	}

	@Resource
	public void setTaskScheduler(TaskScheduler taskScheduler) {
		SpringCronUtil.taskScheduler = taskScheduler;
	}

	/**
	 * @return 获得Scheduler对象
	 */
	public static TaskScheduler getScheduler() {
		return taskScheduler;
	}

	/**
	 * 获得当前运行的所有任务
	 *
	 * @return 所有任务
	 */
	public static List<Serializable> getAllTask() {
		if (CollUtil.isNotEmpty(TASK_FUTURE.keySet())) {
			return new ArrayList<>(TASK_FUTURE.keySet());
		}
		return new ArrayList<>();
	}

	/**
	 * 取消所有的任务
	 */
	public static void destroy() {
		for (ScheduledFuture<?> future : TASK_FUTURE.values()) {
			if (future != null) {
				future.cancel(true);
			}
		}
		TASK_FUTURE.clear();
		TASK_RUNNABLE.clear();
	}
}
