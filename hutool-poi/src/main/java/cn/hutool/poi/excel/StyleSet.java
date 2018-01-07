package cn.hutool.poi.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 样式集合，此样式集合汇集了整个工作簿的样式，用于减少样式的创建和冗余
 * 
 * @author looly
 *
 */
public class StyleSet {

	/** 标题样式 */
	protected CellStyle headCellStyle;
	/** 默认样式 */
	protected CellStyle cellStyle;
	/** 默认数字样式 */
	protected CellStyle cellStyleForNumber;
	/** 默认日期样式 */
	protected CellStyle cellStyleForDate;

	/**
	 * 构造
	 * 
	 * @param workbook 工作簿
	 */
	public StyleSet(Workbook workbook) {
		this.headCellStyle = InternalExcelUtil.createHeadCellStyle(workbook);
		this.cellStyle = InternalExcelUtil.createDefaultCellStyle(workbook);

		// 默认日期格式
		this.cellStyleForDate = InternalExcelUtil.cloneCellStyle(workbook, this.cellStyle);
		// 22表示：m/d/yy h:mm
		this.cellStyleForDate.setDataFormat((short) 22);

		// 默认数字格式
		cellStyleForNumber = InternalExcelUtil.cloneCellStyle(workbook, this.cellStyle);
		// 2表示：0.00
		cellStyleForNumber.setDataFormat((short) 2);
	}

	/**
	 * 获取头部样式，获取后可以定义整体头部样式
	 * 
	 * @return 头部样式
	 */
	public CellStyle getHeadCellStyle() {
		return headCellStyle;
	}

	/**
	 * 获取常规单元格样式，获取后可以定义整体头部样式
	 * 
	 * @return 常规单元格样式
	 */
	public CellStyle getCellStyle() {
		return cellStyle;
	}

	/**
	 * 获取数字（带小数点）单元格样式，获取后可以定义整体头部样式
	 * 
	 * @return 数字（带小数点）单元格样式
	 */
	public CellStyle getCellStyleForNumber() {
		return cellStyleForNumber;
	}

	/**
	 * 获取日期单元格样式，获取后可以定义整体头部样式
	 * 
	 * @return 日期单元格样式
	 */
	public CellStyle getCellStyleForDate() {
		return cellStyleForDate;
	}
}
