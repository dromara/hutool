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

import org.apache.poi.common.usermodel.PictureType;
import org.dromara.hutool.core.io.file.FileNameUtil;

/**
 * 图片类型工具类
 *
 * @author looly
 * @since 6.0.0
 */
public class PictureTypeUtil {

	/**
	 * 获取图片类型枚举
	 *
	 * @param fileName 文件名称
	 * @return 图片类型枚举
	 */
	public static PictureType getType(final String fileName) {
		String extName = FileNameUtil.extName(fileName).toUpperCase();
		if ("JPG".equals(extName)) {
			extName = "JPEG";
		}

		PictureType picType;
		try {
			picType = PictureType.valueOf(extName);
		} catch (final IllegalArgumentException e) {
			// 默认值
			picType = PictureType.JPEG;
		}
		return picType;
	}
}
