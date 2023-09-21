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
