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

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.dromara.hutool.poi.excel.SheetUtil;
import org.dromara.hutool.poi.excel.WorkbookUtil;

import java.io.File;
import java.io.OutputStream;

/**
 * 大数据量Excel写出，只支持XLSX（Excel07版本）<br>
 * 通过封装{@link SXSSFWorkbook}，限制对滑动窗口中的行的访问来实现其低内存使用。<br>
 * 注意如果写出数据大于滑动窗口大小，就会写出到临时文件，此时写出的数据无法访问和编辑。
 *
 * @author looly
 * @since 4.1.13
 */
public class BigExcelWriter extends ExcelWriter {

	/**
	 * 默认内存中保存的行数，默认100
	 */
	public static final int DEFAULT_WINDOW_SIZE = SXSSFWorkbook.DEFAULT_WINDOW_SIZE;

	/**
	 * BigExcelWriter只能flush一次，因此调用后不再重复写出
	 */
	private boolean isFlushed;

	// -------------------------------------------------------------------------- Constructor start

	/**
	 * 构造，默认生成xlsx格式的Excel文件<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(java.io.OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link #setTargetFile(File)}方法自定义写出的文件，然后调用{@link #flush()}方法写出到文件
	 */
	public BigExcelWriter() {
		this(DEFAULT_WINDOW_SIZE);
	}

	/**
	 * 构造<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(java.io.OutputStream)}方法写出到流<br>
	 * 若写出到文件，需要调用{@link #flush(File)} 写出到文件
	 *
	 * @param rowAccessWindowSize 在内存中的行数
	 */
	public BigExcelWriter(final int rowAccessWindowSize) {
		this(WorkbookUtil.createSXSSFBook(rowAccessWindowSize), null);
	}

	/**
	 * 构造<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(java.io.OutputStream)}方法写出到流<br>
	 * 若写出到文件，需要调用{@link #flush(File)} 写出到文件
	 *
	 * @param rowAccessWindowSize   在内存中的行数，-1表示不限制，此时需要手动刷出
	 * @param compressTmpFiles      是否使用Gzip压缩临时文件
	 * @param useSharedStringsTable 是否使用共享字符串表，一般大量重复字符串时开启可节省内存
	 * @param sheetName             写出的sheet名称
	 * @since 5.7.23
	 */
	public BigExcelWriter(final int rowAccessWindowSize, final boolean compressTmpFiles, final boolean useSharedStringsTable, final String sheetName) {
		this(WorkbookUtil.createSXSSFBook(rowAccessWindowSize, compressTmpFiles, useSharedStringsTable), sheetName);
	}

	/**
	 * 构造，默认写出到第一个sheet，第一个sheet名为sheet1
	 *
	 * @param destFilePath 目标文件路径，可以不存在
	 */
	public BigExcelWriter(final String destFilePath) {
		this(destFilePath, null);
	}

	/**
	 * 构造<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(java.io.OutputStream)}方法写出到流<br>
	 * 若写出到文件，需要调用{@link #flush(File)} 写出到文件
	 *
	 * @param rowAccessWindowSize 在内存中的行数
	 * @param sheetName           sheet名，第一个sheet名并写出到此sheet，例如sheet1
	 * @since 4.1.8
	 */
	public BigExcelWriter(final int rowAccessWindowSize, final String sheetName) {
		this(WorkbookUtil.createSXSSFBook(rowAccessWindowSize), sheetName);
	}

	/**
	 * 构造
	 *
	 * @param destFilePath 目标文件路径，可以不存在
	 * @param sheetName    sheet名，第一个sheet名并写出到此sheet，例如sheet1
	 */
	public BigExcelWriter(final String destFilePath, final String sheetName) {
		this(FileUtil.file(destFilePath), sheetName);
	}

	/**
	 * 构造，默认写出到第一个sheet，第一个sheet名为sheet1
	 *
	 * @param destFile 目标文件，可以不存在
	 */
	public BigExcelWriter(final File destFile) {
		this(destFile, null);
	}

	/**
	 * 构造
	 *
	 * @param destFile  目标文件，可以不存在
	 * @param sheetName sheet名，做为第一个sheet名并写出到此sheet，例如sheet1
	 */
	public BigExcelWriter(final File destFile, final String sheetName) {
		this(destFile.exists() ? WorkbookUtil.createSXSSFBook(destFile) : WorkbookUtil.createSXSSFBook(), sheetName);
		this.targetFile = destFile;
	}

	/**
	 * 构造<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(java.io.OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link #setTargetFile(File)}方法自定义写出的文件，然后调用{@link #flush()}方法写出到文件
	 *
	 * @param workbook  {@link SXSSFWorkbook}
	 * @param sheetName sheet名，做为第一个sheet名并写出到此sheet，例如sheet1
	 */
	public BigExcelWriter(final SXSSFWorkbook workbook, final String sheetName) {
		this(SheetUtil.getOrCreateSheet(workbook, sheetName));
	}

	/**
	 * 构造<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(java.io.OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link #setTargetFile(File)}方法自定义写出的文件，然后调用{@link #flush()}方法写出到文件
	 *
	 * @param sheet {@link Sheet}
	 * @since 4.0.6
	 */
	public BigExcelWriter(final Sheet sheet) {
		super(sheet);
	}

	// -------------------------------------------------------------------------- Constructor end

	@Override
	public BigExcelWriter autoSizeColumn(final int columnIndex, final boolean useMergedCells, final float widthRatio) {
		final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
		sheet.trackColumnForAutoSizing(columnIndex);
		super.autoSizeColumn(columnIndex, useMergedCells, widthRatio);
		sheet.untrackColumnForAutoSizing(columnIndex);
		return this;
	}

	@Override
	public BigExcelWriter autoSizeColumnAll(final boolean useMergedCells, final float widthRatio) {
		final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
		sheet.trackAllColumnsForAutoSizing();
		super.autoSizeColumnAll(useMergedCells, widthRatio);
		sheet.untrackAllColumnsForAutoSizing();
		return this;
	}

	@Override
	public ExcelWriter flush(final OutputStream out, final boolean isCloseOut) throws IORuntimeException {
		if (!isFlushed) {
			isFlushed = true;
			return super.flush(out, isCloseOut);
		}
		return this;
	}

	@SuppressWarnings("resource")
	@Override
	public void close() {
		if (null != this.targetFile && !isFlushed) {
			flush();
		}

		// 清理临时文件
		IoUtil.closeIfPossible(this.workbook);
		super.closeWithoutFlush();
	}
}
