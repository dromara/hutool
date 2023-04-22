package org.dromara.hutool.core.text.placeholder.segment;

/**
 * 字符串模板-占位符-抽象 Segment
 * <p>例如：{@literal "???"->"???", "{}"->"{}", "{name}"->"name"}</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
public abstract class AbstractPlaceholderSegment implements StrTemplateSegment {
    /**
     * 占位符变量
     * <p>例如：{@literal "???"->"???", "{}"->"{}", "{name}"->"name"}</p>
     */
    private final String placeholder;

    protected AbstractPlaceholderSegment(final String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String getText() {
        return placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
