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

package org.dromara.hutool.core.date.format.parser;

import java.util.function.Predicate;

/**
 * 通过判断字符串的匹配，解析为日期<br>
 * 通过实现{@link #test(Object)}方法判断字符串是否符合此解析器的规则，如果符合，则调用{@link #parse(CharSequence)}完成解析。
 *
 * @author looly
 * @since 6.0.0
 */
public interface PredicateDateParser extends DateParser, Predicate<CharSequence> {
}
