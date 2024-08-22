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
