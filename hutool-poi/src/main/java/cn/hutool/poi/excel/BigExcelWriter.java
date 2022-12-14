package cn.hutool.poi.excel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.OutputStream;
import java.util.Collection;

/**
 * 大数据量Excel写出，只支持XLSX（Excel07版本）<br>
 * 通过封装{@link SXSSFWorkbook}，限制对滑动窗口中的行的访问来实现其低内存使用。<br>
 * 注意如果写出数据大于滑动窗口大小，就会写出到临时文件，此时写出的数据无法访问和编辑。
 *
 * @author looly
 * @since 4.1.13
 */
public class BigExcelWriter extends ExcelWriter {

	public static final int DEFAULT_WINDOW_SIZE = SXSSFWorkbook.DEFAULT_WINDOW_SIZE;

	/**
	 * BigExcelWriter只能flush一次，因此调用后不再重复写出
	 */
	private boolean isFlushed;

	// -------------------------------------------------------------------------- Constructor start

	/**
	 * 构造，默认生成xlsx格式的Excel文件<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(java.io.OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link #setDestFile(File)}方法自定义写出的文件，然后调用{@link #flush()}方法写出到文件
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
	public BigExcelWriter(int rowAccessWindowSize) {
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
	public BigExcelWriter(int rowAccessWindowSize, boolean compressTmpFiles, boolean useSharedStringsTable, String sheetName) {
		this(WorkbookUtil.createSXSSFBook(rowAccessWindowSize, compressTmpFiles, useSharedStringsTable), sheetName);
	}

	/**
	 * 构造，默认写出到第一个sheet，第一个sheet名为sheet1
	 *
	 * @param destFilePath 目标文件路径，可以不存在
	 */
	public BigExcelWriter(String destFilePath) {
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
	public BigExcelWriter(int rowAccessWindowSize, String sheetName) {
		this(WorkbookUtil.createSXSSFBook(rowAccessWindowSize), sheetName);
	}

	/**
	 * 构造
	 *
	 * @param destFilePath 目标文件路径，可以不存在
	 * @param sheetName    sheet名，第一个sheet名并写出到此sheet，例如sheet1
	 */
	public BigExcelWriter(String destFilePath, String sheetName) {
		this(FileUtil.file(destFilePath), sheetName);
	}

	/**
	 * 构造，默认写出到第一个sheet，第一个sheet名为sheet1
	 *
	 * @param destFile 目标文件，可以不存在
	 */
	public BigExcelWriter(File destFile) {
		this(destFile, null);
	}

	/**
	 * 构造
	 *
	 * @param destFile  目标文件，可以不存在
	 * @param sheetName sheet名，做为第一个sheet名并写出到此sheet，例如sheet1
	 */
	public BigExcelWriter(File destFile, String sheetName) {
		this(destFile.exists() ? WorkbookUtil.createSXSSFBook(destFile) : WorkbookUtil.createSXSSFBook(), sheetName);
		this.destFile = destFile;
	}

	/**
	 * 构造<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(java.io.OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link #setDestFile(File)}方法自定义写出的文件，然后调用{@link #flush()}方法写出到文件
	 *
	 * @param workbook  {@link SXSSFWorkbook}
	 * @param sheetName sheet名，做为第一个sheet名并写出到此sheet，例如sheet1
	 */
	public BigExcelWriter(SXSSFWorkbook workbook, String sheetName) {
		this(WorkbookUtil.getOrCreateSheet(workbook, sheetName));
	}

	/**
	 * 构造<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(java.io.OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link #setDestFile(File)}方法自定义写出的文件，然后调用{@link #flush()}方法写出到文件
	 *
	 * @param sheet {@link Sheet}
	 * @since 4.0.6
	 */
	public BigExcelWriter(Sheet sheet) {
		super(sheet);
	}

	// -------------------------------------------------------------------------- Constructor end

	/**
	 * 设置某列为自动宽度，不考虑合并单元格<br>
	 * 参与计算该列宽度的行为滑动窗口中的行
	 *
	 * @param columnIndex 第几列，从0计数
	 * @return this
	 */
	@Override
	public BigExcelWriter autoSizeColumn(int columnIndex) {
		final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
		sheet.trackColumnForAutoSizing(columnIndex);
		super.autoSizeColumn(columnIndex);
		sheet.untrackColumnForAutoSizing(columnIndex);
		return this;
	}

	/**
	 * 设置某列为自动宽度<br>
	 * 参与计算该列宽度的行为滑动窗口中的行
	 *
	 * @param columnIndex    第几列，从0计数
	 * @param useMergedCells 是否适用于合并单元格
	 * @return this
	 */
	@Override
	public BigExcelWriter autoSizeColumn(int columnIndex, boolean useMergedCells) {
		final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
		sheet.trackColumnForAutoSizing(columnIndex);
		super.autoSizeColumn(columnIndex);
		sheet.untrackColumnForAutoSizing(columnIndex);
		return this;
	}

	/**
	 * 设置所有列为自动宽度，不考虑合并单元格<br>
	 * 参与计算该列宽度的行为滑动窗口中的行<br>
	 * 列数计算是通过最后写入的行计算的
	 *
	 * @return this
	 */
	@Override
	public BigExcelWriter autoSizeColumnAll() {
		final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
		sheet.trackAllColumnsForAutoSizing();
		super.autoSizeColumnAll();
		sheet.untrackAllColumnsForAutoSizing();
		return this;
	}

	/**
	 * 设置某列为自动宽度，不考虑合并单元格<br>
	 * 参与计算该列宽度的行包括该列被追踪后所有写出到临时文件的行和调用本函数时滑动窗口中的行
	 *
	 * @param columnIndex 第几列，从0计数
	 * @return this
	 * @see #trackColumnForAutoSizing(int)
	 * @see #trackColumnsForAutoSizing(Collection)
	 * @see #trackAllColumnsForAutoSizing()
	 */
	public BigExcelWriter autoSizeTrackedColumn(int columnIndex) {
		super.autoSizeColumn(columnIndex);
		return this;
	}

	/**
	 * 设置某列为自动宽度<br>
	 * 参与计算该列宽度的行包括该列被追踪后所有写出到临时文件的行和调用本函数时滑动窗口中的行
	 *
	 * @param columnIndex    第几列，从0计数
	 * @param useMergedCells 是否适用于合并单元格
	 * @return this
	 * @see #trackColumnForAutoSizing(int)
	 * @see #trackColumnsForAutoSizing(Collection)
	 * @see #trackAllColumnsForAutoSizing()
	 */
	public BigExcelWriter autoSizeTrackedColumn(int columnIndex, boolean useMergedCells) {
		super.autoSizeColumn(columnIndex);
		return this;
	}

	/**
	 * 设置所有列为自动宽度，不考虑合并单元格<br>
	 * 参与计算某列宽度的行包括该列被追踪后所有写出到临时文件的行和调用本函数时滑动窗口中的行
	 *
	 * @return this
	 * @see #trackColumnForAutoSizing(int)
	 * @see #trackColumnsForAutoSizing(Collection)
	 * @see #trackAllColumnsForAutoSizing()
	 */
	public BigExcelWriter autoSizeTrackedColumnAll() {
		super.autoSizeColumnAll();
		return this;
	}

	/**
	 * 获取总列数，受{@link SXSSFWorkbook#getRandomAccessWindowSize()}限制，若某行不在内存中，其列数为-1，计算方法为：
	 *
	 * <pre>
	 * 最后写入的行的最后一列序号 + 1
	 * </pre>
	 *
	 * @return 列数
	 */
	@Override
	public int getColumnCount() {
		return getColumnCount(getCurrentRow() - 1);
	}

	/**
	 * 追踪列，保存最大列宽
	 *
	 * @param columnIndex 要追踪的列
	 * @return this
	 */
	public BigExcelWriter trackColumnForAutoSizing(int columnIndex) {
		final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
		sheet.trackColumnForAutoSizing(columnIndex);
		return this;
	}

	/**
	 * 追踪多个列，保存最大列宽
	 *
	 * @param columns 要追踪的列
	 * @return this
	 */
	public BigExcelWriter trackColumnsForAutoSizing(Collection<Integer> columns) {
		final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
		sheet.trackColumnsForAutoSizing(columns);
		return this;
	}

	/**
	 * 追踪所有列，保存最大列宽
	 *
	 * @return this
	 */
	public BigExcelWriter trackAllColumnsForAutoSizing() {
		final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
		sheet.trackAllColumnsForAutoSizing();
		return this;
	}

	/**
	 * 停止追踪列，清除最大列宽
	 *
	 * @param columnIndex 要追踪的列
	 * @return this
	 */
	public BigExcelWriter untrackColumnForAutoSizing(int columnIndex) {
		final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
		sheet.untrackColumnForAutoSizing(columnIndex);
		return this;
	}

	/**
	 * 停止追踪多个列，清除最大列宽
	 *
	 * @param columns 要追踪的列
	 * @return this
	 */
	public BigExcelWriter untrackColumnsForAutoSizing(Collection<Integer> columns) {
		final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
		sheet.untrackColumnsForAutoSizing(columns);
		return this;
	}

	/**
	 * 停止追踪所有列，清除最大列宽
	 *
	 * @return this
	 */
	public BigExcelWriter untrackAllColumnsForAutoSizing() {
		final SXSSFSheet sheet = (SXSSFSheet) this.sheet;
		sheet.untrackAllColumnsForAutoSizing();
		return this;
	}

	@Override
	public ExcelWriter flush(OutputStream out, boolean isCloseOut) throws IORuntimeException {
		if (false == isFlushed) {
			isFlushed = true;
			return super.flush(out, isCloseOut);
		}
		return this;
	}

	@Override
	public void close() {
		if (null != this.destFile && false == isFlushed) {
			flush();
		}

		// 清理临时文件
		((SXSSFWorkbook) this.workbook).dispose();
		super.closeWithoutFlush();
	}
}
