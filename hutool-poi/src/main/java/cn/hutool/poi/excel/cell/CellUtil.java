package cn.hutool.poi.excel.cell;

import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.StyleSet;
import cn.hutool.poi.excel.editors.TrimEditor;

/**
 * Excel表格中单元格工具类
 * @author looly
 *@since 4.0.7
 */
public class CellUtil {
	/**
	 * 获取单元格值
	 * 
	 * @param cell {@link Cell}单元格
	 * @param isTrimCellValue 如果单元格类型为字符串，是否去掉两边空白符
	 * @return 值，类型可能为：Date、Double、Boolean、String
	 */
	public static Object getCellValue(Cell cell, boolean isTrimCellValue) {
		return getCellValue(cell, cell.getCellTypeEnum(), isTrimCellValue);
	}

	/**
	 * 获取单元格值
	 * 
	 * @param cell {@link Cell}单元格
	 * @param cellEditor 单元格值编辑器。可以通过此编辑器对单元格值做自定义操作
	 * @return 值，类型可能为：Date、Double、Boolean、String
	 */
	public static Object getCellValue(Cell cell, CellEditor cellEditor) {
		if (null == cell) {
			return null;
		}
		return getCellValue(cell, cell.getCellTypeEnum(), cellEditor);
	}

	/**
	 * 获取单元格值
	 * 
	 * @param cell {@link Cell}单元格
	 * @param cellType 单元格值类型{@link CellType}枚举
	 * @param isTrimCellValue 如果单元格类型为字符串，是否去掉两边空白符
	 * @return 值，类型可能为：Date、Double、Boolean、String
	 */
	public static Object getCellValue(Cell cell, CellType cellType, final boolean isTrimCellValue) {
		return getCellValue(cell, cellType, isTrimCellValue ? new TrimEditor() : null);
	}

	/**
	 * 获取单元格值<br>
	 * 如果单元格值为数字格式，则判断其格式中是否有小数部分，无则返回Long类型，否则返回Double类型
	 * 
	 * @param cell {@link Cell}单元格
	 * @param cellType 单元格值类型{@link CellType}枚举，如果为{@code null}默认使用cell的类型
	 * @param cellEditor 单元格值编辑器。可以通过此编辑器对单元格值做自定义操作
	 * @return 值，类型可能为：Date、Double、Boolean、String
	 */
	public static Object getCellValue(Cell cell, CellType cellType, CellEditor cellEditor) {
		if (null == cell) {
			return null;
		}
		if (null == cellType) {
			cellType = cell.getCellTypeEnum();
		}

		Object value;
		switch (cellType) {
		case NUMERIC:
			value = getNumericValue(cell);
			break;
		case BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case FORMULA:
			// 遇到公式时查找公式结果类型
			value = getCellValue(cell, cell.getCachedFormulaResultTypeEnum(), cellEditor);
			break;
		case BLANK:
			value = StrUtil.EMPTY;
			break;
		case ERROR:
			final FormulaError error = FormulaError.forInt(cell.getErrorCellValue());
			value = (null == error) ? StrUtil.EMPTY : error.getString();
			break;
		default:
			value = cell.getStringCellValue();
		}

		return null == cellEditor ? value : cellEditor.edit(cell, value);
	}

	/**
	 * 设置单元格值<br>
	 * 根据传入的styleSet自动匹配样式<br>
	 * 当为头部样式时默认赋值头部样式，但是头部中如果有数字、日期等类型，将按照数字、日期样式设置
	 * 
	 * @param cell 单元格
	 * @param value 值
	 * @param styleSet 单元格样式集，包括日期等样式
	 * @param isHeader 是否为标题单元格
	 */
	public static void setCellValue(Cell cell, Object value, StyleSet styleSet, boolean isHeader) {
		final CellStyle headCellStyle = styleSet.getHeadCellStyle();
		final CellStyle cellStyle = styleSet.getCellStyle();
		if(isHeader && null != headCellStyle) {
			cell.setCellStyle(headCellStyle);
		} else if (null != cellStyle) {
			cell.setCellStyle(cellStyle);
		}
		
		if (null == value) {
			cell.setCellValue(StrUtil.EMPTY);
		}else if (value instanceof FormulaCellValue) {
			//公式
			cell.setCellFormula(((FormulaCellValue)value).getValue());
		} else if (value instanceof Date) {
			if (null != styleSet && null != styleSet.getCellStyleForDate()) {
				cell.setCellStyle(styleSet.getCellStyleForDate());
			}
			cell.setCellValue((Date) value);
		} else if (value instanceof Calendar) {
			cell.setCellValue((Calendar) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof RichTextString) {
			cell.setCellValue((RichTextString) value);
		} else if (value instanceof Number) {
			if ((value instanceof Double || value instanceof Float) && null != styleSet && null != styleSet.getCellStyleForNumber()) {
				cell.setCellStyle(styleSet.getCellStyleForNumber());
			}
			cell.setCellValue(((Number) value).doubleValue());
		} else {
			cell.setCellValue(value.toString());
		}
	}

	/**
	 * 获取已有行或创建新行
	 * 
	 * @param row Excel表的行
	 * @param cellIndex 列号
	 * @return {@link Row}
	 * @since 4.0.2
	 */
	public static Cell getOrCreateCell(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (null == cell) {
			cell = row.createCell(cellIndex);
		}
		return cell;
	}
	
	/**
	 * 判断指定的单元格是否是合并单元格
	 * 
	 * @param sheet {@link Sheet}
	 * @param row 行号
	 * @param column 列号
	 * @return 是否是合并单元格
	 */
	public static boolean isMergedRegion(Sheet sheet, int row, int column) {
		final int sheetMergeCount = sheet.getNumMergedRegions();
		CellRangeAddress ca;
		for (int i = 0; i < sheetMergeCount; i++) {
			ca = sheet.getMergedRegion(i);
			if (row >= ca.getFirstRow() && row <= ca.getLastRow() && column >= ca.getFirstColumn() && column <= ca.getLastColumn()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 合并单元格，可以根据设置的值来合并行和列
	 * 
	 * @param sheet 表对象
	 * @param firstRow 起始行，0开始
	 * @param lastRow 结束行，0开始
	 * @param firstColumn 起始列，0开始
	 * @param lastColumn 结束列，0开始
	 * @param cellStyle 单元格样式，只提取边框样式
	 * @return 合并后的单元格号
	 */
	public static int mergingCells(Sheet sheet, int firstRow, int lastRow, int firstColumn, int lastColumn, CellStyle cellStyle) {
		final CellRangeAddress cellRangeAddress = new CellRangeAddress(//
				firstRow, // first row (0-based)
				lastRow, // last row (0-based)
				firstColumn, // first column (0-based)
				lastColumn // last column (0-based)
		);

		if (null != cellStyle) {
			RegionUtil.setBorderTop(cellStyle.getBorderTopEnum(), cellRangeAddress, sheet);
			RegionUtil.setBorderRight(cellStyle.getBorderRightEnum(), cellRangeAddress, sheet);
			RegionUtil.setBorderBottom(cellStyle.getBorderBottomEnum(), cellRangeAddress, sheet);
			RegionUtil.setBorderLeft(cellStyle.getBorderLeftEnum(), cellRangeAddress, sheet);
		}
		return sheet.addMergedRegion(cellRangeAddress);
	}
	
	// -------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 获取数字类型的单元格值
	 * 
	 * @param cell 单元格
	 * @return 单元格值，可能为Long、Double、Date
	 */
	private static Object getNumericValue(Cell cell) {
		final double value = cell.getNumericCellValue();

		final CellStyle style = cell.getCellStyle();
		if (null == style) {
			return value;
		}

		final short formatIndex = style.getDataFormat();
		// 判断是否为日期
		if (isDateType(cell, formatIndex)) {
			return DateUtil.date(cell.getDateCellValue());// 使用Hutool的DateTime包装
		}

		final String format = style.getDataFormatString();
		// 普通数字
		if (null != format && format.indexOf(StrUtil.C_DOT) < 0) {
			final long longPart = (long) value;
			if (longPart == value) {
				// 对于无小数部分的数字类型，转为Long
				return longPart;
			}
		}
		return value;
	}

	/**
	 * 是否为日期格式<br>
	 * 判断方式：
	 * 
	 * <pre>
	 * 1、指定序号
	 * 2、org.apache.poi.ss.usermodel.DateUtil.isADateFormat方法判定
	 * </pre>
	 * 
	 * @param formatIndex 格式序号
	 * @param format 格式字符串
	 * @return 是否为日期格式
	 */
	private static boolean isDateType(Cell cell, int formatIndex) {
		// yyyy-MM-dd----- 14
		// yyyy年m月d日---- 31
		// yyyy年m月------- 57
		// m月d日 ---------- 58
		// HH:mm----------- 20
		// h时mm分 -------- 32
		if (formatIndex == 14 || formatIndex == 31 || formatIndex == 57 || formatIndex == 58 || formatIndex == 20 || formatIndex == 32) {
			return true;
		}

		if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
			return true;
		}

		return false;
	}
	// -------------------------------------------------------------------------------------------------------------- Private method end
}
