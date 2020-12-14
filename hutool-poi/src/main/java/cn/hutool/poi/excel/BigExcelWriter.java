package cn.hutool.poi.excel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.OutputStream;

/**
 * 大数据量Excel写出
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
	 * 构造，默认生成xls格式的Excel文件<br>
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
	 * @param sheetName sheet名，第一个sheet名并写出到此sheet，例如sheet1
	 * @since 4.1.8
	 */
	public BigExcelWriter(int rowAccessWindowSize, String sheetName) {
		this(WorkbookUtil.createSXSSFBook(rowAccessWindowSize), sheetName);
	}

	/**
	 * 构造
	 * 
	 * @param destFilePath 目标文件路径，可以不存在
	 * @param sheetName sheet名，第一个sheet名并写出到此sheet，例如sheet1
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
	 * @param destFile 目标文件，可以不存在
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
	 * @param workbook {@link SXSSFWorkbook}
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

	@Override
	public BigExcelWriter autoSizeColumn(int columnIndex) {
		final SXSSFSheet sheet = (SXSSFSheet)this.sheet;
		sheet.trackColumnForAutoSizing(columnIndex);
		super.autoSizeColumn(columnIndex);
		sheet.untrackColumnForAutoSizing(columnIndex);
		return this;
	}

	@Override
	public BigExcelWriter autoSizeColumnAll() {
		final SXSSFSheet sheet = (SXSSFSheet)this.sheet;
		sheet.trackAllColumnsForAutoSizing();
		super.autoSizeColumnAll();
		sheet.untrackAllColumnsForAutoSizing();
		return this;
	}

	@Override
	public ExcelWriter flush(OutputStream out, boolean isCloseOut) throws IORuntimeException {
		if(false == isFlushed){
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
