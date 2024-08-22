/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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
 * 定时任务中作业的抽象封装和实现，包括Runnable实现和反射实现<br>
 * {@link org.dromara.hutool.cron.task.Task}表示一个具体的任务，当满足时间匹配要求时，会执行{@link org.dromara.hutool.cron.task.Task#execute()}方法。
 *
 * @author looly
 *
 */
package org.dromara.hutool.cron.task;
