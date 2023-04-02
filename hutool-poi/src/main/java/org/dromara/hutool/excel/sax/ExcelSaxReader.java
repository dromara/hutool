/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.excel.sax;

import org.dromara.hutool.exceptions.POIException;
import org.dromara.hutool.io.file.FileUtil;

import java.io.File;
import java.io.InputStream;

/**
 * Sax方式读取Excel接口，提供一些共用方法
 * @author looly
 *
 * @param <T> 子对象类型，用于标记返回值this
 * @since 3.2.0
 */
public interface ExcelSaxReader<T> {

	/**
	 * sheet r:Id前缀
	 */
	String RID_PREFIX = "rId";
	/**
	 * sheet name前缀
	 */
	String SHEET_NAME_PREFIX = "sheetName:";

	/**
	 * 开始读取Excel
	 *
	 * @param file Excel文件
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号或sheet名称，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	T read(File file, String idOrRidOrSheetName) throws POIException;

	/**
	 * 开始读取Excel，读取结束后并不关闭流
	 *
	 * @param in Excel流
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	T read(InputStream in, String idOrRidOrSheetName) throws POIException;

	/**
	 * 开始读取Excel，读取所有sheet
	 *
	 * @param path Excel文件路径
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final String path) throws POIException {
		return read(FileUtil.file(path));
	}

	/**
	 * 开始读取Excel，读取所有sheet
	 *
	 * @param file Excel文件
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final File file) throws POIException {
		return read(file, -1);
	}

	/**
	 * 开始读取Excel，读取所有sheet，读取结束后并不关闭流
	 *
	 * @param in Excel包流
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final InputStream in) throws POIException {
		return read(in, -1);
	}

	/**
	 * 开始读取Excel
	 *
	 * @param path 文件路径
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号或sheet名称，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final String path, final int idOrRidOrSheetName) throws POIException {
		return read(FileUtil.file(path), idOrRidOrSheetName);
	}

	/**
	 * 开始读取Excel
	 *
	 * @param path 文件路径
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号或sheet名称，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final String path, final String idOrRidOrSheetName) throws POIException {
		return read(FileUtil.file(path), idOrRidOrSheetName);
	}

	/**
	 * 开始读取Excel
	 *
	 * @param file Excel文件
	 * @param rid Excel中的sheet rid编号，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final File file, final int rid) throws POIException{
		return read(file, String.valueOf(rid));
	}

	/**
	 * 开始读取Excel，读取结束后并不关闭流
	 *
	 * @param in Excel流
	 * @param rid Excel中的sheet rid编号，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final InputStream in, final int rid) throws POIException{
		return read(in, String.valueOf(rid));
	}
}
