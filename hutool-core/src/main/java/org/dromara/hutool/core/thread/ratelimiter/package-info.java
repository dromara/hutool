/*
 * Copyright (c) 2024 Hutool Team.
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
 * 限流器实现，几种策略包括：
 * <ul>
 *     <li>令牌桶（Token Bucket）</li>
 *     <li>漏桶（Leaky Bucket）</li>
 *     <li>固定窗口（Fixed Window）</li>
 *     <li>滑动窗口（Sliding Window）</li>
 * </ul>
 * 概念见：https://www.explainthis.io/zh-hans/swe/rate-limiter
 *
 * @author Looly
 * @since 6.0.0
 */
package org.dromara.hutool.core.thread.ratelimiter;
