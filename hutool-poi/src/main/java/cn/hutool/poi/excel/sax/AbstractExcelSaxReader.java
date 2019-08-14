package cn.hutool.poi.excel.sax;

import java.io.File;
import java.io.InputStream;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.exceptions.POIException;

/**
 * 抽象的Sax方式Excel读取器，提供一些共用方法
 * 
 * @author looly
 *
 * @param <T> 子对象类型，用于标记返回值this
 * @since 3.2.0
 */
public abstract class AbstractExcelSaxReader<T> implements ExcelSaxReader<T> {
	
	@Override
	public T read(String path) throws POIException {
		return read(FileUtil.file(path));
	}

	@Override
	public T read(File file) throws POIException {
		return read(file, -1);
	}

	@Override
	public T read(InputStream in) throws POIException {
		return read(in, -1);
	}

	@Override
	public T read(String path, int sheetIndex) throws POIException {
		return read(FileUtil.file(path), sheetIndex);
	}
}
