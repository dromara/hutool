package cn.hutool.poi.excel.sax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import cn.hutool.core.exceptions.DependencyException;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import cn.hutool.poi.exceptions.POIException;

/**
 * Sax方式读取Excel文件<br>
 * Excel2007格式说明见：http://www.cnblogs.com/wangmingshun/p/6654143.html
 * 
 * @author Looly
 * @since 3.1.2
 *
 */
public class Excel07SaxReader extends AbstractExcelSaxReader<Excel07SaxReader> implements ContentHandler {

	// saxParser
	private static final String CLASS_SAXPARSER = "org.apache.xerces.parsers.SAXParser";
	/** Cell单元格元素 */
	private static final String C_ELEMENT = "c";
	/** 行元素 */
	private static final String ROW_ELEMENT = "row";
	/** Cell中的行列号 */
	private static final String R_ATTR = "r";
	/** Cell类型 */
	private static final String T_ELEMENT = "t";
	/** SST（SharedStringsTable） 的索引 */
	private static final String S_ATTR_VALUE = "s";
	// 列中属性值
	private static final String T_ATTR_VALUE = "t";
	// sheet r:Id前缀
	private static final String RID_PREFIX = "rId";

	// excel 2007 的共享字符串表,对应sharedString.xml
	private SharedStringsTable sharedStringsTable;
	// 当前行
	private int curRow;
	// 当前列
	private int curCell;
	// 上一次的内容
	private String lastContent;
	// 单元数据类型
	private CellDataType cellDataType;
	// 当前列坐标， 如A1，B5
	private String curCoordinate;
	// 前一个列的坐标
	private String preCoordinate;
	// 行的最大列坐标
	private String maxCellCoordinate;
	// 单元格的格式表，对应style.xml
	private StylesTable stylesTable;
	// 单元格存储格式的索引，对应style.xml中的numFmts元素的子元素索引
	private int numFmtIndex;
	// 单元格存储的格式化字符串，nmtFmt的formateCode属性的值
	private String numFmtString;
	// sheet的索引
	private int sheetIndex;

	// 存储每行的列元素
	List<Object> rowCellList = new ArrayList<>();

	/** 行处理器 */
	private RowHandler rowHandler;

	/**
	 * 构造
	 * 
	 * @param rowHandler 行处理器
	 */
	public Excel07SaxReader(RowHandler rowHandler) {
		this.rowHandler = rowHandler;
	}

	/**
	 * 设置行处理器
	 * 
	 * @param rowHandler 行处理器
	 * @return this
	 */
	public Excel07SaxReader setRowHandler(RowHandler rowHandler) {
		this.rowHandler = rowHandler;
		return this;
	}

	// ------------------------------------------------------------------------------ Read start
	@Override
	public Excel07SaxReader read(File file, int sheetIndex) throws POIException {
		try {
			return read(OPCPackage.open(file), sheetIndex);
		} catch (Exception e) {
			throw new POIException(e);
		}
	}

	@Override
	public Excel07SaxReader read(InputStream in, int sheetIndex) throws POIException {
		try {
			return read(OPCPackage.open(in), sheetIndex);
		} catch (DependencyException e) {
			throw e;
		} catch (Exception e) {
			throw ExceptionUtil.wrap(e, POIException.class);
		}
	}

	/**
	 * 开始读取Excel，Sheet编号从0开始计数
	 * 
	 * @param opcPackage {@link OPCPackage}，Excel包
	 * @param sheetIndex Excel中的sheet编号，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	public Excel07SaxReader read(OPCPackage opcPackage, int sheetIndex) throws POIException {
		InputStream sheetInputStream = null;
		try {
			final XSSFReader xssfReader = new XSSFReader(opcPackage);

			// 获取共享样式表
			stylesTable = xssfReader.getStylesTable();
			// 获取共享字符串表
			this.sharedStringsTable = xssfReader.getSharedStringsTable();

			if (sheetIndex > -1) {
				this.sheetIndex = sheetIndex;
				// 根据 rId# 或 rSheet# 查找sheet
				sheetInputStream = xssfReader.getSheet(RID_PREFIX + (sheetIndex + 1));
				parse(sheetInputStream);
			} else {
				this.sheetIndex = -1;
				// 遍历所有sheet
				final Iterator<InputStream> sheetInputStreams = xssfReader.getSheetsData();
				while (sheetInputStreams.hasNext()) {
					// 重新读取一个sheet时行归零
					curRow = 0;
					this.sheetIndex++;
					sheetInputStream = sheetInputStreams.next();
					parse(sheetInputStream);
				}
			}
		} catch (DependencyException e) {
			throw e;
		} catch (Exception e) {
			throw ExceptionUtil.wrap(e, POIException.class);
		} finally {
			IoUtil.close(sheetInputStream);
			IoUtil.close(opcPackage);
		}
		return this;
	}
	// ------------------------------------------------------------------------------ Read end

	/**
	 * 读到一个xml开始标签时的回调处理方法
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// 单元格元素
		if (C_ELEMENT.equals(qName)) {

			// 获取当前列坐标
			String tempCurCoordinate = attributes.getValue(R_ATTR);
			// 前一列为null，则将其设置为"@",A为第一列，ascii码为65，前一列即为@，ascii码64
			if (preCoordinate == null) {
				preCoordinate = String.valueOf(ExcelSaxUtil.CELL_FILL_CHAR);
			} else {
				// 存在，则前一列要设置为上一列的坐标
				preCoordinate = curCoordinate;
			}
			// 重置当前列
			curCoordinate = tempCurCoordinate;
			// 设置单元格类型
			setCellType(attributes);
		}

		lastContent = "";
	}

	/**
	 * 设置单元格的类型
	 *
	 * @param attribute
	 */
	private void setCellType(Attributes attribute) {
		// 重置numFmtIndex,numFmtString的值
		numFmtIndex = 0;
		numFmtString = "";
		this.cellDataType = CellDataType.of(attribute.getValue(T_ATTR_VALUE));

		// 获取单元格的xf索引，对应style.xml中cellXfs的子元素xf
		final String xfIndexStr = attribute.getValue(S_ATTR_VALUE);
		if (xfIndexStr != null) {
			int xfIndex = Integer.parseInt(xfIndexStr);
			XSSFCellStyle xssfCellStyle = stylesTable.getStyleAt(xfIndex);
			numFmtIndex = xssfCellStyle.getDataFormat();
			numFmtString = xssfCellStyle.getDataFormatString();

			if (numFmtString == null) {
				cellDataType = CellDataType.NULL;
				numFmtString = BuiltinFormats.getBuiltinFormat(numFmtIndex);
			} else if (org.apache.poi.ss.usermodel.DateUtil.isADateFormat(numFmtIndex, numFmtString)) {
				cellDataType = CellDataType.DATE;
			}
		}

	}

	/**
	 * 标签结束的回调处理方法
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		final String contentStr = StrUtil.trim(lastContent);

		if (T_ELEMENT.equals(qName)) {
			// type标签
			// rowCellList.add(curCell++, contentStr);
		} else if (C_ELEMENT.equals(qName)) {
			// cell标签
			Object value = ExcelSaxUtil.getDataValue(this.cellDataType, contentStr, this.sharedStringsTable, this.numFmtString);
			// 补全单元格之间的空格
			fillBlankCell(preCoordinate, curCoordinate, false);
			rowCellList.add(curCell++, value);
		} else if (ROW_ELEMENT.equals(qName)) {
			// 如果是row标签，说明已经到了一行的结尾
			// 最大列坐标以第一行的为准
			if (curRow == 0) {
				maxCellCoordinate = curCoordinate;
			}

			// 补全一行尾部可能缺失的单元格
			if (maxCellCoordinate != null) {
				fillBlankCell(curCoordinate, maxCellCoordinate, true);
			}

			rowHandler.handle(sheetIndex, curRow, rowCellList);

			// 一行结束
			// 清空rowCellList,
			rowCellList.clear();
			// 行数增加
			curRow++;
			// 当前列置0
			curCell = 0;
			// 置空当前列坐标和前一列坐标
			curCoordinate = null;
			preCoordinate = null;
		}
	}

	/**
	 * s标签结束的回调处理方法
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// 得到单元格内容的值
		lastContent = lastContent.concat(new String(ch, start, length));
	}

	// --------------------------------------------------------------------------------------- Pass method start
	@Override
	public void setDocumentLocator(Locator locator) {
		// pass
	}

	/**
	 * ?xml标签的回调处理方法
	 */
	@Override
	public void startDocument() throws SAXException {
		// pass
	}

	@Override
	public void endDocument() throws SAXException {
		// pass
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// pass
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// pass
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		// pass
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		// pass
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// pass
	}
	// --------------------------------------------------------------------------------------- Pass method end

	// --------------------------------------------------------------------------------------- Private method start
	/**
	 * 处理流中的Excel数据
	 * 
	 * @param sheetInputStream sheet流
	 * @throws IOException IO异常
	 * @throws SAXException SAX异常
	 */
	private void parse(InputStream sheetInputStream) throws IOException, SAXException {
		fetchSheetReader().parse(new InputSource(sheetInputStream));
	}

	/**
	 * 填充空白单元格，如果前一个单元格大于后一个，不需要填充<br>
	 *
	 * @param preCoordinate 前一个单元格坐标
	 * @param curCoordinate 当前单元格坐标
	 * @param isEnd 是否为最后一个单元格
	 */
	private void fillBlankCell(String preCoordinate, String curCoordinate, boolean isEnd) {
		if (false == curCoordinate.equals(preCoordinate)) {
			int len = ExcelSaxUtil.countNullCell(preCoordinate, curCoordinate);
			if (isEnd) {
				len++;
			}
			while (len-- > 0) {
				rowCellList.add(curCell++, "");
			}
		}
	}

	/**
	 * 获取sheet的解析器
	 *
	 * @param sharedStringsTable
	 * @return {@link XMLReader}
	 * @throws SAXException SAX异常
	 */
	private XMLReader fetchSheetReader() throws SAXException {
		XMLReader xmlReader = null;
		try {
			xmlReader = XMLReaderFactory.createXMLReader(CLASS_SAXPARSER);
		} catch (SAXException e) {
			if (e.getMessage().contains("org.apache.xerces.parsers.SAXParser")) {
				throw new DependencyException(e, "You need to add 'xerces:xercesImpl' to your project and version >= 2.11.0");
			} else {
				throw e;
			}
		}
		xmlReader.setContentHandler(this);
		return xmlReader;
	}
	// --------------------------------------------------------------------------------------- Private method end
}