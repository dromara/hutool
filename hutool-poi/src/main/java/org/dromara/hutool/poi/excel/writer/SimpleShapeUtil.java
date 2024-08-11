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

package org.dromara.hutool.poi.excel.writer;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSimpleShape;
import org.dromara.hutool.poi.excel.SimpleClientAnchor;
import org.dromara.hutool.poi.excel.style.ShapeConfig;

import java.awt.Color;

/**
 * 简单形状工具类<br>
 * 用于辅助写出指定的图形
 *
 * @author Looly
 * @since 6.0.0
 */
public class SimpleShapeUtil {

	/**
	 * 绘制简单形状
	 *
	 * @param sheet        {@link Sheet}
	 * @param clientAnchor 绘制区域信息
	 * @param shapeConfig  形状配置，包括形状类型、线条样式、线条宽度、线条颜色、填充颜色等
	 * @since 6.0.0
	 */
	public static void writeSimpleShape(final Sheet sheet, final SimpleClientAnchor clientAnchor, ShapeConfig shapeConfig) {
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
}
