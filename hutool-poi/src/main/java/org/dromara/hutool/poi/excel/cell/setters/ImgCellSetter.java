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
