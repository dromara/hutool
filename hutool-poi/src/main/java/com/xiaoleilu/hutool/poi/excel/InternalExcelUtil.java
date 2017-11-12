package com.xiaoleilu.hutool.poi.excel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.poi.excel.editors.TrimEditor;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Excel内部工具类，主要针对行等操作支持
 * 
 * @author looly
 */
public class InternalExcelUtil {
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
		default:
			value = cell.getStringCellValue();
		}

		return null == cellEditor ? value : cellEditor.edit(cell, value);
	}

	/**
	 * 设置单元格值
	 * 
	 * @param cell 单元格
	 * @param value 值
	 * @param cellStyle 单元格样式
	 */
	public static void setCellValue(Cell cell, Object value, CellStyle cellStyle) {
		if (value instanceof Date) {
			short format = cellStyle.getDataFormat();
			if(0 == format) {
				final CellStyle styleForDate = cloneCellStyle(cell, cellStyle);
				//22表示：m/d/yy h:mm
				styleForDate.setDataFormat((short)22);
				cell.setCellStyle(styleForDate);
			}
			cell.setCellValue((Date) value);
		} else if (value instanceof Calendar) {
			cell.setCellValue((Calendar) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof RichTextString) {
			cell.setCellValue((RichTextString) value);
		} else if (value instanceof Number) {
			if(value instanceof Double || value instanceof Float) {
				//Double默认保留两位小数
				final short format = cellStyle.getDataFormat();
				if(0 == format) {
					final CellStyle styleForDouble = cloneCellStyle(cell, cellStyle);
					//2表示：0.00
					styleForDouble.setDataFormat((short)2);
					cell.setCellStyle(styleForDouble);
				}
			}
			cell.setCellValue(((Number) value).doubleValue());
		} else {
			cell.setCellValue(value.toString());
		}
	}
	
	/**
	 * 克隆新的{@link CellStyle}
	 * @param cell 单元格
	 * @param cellStyle 被复制的样式
	 * @return {@link CellStyle}
	 */
	public static CellStyle cloneCellStyle(Cell cell, CellStyle cellStyle) {
		final CellStyle newCellStyle = cell.getSheet().getWorkbook().createCellStyle();
		newCellStyle.cloneStyleFrom(cellStyle);
		return newCellStyle;
	}
	
	/**
	 * 读取一行
	 * 
	 * @param row 行
	 * @param cellEditor 单元格编辑器
	 * @return 单元格值列表
	 */
	public static List<Object> readRow(Row row, CellEditor cellEditor) {
		final List<Object> cellValues = new ArrayList<>();
		
		short length = row.getLastCellNum();
		for (short i = 0; i < length; i++) {
			cellValues.add(InternalExcelUtil.getCellValue(row.getCell(i), cellEditor));
		}
		return cellValues;
	}

	/**
	 * 写一行数据
	 * 
	 * @param row 行
	 * @param rowData 一行的数据
	 * @param cellStyle 单元格样式
	 */
	public static void writeRow(Row row, Iterable<?> rowData, CellStyle cellStyle) {
		int i = 0;
		Cell cell;
		for (Object value : rowData) {
			cell = row.createCell(i);
			if (null != cellStyle) {
				cell.setCellStyle(cellStyle);
			}
			setCellValue(cell, value, cellStyle);
			i++;
		}
	}

	/**
	 * 获取工作簿指定sheet中图片列表
	 * 
	 * @param workbook 工作簿{@link Workbook}
	 * @param sheetIndex sheet的索引
	 * @return 图片映射，键格式：行_列，值：{@link PictureData}
	 */
	public static Map<String, PictureData> getPicMap(Workbook workbook, int sheetIndex) {
		Assert.notNull(workbook, "Workbook must be not null !");
		if (sheetIndex < 0) {
			sheetIndex = 0;
		}

		if (workbook instanceof HSSFWorkbook) {
			return getPicMapXls((HSSFWorkbook) workbook, sheetIndex);
		} else if (workbook instanceof XSSFWorkbook) {
			return getPicMapXlsx((XSSFWorkbook) workbook, sheetIndex);
		} else {
			throw new IllegalArgumentException(StrUtil.format("Workbook type [{}] is not supported!", workbook.getClass()));
		}
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
	 * @param cellStyle 单元格样式
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

	/**
	 * cell文本位置样式
	 * 
	 * @param cellStyle {@link CellStyle}
	 * @param halign 横向位置
	 * @param valign 纵向位置
	 * @return {@link CellStyle}
	 */
	public static CellStyle setAlign(CellStyle cellStyle, HorizontalAlignment halign, VerticalAlignment valign) {
		cellStyle.setAlignment(halign);
		cellStyle.setVerticalAlignment(valign);
		return cellStyle;
	}

	/**
	 * 设置cell的四个边框粗细和颜色
	 * 
	 * @param cellStyle {@link CellStyle}
	 * @param borderSize 边框粗细{@link BorderStyle}枚举
	 * @param colorIndex 颜色的short值
	 * @return {@link CellStyle}
	 */
	public static CellStyle setBorder(CellStyle cellStyle, BorderStyle borderSize, IndexedColors colorIndex) {
		cellStyle.setBorderBottom(borderSize);
		cellStyle.setBottomBorderColor(colorIndex.index);

		cellStyle.setBorderLeft(borderSize);
		cellStyle.setLeftBorderColor(colorIndex.index);

		cellStyle.setBorderRight(borderSize);
		cellStyle.setRightBorderColor(colorIndex.index);

		cellStyle.setBorderTop(borderSize);
		cellStyle.setTopBorderColor(colorIndex.index);

		return cellStyle;
	}

	/**
	 * 给cell设置颜色
	 * 
	 * @param cellStyle {@link CellStyle}
	 * @param backgroundColor 背景颜色
	 * @param fillPattern 填充方式 {@link FillPatternType}枚举
	 * @return {@link CellStyle}
	 */
	public static CellStyle setColor(CellStyle cellStyle, IndexedColors backgroundColor, FillPatternType fillPattern) {
		cellStyle.setFillForegroundColor(backgroundColor.index);
		cellStyle.setFillPattern(fillPattern);
		return cellStyle;
	}

	/**
	 * 创建默认普通单元格样式
	 * 
	 * <pre>
	 * 1. 文字上下左右居中
	 * 2. 细边框，黑色
	 * </pre>
	 * 
	 * @param workbook {@link Workbook} 工作簿
	 * @return {@link CellStyle}
	 */
	public static CellStyle createDefaultCellStyle(Workbook workbook) {
		final CellStyle cellStyle = workbook.createCellStyle();
		setAlign(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
		setBorder(cellStyle, BorderStyle.THIN, IndexedColors.BLACK);
		return cellStyle;
	}

	/**
	 * 创建默认头部样式
	 * 
	 * @param workbook {@link Workbook} 工作簿
	 * @return {@link CellStyle}
	 */
	public static CellStyle createHeadCellStyle(Workbook workbook) {
		final CellStyle cellStyle = workbook.createCellStyle();
		setAlign(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
		setBorder(cellStyle, BorderStyle.THIN, IndexedColors.BLACK);
		setColor(cellStyle, IndexedColors.GREY_25_PERCENT, FillPatternType.SOLID_FOREGROUND);
		return cellStyle;
	}

	// -------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 获取XLS工作簿指定sheet中图片列表
	 * 
	 * @param workbook 工作簿{@link Workbook}
	 * @param sheetIndex sheet的索引
	 * @return 图片映射，键格式：行_列，值：{@link PictureData}
	 */
	private static Map<String, PictureData> getPicMapXls(HSSFWorkbook workbook, int sheetIndex) {
		final Map<String, PictureData> picMap = new HashMap<>();
		final List<HSSFPictureData> pictures = workbook.getAllPictures();
		if (CollectionUtil.isNotEmpty(pictures)) {
			final HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
			HSSFClientAnchor anchor;
			int pictureIndex;
			for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
				if (shape instanceof HSSFPicture) {
					pictureIndex = ((HSSFPicture) shape).getPictureIndex() - 1;
					anchor = (HSSFClientAnchor) shape.getAnchor();
					picMap.put(StrUtil.format("{}_{}", anchor.getRow1(), anchor.getCol1()), pictures.get(pictureIndex));
				}
			}
		}
		return picMap;
	}

	/**
	 * 获取XLSX工作簿指定sheet中图片列表
	 * 
	 * @param workbook 工作簿{@link Workbook}
	 * @param sheetIndex sheet的索引
	 * @return 图片映射，键格式：行_列，值：{@link PictureData}
	 */
	private static Map<String, PictureData> getPicMapXlsx(XSSFWorkbook workbook, int sheetIndex) {
		final Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
		final XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		XSSFDrawing drawing;
		for (POIXMLDocumentPart dr : sheet.getRelations()) {
			if (dr instanceof XSSFDrawing) {
				drawing = (XSSFDrawing) dr;
				final List<XSSFShape> shapes = drawing.getShapes();
				XSSFPicture pic;
				CTMarker ctMarker;
				for (XSSFShape shape : shapes) {
					pic = (XSSFPicture) shape;
					ctMarker = pic.getPreferredSize().getFrom();
					sheetIndexPicMap.put(StrUtil.format("{}_{}", ctMarker.getRow(), ctMarker.getCol()), pic.getPictureData());
				}
			}
		}
		return sheetIndexPicMap;
	}

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
		final String format = style.getDataFormatString();

		// 判断是否为日期
		if (isDateType(formatIndex, format)) {
			return DateUtil.date(cell.getDateCellValue());// 使用Hutool的DateTime包装
		}

		// 普通数字
		if (null != format && format.indexOf('.') < 0) {
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
	private static boolean isDateType(int formatIndex, String format) {
		// yyyy-MM-dd----- 14
		// yyyy年m月d日---- 31
		// yyyy年m月------- 57
		// m月d日 ---------- 58
		// HH:mm----------- 20
		// h时mm分 -------- 32
		if (formatIndex == 14 || formatIndex == 31 || formatIndex == 57 || formatIndex == 58 || formatIndex == 20 || formatIndex == 32) {
			return true;
		}

		if (org.apache.poi.ss.usermodel.DateUtil.isADateFormat(formatIndex, format)) {
			return true;
		}

		return false;
	}
	// -------------------------------------------------------------------------------------------------------------- Private method end
}
