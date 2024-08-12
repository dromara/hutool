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

package org.dromara.hutool.poi.excel.writer;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSimpleShape;
import org.dromara.hutool.poi.excel.ExcelImgType;
import org.dromara.hutool.poi.excel.SimpleClientAnchor;
import org.dromara.hutool.poi.excel.style.ShapeConfig;

import java.awt.Color;

/**
 * Excel绘制工具类<br>
 * 用于辅助写出指定的图形
 *
 * @author Looly
 * @since 6.0.0
 */
public class ExcelDrawingUtil {

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
	public static void drawingImg(final Sheet sheet, final byte[] pictureData,
								  final ExcelImgType imgType, final SimpleClientAnchor clientAnchor) {
		final Drawing<?> patriarch = sheet.createDrawingPatriarch();
		final Workbook workbook = sheet.getWorkbook();
		final ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
		clientAnchor.copyTo(anchor);

		patriarch.createPicture(anchor, workbook.addPicture(pictureData, imgType.getValue()));
	}

	/**
	 * 绘制简单形状
	 *
	 * @param sheet        {@link Sheet}
	 * @param clientAnchor 绘制区域信息
	 * @param shapeConfig  形状配置，包括形状类型、线条样式、线条宽度、线条颜色、填充颜色等
	 * @since 6.0.0
	 */
	public static void drawingSimpleShape(final Sheet sheet, final SimpleClientAnchor clientAnchor, ShapeConfig shapeConfig) {
		final Drawing<?> patriarch = sheet.createDrawingPatriarch();
		final ClientAnchor anchor = sheet.getWorkbook().getCreationHelper().createClientAnchor();
		clientAnchor.copyTo(anchor);

		if (null == shapeConfig) {
			shapeConfig = ShapeConfig.of();
		}
		final Color lineColor = shapeConfig.getLineColor();
		if (patriarch instanceof HSSFPatriarch) {
			final HSSFSimpleShape simpleShape = ((HSSFPatriarch) patriarch).createSimpleShape((HSSFClientAnchor) anchor);
			simpleShape.setShapeType(shapeConfig.getShapeType().ooxmlId);
			simpleShape.setLineStyle(shapeConfig.getLineStyle().getValue());
			simpleShape.setLineWidth(shapeConfig.getLineWidth());
			if (null != lineColor) {
				simpleShape.setLineStyleColor(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue());
			}
		} else if (patriarch instanceof XSSFDrawing) {
			final XSSFSimpleShape simpleShape = ((XSSFDrawing) patriarch).createSimpleShape((XSSFClientAnchor) anchor);
			simpleShape.setShapeType(shapeConfig.getShapeType().ooxmlId);
			simpleShape.setLineStyle(shapeConfig.getLineStyle().getValue());
			simpleShape.setLineWidth(shapeConfig.getLineWidth());
			if (null != lineColor) {
				simpleShape.setLineStyleColor(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue());
			}
		} else {
			throw new UnsupportedOperationException("Unsupported patriarch type: " + patriarch.getClass().getName());
		}
	}

	/**
	 * 添加批注
	 *
	 * @param cell {@link Cell}
	 * @param clientAnchor 绘制区域信息
	 * @param content 内容
	 */
	public static void drawingCellComment(final Cell cell, final SimpleClientAnchor clientAnchor, final String content){
		final Sheet sheet = cell.getSheet();
		final Drawing<?> patriarch = sheet.createDrawingPatriarch();
		final Workbook workbook = sheet.getWorkbook();
		final ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
		clientAnchor.copyTo(anchor);

		final RichTextString richTextString = workbook.getCreationHelper().createRichTextString(content);
		final Comment cellComment = patriarch.createCellComment(anchor);
		cellComment.setString(richTextString);

		cell.setCellComment(cellComment);
	}
}
