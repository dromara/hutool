package com.xiaoleilu.hutool.poi.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
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
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.poi.exceptions.POIException;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Excel工具类
 * 
 * @author Looly
 *
 */
public class ExcelUtil {

	/**
	 * 加载工作簿
	 * 
	 * @param in Excel输入流
	 * @return {@link Workbook}
	 */
	public static Workbook loadBook(InputStream in) {
		try {
			return WorkbookFactory.create(in);
		} catch (Exception e) {
			throw new POIException(e);
		}
	}

	/**
	 * 是否为XLS格式的Excel文件（HSSF）<br>
	 * XLS文件主要用于Excel 97~2003创建
	 * 
	 * @param in excel输入流
	 * @return 是否为XLS格式的Excel文件（HSSF）
	 */
	public static boolean isXls(InputStream in) {
		final PushbackInputStream pin = IoUtil.toPushbackStream(in, 8);
		try {
			return POIFSFileSystem.hasPOIFSHeader(pin);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 是否为XLSX格式的Excel文件（XSSF）<br>
	 * XLSX文件主要用于Excel 2007+创建
	 * 
	 * @param in excel输入流
	 * @return 是否为XLSX格式的Excel文件（XSSF）
	 */
	public static boolean isXlsx(InputStream in) {
		final PushbackInputStream pin = IoUtil.toPushbackStream(in, 8);
		try {
			return DocumentFactoryHelper.hasOOXMLHeader(pin);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	
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
	 * @param cellType 单元格值类型{@link CellType}枚举
	 * @param isTrimCellValue 如果单元格类型为字符串，是否去掉两边空白符
	 * @return 值，类型可能为：Date、Double、Boolean、String
	 */
	public static Object getCellValue(Cell cell, CellType cellType, boolean isTrimCellValue) {
		switch (cellType) {
			case NUMERIC:
				if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				}
				return cell.getNumericCellValue();
			case BOOLEAN:
				return cell.getBooleanCellValue();
			case FORMULA:
				//遇到公式时查找公式结果类型
				return getCellValue(cell, cell.getCachedFormulaResultTypeEnum(), isTrimCellValue);
			case BLANK:
				return StrUtil.EMPTY;
			default:
				return isTrimCellValue ? StrUtil.trim(cell.getStringCellValue()) : cell.getStringCellValue();
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
}
