package org.dromara.hutool.core.date.format.parser;

import java.util.function.Predicate;

/**
 * 通过判断字符串的匹配，解析为日期
 *
 * @author looly
 * @since 6.0.0
 */
public interface PredicateDateParser extends DateParser, Predicate<CharSequence> {
}
