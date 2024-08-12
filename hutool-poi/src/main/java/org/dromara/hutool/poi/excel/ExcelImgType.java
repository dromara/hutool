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

package org.dromara.hutool.poi.excel;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel支持的图片类型枚举
 *
 * @author Looly
 * @see Workbook#PICTURE_TYPE_EMF
 * @see Workbook#PICTURE_TYPE_WMF
 * @see Workbook#PICTURE_TYPE_PICT
 * @see Workbook#PICTURE_TYPE_JPEG
 * @see Workbook#PICTURE_TYPE_PNG
 * @see Workbook#PICTURE_TYPE_DIB
 * @since 6.0.0
 */
public enum ExcelImgType {
	/**
	 * Extended windows meta file
	 */
	EMF(Workbook.PICTURE_TYPE_EMF),

	/**
	 * Windows Meta File
	 */
	WMF(Workbook.PICTURE_TYPE_WMF),

	/**
	 * Mac PICT format
	 */
	PICT(Workbook.PICTURE_TYPE_PICT),

	/**
	 * JPEG format
	 */
	JPEG(Workbook.PICTURE_TYPE_JPEG),

	/**
	 * PNG format
	 */
	PNG(Workbook.PICTURE_TYPE_PNG),

	/**
	 * Device independent bitmap
	 */
	DIB(Workbook.PICTURE_TYPE_DIB);

	private final int value;

	/**
	 * 构造
	 *
	 * @param value 类型编码
	 */
	ExcelImgType(final int value) {
		this.value = value;
	}

	/**
	 * 获取类型编码
	 *
	 * @return 编码
	 */
	public int getValue() {
		return this.value;
	}
}
