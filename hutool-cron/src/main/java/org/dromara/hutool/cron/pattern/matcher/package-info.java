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
 * 定时任务表达式匹配器，内部使用<br>
 * 单一表达式使用{@link org.dromara.hutool.cron.pattern.matcher.PatternMatcher}表示<br>
 * {@link org.dromara.hutool.cron.pattern.matcher.PatternMatcher}由7个{@link org.dromara.hutool.cron.pattern.matcher.PartMatcher}组成，
 * 分别表示定时任务表达式中的7个位置:
 * <pre>
 *         0      1     2        3         4       5        6
 *      SECOND MINUTE HOUR DAY_OF_MONTH MONTH DAY_OF_WEEK YEAR
 * </pre>
 *
 * @author looly
 *
 */
package org.dromara.hutool.cron.pattern.matcher;
