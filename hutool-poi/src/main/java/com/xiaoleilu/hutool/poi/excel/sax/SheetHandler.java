package com.xiaoleilu.hutool.poi.excel.sax;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.xiaoleilu.hutool.date.DatePattern;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 一个Sheet处理器
 * 
 * @author looly
 * @since 3.1.2
 */
public class SheetHandler extends DefaultHandler {

	/** 第0个单元格 */
	private static final char CELL_ZERO = '@';
	/** 单元格节点 */
	private static final String ELEMENT_TYPE_CELL = "c";
	/** 值节点 */
	private static final String ELEMENT_TYPE_VALUE = "v";
	/** 类型元素节点 */
	private static final String ELEMENT_TYPE_TYPE = "t";
	/** 行元素节点 */
	private static final String ELEMENT_TYPE_ROW = "row";

	/** 属性中的单元格坐标名 */
	private static final String ATTRIBUTE_COORDINATE = "r";
	/** 属性中的样式名 */
	private static final String ATTRIBUTE_STYLE = "s";
	/** 属性中的类型名 */
	private static final String ATTRIBUTE_TYPE = "t";

	/** 共享字符串表 */
	private SharedStringsTable table;
	/** 共享样式表 */
	private StylesTable stylesTable;

	private boolean isEnd;
	private String lastContent;
	/** 行指针，指示处理到的行号 */
	private int rowIndex;
	/** 列指针，指示处理到的列号 */
	private int colIndex;

	/** 前一个元素位置，用来计算其中空的单元格数量，如A6和A8等 */
	private String preRef = null;
	/** 当前元素位置，用来计算其中空的单元格数量，如A6和A8等 */
	private String ref = null;
	/** 定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格 */
	private String maxRef = null;
	/** 元素是否为String */
	private boolean nextIsString;
	/** 单元格数据类型 */
	private CellDataType cellDataType;
	/** 单元格样式 */
	private XSSFCellStyle cellStyle;
	/** 一行的数据列表 */
	private List<String> row = new ArrayList<String>();

	public SheetHandler(SharedStringsTable table, StylesTable stylesTable) {
		this.table = table;
		this.stylesTable = stylesTable;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		isEnd = false;
		// 跳过非单元格的节点
		if (false == ELEMENT_TYPE_CELL.equals(qName)) {
			return;
		}

		//读取新节点位置，并保存上一个节点
		final String tmpRef = attributes.getValue(ATTRIBUTE_COORDINATE);
		if (null == this.preRef) {
			// 设为第0列
			this.preRef = String.valueOf(CELL_ZERO);
		} else {
			this.preRef = this.ref;
		}
		this.ref = tmpRef;
		
		//当前单元格数据类型
		this.cellDataType = getCellDataType(attributes);
		this.cellStyle = getCellStyle(attributes);
		
		//清空上一个单元格内容
		this.lastContent = StrUtil.EMPTY;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		//读取节点数据，同一节点可能会被调用多次，将多次读取内容串起来
		lastContent.concat(new String(ch, start, length));
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		//节点结束时调用的方法，此时characters可能被调用多次
		final String value = StrUtil.trim(this.lastContent);
		if(ELEMENT_TYPE_TYPE.equals(qName)) {
			row.add(this.colIndex, value);
		}else if(ELEMENT_TYPE_CELL.equals(qName)) {
			ExcelSaxUtil.getDataValue(this.cellDataType, value, this.cellStyle);
		}
		
		if(false == this.isEnd && ELEMENT_TYPE_CELL.equals(qName) && CellDataType.NULL == this.cellDataType) {
			//空单元格
			row.add(colIndex, StrUtil.EMPTY);
			colIndex++;
		}else if(CellDataType.SSTINDEX == this.cellDataType) {
			int idx = Integer.parseInt(lastContent);
			this.lastContent = new XSSFRichTextString(this.table.getEntryAt(idx)).toString();
		}
		
		this.isEnd = true;
		
		
	}

	//----------------------------------------------------------------------------------------------- Private method start
	/**
	 * 设置单元格类型
	 * 
	 * @param attributes 属性
	 */
	private CellDataType getCellDataType(Attributes attributes) {
		CellDataType type = CellDataType.of(attributes.getValue(ATTRIBUTE_TYPE));

		// 数字类型中的日期类型判断
		XSSFCellStyle cellStyle = getCellStyle(attributes);
		if (null != cellStyle) {
			String formatStr = cellStyle.getDataFormatString();
			if (CellDataType.DATE.name().equals(formatStr)) {
				type = CellDataType.DATE;
				formatStr = DatePattern.NORM_DATETIME_PATTERN;
			}
			if (null == formatStr) {
				type = CellDataType.NULL;
			}
		}
		return type;
	}

	/**
	 * 获取单元格样式，无样式返回null
	 * 
	 * @param attributes 单元格属性
	 * @return 单元格样式，无样式null
	 */
	private XSSFCellStyle getCellStyle(Attributes attributes) {
		final String styleIndexStr = attributes.getValue(ATTRIBUTE_STYLE);
		if (null == styleIndexStr) {
			return null;
		}
		return this.stylesTable.getStyleAt(Integer.parseInt(styleIndexStr));
	}
	//----------------------------------------------------------------------------------------------- Private method end
}
