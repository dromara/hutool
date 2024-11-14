/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.poi.excel.shape;

import org.apache.poi.ss.usermodel.*;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.file.FileTypeUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.stream.StreamUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Excel图片工具类
 *
 * @author looly
 * @since 4.0.7
 */
public class ExcelPicUtil {

	/**
	 * 获取图片类型
	 *
	 * @param picFile 图片文件
	 * @return 图片类型，默认PNG
	 * @since 6.0.0
	 */
	public static ExcelPicType getPicType(final File picFile) {
		final String type = FileTypeUtil.getType(picFile);
		if (StrUtil.equalsAnyIgnoreCase(type, "jpg", "jpeg")) {
			return ExcelPicType.JPEG;
		} else if (StrUtil.equalsAnyIgnoreCase(type, "emf")) {
			return ExcelPicType.EMF;
		} else if (StrUtil.equalsAnyIgnoreCase(type, "wmf")) {
			return ExcelPicType.WMF;
		} else if (StrUtil.equalsAnyIgnoreCase(type, "pict")) {
			return ExcelPicType.PICT;
		} else if (StrUtil.equalsAnyIgnoreCase(type, "dib")) {
			return ExcelPicType.DIB;
		}

		// 默认格式
		return ExcelPicType.PNG;
	}

	/**
	 * 写入图片到文件
	 *
	 * @param pic  图片数据
	 * @param file 文件
	 */
	public static void writePicTo(final Picture pic, final File file) {
		writePicTo(pic.getPictureData(), file);
	}

	/**
	 * 写入图片到文件
	 *
	 * @param pic  图片数据
	 * @param file 文件
	 */
	public static void writePicTo(final PictureData pic, final File file) {
		FileUtil.writeBytes(pic.getData(), file);
	}

	/**
	 * 获取所有图片列表
	 *
	 * @param workbook 工作簿{@link Workbook}
	 * @return 图片列表
	 */
	public static List<? extends PictureData> getAllPictures(final Workbook workbook) {
		return workbook.getAllPictures();
	}

	/**
	 * 获取工作簿指定sheet中绘制的图片列表
	 *
	 * @param workbook   工作簿{@link Workbook}
	 * @param sheetIndex sheet的索引
	 * @return 图片映射，键格式：行_列，值：{@link PictureData}
	 */
	public static List<Picture> getShapePics(final Workbook workbook, int sheetIndex) {
		Assert.notNull(workbook, "Workbook must be not null !");
		if (sheetIndex < 0) {
			sheetIndex = 0;
		}

		return getShapePics(workbook.getSheetAt(sheetIndex));
	}

	/**
	 * 获取工作簿指定sheet中绘制的图片列表<br>
	 * 结果中{@link Picture#getClientAnchor()}标识位置信息，{@link Picture#getPictureData()}标识图片数据
	 *
	 * @param sheet 工作表{@link Sheet}
	 * @return 图片列表
	 */
	public static List<Picture> getShapePics(final Sheet sheet) {
		Assert.notNull(sheet, "Sheet must be not null !");

		final Drawing<?> drawing = sheet.getDrawingPatriarch();
		if (null == drawing) {
			return ListUtil.empty();
		}

		return StreamUtil.of(drawing)
			.filter(shape -> shape instanceof Picture)
			.map(shape -> (Picture) shape)
			.collect(Collectors.toList());
	}
}
