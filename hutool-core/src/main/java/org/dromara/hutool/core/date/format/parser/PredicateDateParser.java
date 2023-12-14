package org.dromara.hutool.core.date.format.parser;

import java.util.function.Predicate;

/**
 * 通过判断字符串的匹配，解析为日期<br>
 * 通过实现{@link #test(Object)}方法判断字符串是否符合此解析器的规则，如果符合，则调用{@link #parse(String)}完成解析。
 *
 * @author looly
 * @since 6.0.0
 */
public interface PredicateDateParser extends DateParser, Predicate<CharSequence> {
}
