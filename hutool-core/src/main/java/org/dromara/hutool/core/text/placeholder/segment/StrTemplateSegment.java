package org.dromara.hutool.core.text.placeholder.segment;

import java.util.Iterator;

/**
 * 字符串模板-抽象 Segment
 *
 * @author emptypoint
 * @since 6.0.0
 */
public interface StrTemplateSegment {
    /**
     * 在格式化中，按顺序 拼接 参数值
     *
     * <p>如果是固定文本，则直接拼接，如果是占位符，则拼接参数值</p>
     *
     * @param sb            存储格式化结果的变量
     * @param valueIterator 与占位符依次对应的参数值列表
     */
    void format(final StringBuilder sb, final Iterator<String> valueIterator);

    /**
     * 获取文本值
     *
     * @return 文本值，对于固定文本Segment，返回文本值；对于单占位符Segment，返回占位符；对于有前后缀的占位符Segment，返回占位符完整文本，例如: "{name}"
     */
    String getText();

}
