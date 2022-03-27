/**
 * 定时任务中作业的抽象封装和实现，包括Runnable实现和反射实现<br>
 * {@link cn.hutool.cron.task.Task}表示一个具体的任务，当满足时间匹配要求时，会执行{@link cn.hutool.cron.task.Task#execute()}方法。
 *
 * @author looly
 *
 */
package cn.hutool.cron.task;
