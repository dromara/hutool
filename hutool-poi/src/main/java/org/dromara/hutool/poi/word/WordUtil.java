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

package org.dromara.hutool.poi.word;

import java.io.File;

/**
 * Word工具类
 *
 * @author Looly
 * @since 4.5.16
 */
public class WordUtil {

	/**
	 * 创建Word 07格式的生成器
	 *
	 * @return {@link Word07Writer}
	 */
	public static Word07Writer getWriter() {
		return new Word07Writer();
	}

	/**
	 * 创建Word 07格式的生成器
	 *
	 * @param destFile 目标文件
	 * @return {@link Word07Writer}
	 */
	public static Word07Writer getWriter(final File destFile) {
		return new Word07Writer(destFile);
	}
}
