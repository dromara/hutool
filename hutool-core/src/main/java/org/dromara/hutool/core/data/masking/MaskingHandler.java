/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.data.masking;

/**
 * 脱敏处理器，用于自定义脱敏规则
 *
 */
@FunctionalInterface
public interface MaskingHandler {

	/**
	 * 处理传入的数据字符串，经过脱敏逻辑后，返回处理后的值
	 *
	 * @param value 待处理的值
	 * @return 处理后的值
	 */
	String handle(CharSequence value);
}
