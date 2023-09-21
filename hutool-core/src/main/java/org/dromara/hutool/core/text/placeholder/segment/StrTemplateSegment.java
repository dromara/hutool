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
