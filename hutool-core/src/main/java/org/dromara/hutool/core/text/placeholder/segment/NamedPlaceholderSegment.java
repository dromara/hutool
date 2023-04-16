package org.dromara.hutool.core.text.placeholder.segment;

/**
 * 字符串模板-有前后缀的变量占位符 Segment
 * <p>例如，"{1}", "{name}", "#{id}"</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
public class NamedPlaceholderSegment extends AbstractPlaceholderSegment {
    /**
     * 占位符完整文本
     * <p>例如：{@literal "{name}"->"{name}"}</p>
     */
    private final String wholePlaceholder;

    public NamedPlaceholderSegment(final String name, final String wholePlaceholder) {
        super(name);
        this.wholePlaceholder = wholePlaceholder;
    }

    @Override
    public String getText() {
        return wholePlaceholder;
    }

}
