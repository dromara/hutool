package org.dromara.hutool.core.text.placeholder.segment;

import java.util.Iterator;

/**
 * 字符串模板-固定文本 Segment
 *
 * @author emptypoint
 * @since 6.0.0
 */
public class LiteralSegment implements StrTemplateSegment {
    /**
     * 模板中固定的一段文本
     */
    private final String text;

    public LiteralSegment(final String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void format(final StringBuilder sb, final Iterator<String> valueIterator) {
        // 在格式化中 拼接 固定文本
        sb.append(text);
    }

}
