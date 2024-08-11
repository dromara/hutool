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
import org.dromara.hutool.poi.excel.ExcelImgUtil;

import java.io.File;

/**
 * 图片单元格值设置器
 *
 * @author Looly
 * @since 6.0.0
 */
public class ImgCellSetter implements CellSetter {

	private final byte[] pictureData;
	private final int imgType;

	// region ----- 构造

	/**
	 * 构造，默认PNG图片
	 *
	 * @param pictureData 图片数据
	 */
	public ImgCellSetter(final byte[] pictureData) {
		this(pictureData, Workbook.PICTURE_TYPE_PNG);
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
	public ImgCellSetter(final byte[] pictureData, final int imgType) {
		this.pictureData = pictureData;
		this.imgType = imgType;
	}
	// endregion

	@Override
	public void setValue(final Cell cell) {
		final Sheet sheet = cell.getSheet();
		final Workbook workbook = sheet.getWorkbook();
		final Drawing<?> patriarch = sheet.createDrawingPatriarch();
		final ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();

		final int columnIndex = cell.getColumnIndex();
		final int rowIndex = cell.getRowIndex();
		// 填充当前单元格
		anchor.setCol1(columnIndex);
		anchor.setRow1(rowIndex);
		anchor.setCol2(columnIndex + 1);
		anchor.setRow2(rowIndex + 1);

		patriarch.createPicture(anchor, workbook.addPicture(this.pictureData, this.imgType));
	}
}
