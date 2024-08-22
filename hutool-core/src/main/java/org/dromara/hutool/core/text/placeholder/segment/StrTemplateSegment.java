/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
