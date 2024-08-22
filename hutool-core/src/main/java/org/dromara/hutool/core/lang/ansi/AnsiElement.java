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

package org.dromara.hutool.core.lang.ansi;

/**
 * ANSI可转义节点接口，实现为ANSI颜色等
 *
 * <p>来自Spring Boot</p>
 *
 * @author Phillip Webb
 */
public interface AnsiElement {

	/**
	 * 获取ANSI代码
	 * @return ANSI代码
	 * @since 5.8.7
	 */
	int getCode();

	/**
	 * @return ANSI转义编码
	 */
	@Override
	String toString();

}
