package com.xiaoleilu.hutool.poi.excel.sax;

import java.io.File;
import java.io.InputStream;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.poi.exceptions.POIException;

/**
 * 抽象的Sax方式Excel读取器，提供一些共用方法
 * @author looly
 *
 * @param <T> 子对象类型，用于标记返回值this
 * @since 3.2.0
 */
public abstract class AbstractExcelSaxReader<T> {
	// ------------------------------------------------------------------------------ Read start
	/**
	 * 开始读取Excel，读取所有sheet
	 * 
	 * @param path Excel文件路径
	 * @return this
	 * @throws POIException POI异常
	 */
	public T read(String path) throws POIException {
		return read(FileUtil.file(path));
	}

	/**
	 * 开始读取Excel，读取所有sheet
	 * 
	 * @param file Excel文件
	 * @return this
	 * @throws POIException POI异常
	 */
	public T read(File file) throws POIException {
		return read(file, -1);
	}

	/**
	 * 开始读取Excel，读取所有sheet，读取结束后并不关闭流
	 * 
	 * @param in Excel包流
	 * @return this
	 * @throws POIException POI异常
	 */
	public T read(InputStream in) throws POIException {
		return read(in, -1);
	}

	/**
	 * 开始读取Excel
	 * 
	 * @param path 文件路径
	 * @param sheetIndex Excel中的sheet编号，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	public T read(String path, int sheetIndex) throws POIException {
		return read(FileUtil.file(path), sheetIndex);
	}

	/**
	 * 开始读取Excel
	 * 
	 * @param file Excel文件
	 * @param sheetIndex Excel中的sheet编号，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	public abstract T read(File file, int sheetIndex) throws POIException;

	/**
	 * 开始读取Excel，读取结束后并不关闭流
	 * 
	 * @param in Excel流
	 * @param sheetIndex Excel中的sheet编号，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	public abstract T read(InputStream in, int sheetIndex) throws POIException;
}
