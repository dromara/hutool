package org.dromara.hutool.core.text.placeholder.segment;

/**
 * 字符串模板-抽象 Segment
 *
 * @author emptypoint
 * @since 6.0.0
 */
public interface StrTemplateSegment {
    /**
     * 获取文本值
     *
     * @return 文本值，对于固定文本Segment，返回文本值；对于单占位符Segment，返回占位符；对于有前后缀的占位符Segment，返回占位符完整文本，例如: "{name}"
     */
    String getText();

}
