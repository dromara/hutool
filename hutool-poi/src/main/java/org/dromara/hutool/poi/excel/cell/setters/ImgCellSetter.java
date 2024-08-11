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

package org.dromara.hutool.poi.excel.cell.setters;

import org.apache.poi.ss.usermodel.*;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.poi.excel.ExcelImgType;
import org.dromara.hutool.poi.excel.ExcelImgUtil;
import org.dromara.hutool.poi.excel.SimpleClientAnchor;
import org.dromara.hutool.poi.excel.writer.ExcelDrawingUtil;

import java.io.File;

/**
 * 图片单元格值设置器
 *
 * @author Looly
 * @since 6.0.0
 */
public class ImgCellSetter implements CellSetter {

	private final byte[] pictureData;
	private final ExcelImgType imgType;

	// region ----- 构造

	/**
	 * 构造，默认PNG图片
	 *
	 * @param pictureData 图片数据
	 */
	public ImgCellSetter(final byte[] pictureData) {
		this(pictureData, ExcelImgType.PNG);
	}

	/**
	 * 构造
	 *
	 * @param picturefile 图片数据
	 */
	public ImgCellSetter(final File picturefile) {
		this(FileUtil.readBytes(picturefile), ExcelImgUtil.getImgType(picturefile));
	}

	/**
	 * 构造
	 *
	 * @param pictureData 图片数据
	 * @param imgType     图片类型
	 */
	public ImgCellSetter(final byte[] pictureData, final ExcelImgType imgType) {
		this.pictureData = pictureData;
		this.imgType = imgType;
	}
	// endregion

	@Override
	public void setValue(final Cell cell) {
		final Sheet sheet = cell.getSheet();
		final int columnIndex = cell.getColumnIndex();
		final int rowIndex = cell.getRowIndex();

		ExcelDrawingUtil.drawingImg(sheet, this.pictureData, this.imgType,
			new SimpleClientAnchor(columnIndex, rowIndex, columnIndex + 1, rowIndex + 1));
	}
}
