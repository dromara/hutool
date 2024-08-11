/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
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

	/**
	 * 写出图片，本方法只是将数据写入Workbook中的Sheet，并不写出到文件<br>
	 * 添加图片到当前sheet中
	 *
	 * @param sheet {@link Sheet}
	 * @param pictureData  数据bytes
	 * @param imgType      图片类型，对应poi中Workbook类中的图片类型2-7变量
	 * @param clientAnchor 图片的位置和大小信息
	 * @author vhukze
	 * @since 6.0.0
	 */
	public static void writeImg(final Sheet sheet, final byte[] pictureData,
								final ExcelImgType imgType, final SimpleClientAnchor clientAnchor) {
		final Drawing<?> patriarch = sheet.createDrawingPatriarch();
		final Workbook workbook = sheet.getWorkbook();
		final ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
		clientAnchor.copyTo(anchor);

		patriarch.createPicture(anchor, workbook.addPicture(pictureData, imgType.getValue()));
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
