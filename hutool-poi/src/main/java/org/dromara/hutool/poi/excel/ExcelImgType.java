/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
