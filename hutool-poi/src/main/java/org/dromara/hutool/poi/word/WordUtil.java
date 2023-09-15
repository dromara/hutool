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
