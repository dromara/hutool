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
