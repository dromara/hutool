/*
 * Copyright (c) 2013-2024 Hutool Team.
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

import org.dromara.hutool.core.text.StrUtil;

/**
 * ANSI文本样式风格枚举
 *
 * <p>来自Spring Boot</p>
 *
 * @author Phillip Webb
 * @since 5.8.0
 */
public enum AnsiStyle implements AnsiElement {

	/**
	 * 重置/正常
	 */
	NORMAL(0),

	/**
	 * 粗体或增加强度
	 */
	BOLD(1),

	/**
	 * 弱化（降低强度）
	 */
	FAINT(2),

	/**
	 * 斜体
	 */
	ITALIC(3),

	/**
	 * 下划线
	 */
	UNDERLINE(4);

	private final int code;

	AnsiStyle(final int code) {
		this.code = code;
	}

	/**
	 * 获取ANSI文本样式风格代码
	 *
	 * @return 文本样式风格代码
	 */
	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return StrUtil.toString(this.code);
	}

}
