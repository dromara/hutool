package org.dromara.hutool.core.text.placeholder.segment;

/**
 * 基字符串模板-基于下标的占位符 Segment
 * <p>例如，"{1}"</p>
 *
 * @author emptypoint
 * @since 6.0.0
 */
public class IndexedPlaceholderSegment extends NamedPlaceholderSegment {
    /**
     * 下标值
     */
    private final int index;

    public IndexedPlaceholderSegment(final String idxStr, final String wholePlaceholder) {
        super(idxStr, wholePlaceholder);
        this.index = Integer.parseInt(idxStr);
    }

    public int getIndex() {
        return index;
    }
}
