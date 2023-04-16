package org.dromara.hutool.core.text.placeholder.segment;

/**
 * 字符串模板-单变量占位符 Segment
 * <p>例如，"?", "{}", "$$$"</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
public class SinglePlaceholderSegment extends AbstractPlaceholderSegment {

    private SinglePlaceholderSegment(final String placeholder) {
        super(placeholder);
    }

    public static SinglePlaceholderSegment newInstance(final String placeholder) {
        return new SinglePlaceholderSegment(placeholder);
    }
}
