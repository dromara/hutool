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

}
