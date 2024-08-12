/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 定时任务模块，提供类Crontab表达式的定时任务，实现参考了Cron4j，同时可以支持秒级别的定时任务定义和年的定义（同时兼容Crontab、Cron4j、Quartz表达式）<br>
 * 定时任务模块由三部分组成：
 * <ul>
 *     <li>{@link org.dromara.hutool.cron.Scheduler} 定时任务调度器，用于整体管理任务的增删、启停和触发运行。</li>
 *     <li>{@link org.dromara.hutool.cron.task.Task} 定时任务实现，用于定义具体的任务</li>
 *     <li>{@link org.dromara.hutool.cron.pattern.CronPattern} 定时任务表达式，用于定义任务触发时间</li>
 * </ul>
 *
 * 同时，提供了{@link org.dromara.hutool.cron.CronUtil}工具类，维护一个全局的{@link org.dromara.hutool.cron.Scheduler}。
 *
 * @author looly
 */
package org.dromara.hutool.cron;
