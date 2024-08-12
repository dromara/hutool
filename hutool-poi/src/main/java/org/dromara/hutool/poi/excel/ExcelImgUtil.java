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

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.file.FileTypeUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.multi.ListValueMap;
import org.dromara.hutool.core.text.StrUtil;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.io.File;
import java.util.List;

/**
 * Excel图片工具类
 *
 * @author looly
 * @since 4.0.7
 */
public class ExcelImgUtil {

	/**
	 * 获取图片类型
	 *
	 * @param imgFile 图片文件
	 * @return 图片类型，默认PNG
	 * @since 6.0.0
	 */
	public static ExcelImgType getImgType(final File imgFile) {
		final String type = FileTypeUtil.getType(imgFile);
		if (StrUtil.equalsAnyIgnoreCase(type, "jpg", "jpeg")) {
			return ExcelImgType.JPEG;
		} else if (StrUtil.equalsAnyIgnoreCase(type, "emf")) {
			return ExcelImgType.EMF;
		} else if (StrUtil.equalsAnyIgnoreCase(type, "wmf")) {
			return ExcelImgType.WMF;
		} else if (StrUtil.equalsAnyIgnoreCase(type, "pict")) {
			return ExcelImgType.PICT;
		} else if (StrUtil.equalsAnyIgnoreCase(type, "dib")) {
			return ExcelImgType.DIB;
		}

		// 默认格式
		return ExcelImgType.PNG;
	}

	/**
	 * 获取工作簿指定sheet中图片列表
	 *
	 * @param workbook   工作簿{@link Workbook}
	 * @param sheetIndex sheet的索引
	 * @return 图片映射，键格式：行_列，值：{@link PictureData}
	 */
	public static ListValueMap<String, PictureData> getPicMap(final Workbook workbook, int sheetIndex) {
		Assert.notNull(workbook, "Workbook must be not null !");
		if (sheetIndex < 0) {
			sheetIndex = 0;
		}

		if (workbook instanceof HSSFWorkbook) {
			return getPicMapXls((HSSFWorkbook) workbook, sheetIndex);
		} else if (workbook instanceof XSSFWorkbook) {
			return getPicMapXlsx((XSSFWorkbook) workbook, sheetIndex);
		} else {
			throw new IllegalArgumentException(StrUtil.format("Workbook type [{}] is not supported!", workbook.getClass()));
		}
	}

	// -------------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 获取XLS工作簿指定sheet中图片列表
	 *
	 * @param workbook   工作簿{@link Workbook}
	 * @param sheetIndex sheet的索引
	 * @return 图片映射，键格式：行_列，值：{@link PictureData}
	 */
	private static ListValueMap<String, PictureData> getPicMapXls(final HSSFWorkbook workbook, final int sheetIndex) {
		final ListValueMap<String, PictureData> picMap = new ListValueMap<>();
		final List<HSSFPictureData> pictures = workbook.getAllPictures();
		if (CollUtil.isNotEmpty(pictures)) {
			final HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
			HSSFClientAnchor anchor;
			int pictureIndex;
			for (final HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
				if (shape instanceof HSSFPicture) {
					pictureIndex = ((HSSFPicture) shape).getPictureIndex() - 1;
					anchor = (HSSFClientAnchor) shape.getAnchor();
					picMap.putValue(StrUtil.format("{}_{}", anchor.getRow1(), anchor.getCol1()), pictures.get(pictureIndex));
				}
			}
		}
		return picMap;
	}

	/**
	 * 获取XLSX工作簿指定sheet中图片列表
	 *
	 * @param workbook   工作簿{@link Workbook}
	 * @param sheetIndex sheet的索引
	 * @return 图片映射，键格式：行_列，值：{@link PictureData}
	 */
	private static ListValueMap<String, PictureData> getPicMapXlsx(final XSSFWorkbook workbook, final int sheetIndex) {
		final ListValueMap<String, PictureData> sheetIndexPicMap = new ListValueMap<>();
		final XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		XSSFDrawing drawing;
		for (final POIXMLDocumentPart dr : sheet.getRelations()) {
			if (dr instanceof XSSFDrawing) {
				drawing = (XSSFDrawing) dr;
				final List<XSSFShape> shapes = drawing.getShapes();
				XSSFPicture pic;
				CTMarker ctMarker;
				for (final XSSFShape shape : shapes) {
					if (shape instanceof XSSFPicture) {
						pic = (XSSFPicture) shape;
						ctMarker = pic.getPreferredSize().getFrom();
						sheetIndexPicMap.putValue(StrUtil.format("{}_{}", ctMarker.getRow(), ctMarker.getCol()), pic.getPictureData());
					}
					// 其他类似于图表等忽略，see: https://gitee.com/dromara/hutool/issues/I38857
				}
			}
		}
		return sheetIndexPicMap;
	}
	// -------------------------------------------------------------------------------------------------------------- Private method end
}
