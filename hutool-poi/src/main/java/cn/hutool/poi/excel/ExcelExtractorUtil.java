package cn.hutool.poi.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.extractor.ExcelExtractor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * {@link ExcelExtractor}工具封装
 *
 * @author looly
 * @since 5.4.4
 */
public class ExcelExtractorUtil {
	/**
	 * 获取 {@link ExcelExtractor} 对象
	 *
	 * @param wb {@link Workbook}
	 * @return {@link ExcelExtractor}
	 */
	public static ExcelExtractor getExtractor(Workbook wb) {
		ExcelExtractor extractor;
		if (wb instanceof HSSFWorkbook) {
			extractor = new org.apache.poi.hssf.extractor.ExcelExtractor((HSSFWorkbook) wb);
		} else {
			extractor = new XSSFExcelExtractor((XSSFWorkbook) wb);
		}
		return extractor;
	}

	/**
	 * 读取为文本格式<br>
	 * 使用{@link ExcelExtractor} 提取Excel内容
	 *
	 * @param wb            {@link Workbook}
	 * @param withSheetName 是否附带sheet名
	 * @return Excel文本
	 * @since 4.1.0
	 */
	public static String readAsText(Workbook wb, boolean withSheetName) {
		final ExcelExtractor extractor = getExtractor(wb);
		extractor.setIncludeSheetNames(withSheetName);
		return extractor.getText();
	}
}
