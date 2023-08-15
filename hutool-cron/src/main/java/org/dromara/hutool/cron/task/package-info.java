/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

/**
 * 定时任务中作业的抽象封装和实现，包括Runnable实现和反射实现<br>
 * {@link org.dromara.hutool.cron.task.Task}表示一个具体的任务，当满足时间匹配要求时，会执行{@link org.dromara.hutool.cron.task.Task#execute()}方法。
 *
 * @author looly
 *
 */
package org.dromara.hutool.cron.task;
