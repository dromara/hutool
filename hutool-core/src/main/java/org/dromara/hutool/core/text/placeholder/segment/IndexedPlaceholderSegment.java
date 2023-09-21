/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

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
