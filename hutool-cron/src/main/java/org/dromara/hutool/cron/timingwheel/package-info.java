/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

/**
 * 时间轮实现，重写了kafka的TimingWheel<br>
 * 时间轮一般会实现成一个环形结构，类似一个时钟，分为很多槽，一个槽代表一个时间间隔，每个槽使用双向链表存储定时任务。指针周期性地跳动，跳动到一个槽位，就执行该槽位的定时任务。
 *
 * <p>
 * 时间轮算法介绍：https://www.confluent.io/blog/apache-kafka-purgatory-hierarchical-timing-wheels/<br>
 * 参考：https://github.com/eliasyaoyc/timingwheel
 *
 * @author looly
 */
package org.dromara.hutool.cron.timingwheel;
