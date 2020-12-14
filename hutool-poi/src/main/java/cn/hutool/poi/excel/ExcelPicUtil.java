package cn.hutool.poi.excel;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel图片工具类
 * 
 * @author looly
 * @since 4.0.7
 */
public class ExcelPicUtil {
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
		final Map<String, PictureData> sheetIndexPicMap = new HashMap<>();
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
	// -------------------------------------------------------------------------------------------------------------- Private method end
}
