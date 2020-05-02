package cn.hutool.poi.excel.sax;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import cn.hutool.poi.exceptions.POIException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Sax方式读取Excel文件<br>
 * Excel2007格式说明见：http://www.cnblogs.com/wangmingshun/p/6654143.html
 *
 * @author Looly
 * @since 3.1.2
 */
public class Excel07SaxReader extends AbstractExcelSaxReader<Excel07SaxReader> implements ContentHandler {

	/**
	 * Cell单元格元素
	 */
	private static final String C_ELEMENT = "c";
	/**
	 * 行元素
	 */
	private static final String ROW_ELEMENT = "row";
	/**
	 * Cell中的行列号(Reference)，行模式下此为行号属性名，列模式下为列号属性名
	 */
	private static final String R_ATTR = "r";
	/**
	 * SST（SharedStringsTable） 的索引，样式index
	 */
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
	private final StringBuilder lastContent = new StringBuilder(64);
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
	// 单元格存储的格式化字符串，nmtFmt的formatCode属性的值
	private String numFmtString;
	// sheet的索引
	private int sheetIndex;

	// 存储每行的列元素
	List<Object> rowCellList = new ArrayList<>();

	/**
	 * 行处理器
	 */
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
	public Excel07SaxReader read(File file, int rid) throws POIException {
		try {
			return read(OPCPackage.open(file), rid);
		} catch (Exception e) {
			throw new POIException(e);
		}
	}

	@Override
	public Excel07SaxReader read(InputStream in, int rid) throws POIException {
		try {
			return read(OPCPackage.open(in), rid);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new POIException(e);
		}
	}

	/**
	 * 开始读取Excel，Sheet编号从0开始计数
	 *
	 * @param opcPackage {@link OPCPackage}，Excel包
	 * @param rid        Excel中的sheet rid编号，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	public Excel07SaxReader read(OPCPackage opcPackage, int rid) throws POIException {
		InputStream sheetInputStream = null;
		try {
			final XSSFReader xssfReader = new XSSFReader(opcPackage);

			// 获取共享样式表
			try{
				stylesTable = xssfReader.getStylesTable();
			} catch (Exception e){
				//ignore
			}
			// 获取共享字符串表
			this.sharedStringsTable = xssfReader.getSharedStringsTable();

			if (rid > -1) {
				this.sheetIndex = rid;
				// 根据 rId# 或 rSheet# 查找sheet
				sheetInputStream = xssfReader.getSheet(RID_PREFIX + (rid + 1));
				ExcelSaxUtil.readFrom(sheetInputStream, this);
			} else {
				this.sheetIndex = -1;
				// 遍历所有sheet
				final Iterator<InputStream> sheetInputStreams = xssfReader.getSheetsData();
				while (sheetInputStreams.hasNext()) {
					// 重新读取一个sheet时行归零
					curRow = 0;
					this.sheetIndex++;
					sheetInputStream = sheetInputStreams.next();
					ExcelSaxUtil.readFrom(sheetInputStream, this);
				}
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new POIException(e);
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
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		// 单元格元素
		if (C_ELEMENT.equals(localName)) {
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

		lastContent.setLength(0);
	}

	/**
	 * 标签结束的回调处理方法
	 */
	@Override
	public void endElement(String uri, String localName, String qName) {
		final String contentStr = StrUtil.trim(lastContent);

//		if (T_ELEMENT.equals(qName)) {
//			// type标签
//			// rowCellList.add(curCell++, contentStr);
//		} else
		if (C_ELEMENT.equals(localName)) {
			// cell标签
			Object value = ExcelSaxUtil.getDataValue(this.cellDataType, contentStr, this.sharedStringsTable, this.numFmtString);
			// 补全单元格之间的空格
			fillBlankCell(preCoordinate, curCoordinate, false);
			rowCellList.add(curCell++, value);
		} else if (ROW_ELEMENT.equals(localName)) {
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
			// 新建一个新列，之前的列抛弃（可能被回收或rowHandler处理）
			rowCellList = new ArrayList<>(curCell + 1);
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
	public void characters(char[] ch, int start, int length) {
		// 得到单元格内容的值
		lastContent.append(ch, start, length);
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
	public void startDocument() {
		// pass
	}

	@Override
	public void endDocument() {
		// pass
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) {
		// pass
	}

	@Override
	public void endPrefixMapping(String prefix) {
		// pass
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) {
		// pass
	}

	@Override
	public void processingInstruction(String target, String data) {
		// pass
	}

	@Override
	public void skippedEntity(String name) {
		// pass
	}
	// --------------------------------------------------------------------------------------- Pass method end

	// --------------------------------------------------------------------------------------- Private method start
	/**
	 * 填充空白单元格，如果前一个单元格大于后一个，不需要填充<br>
	 *
	 * @param preCoordinate 前一个单元格坐标
	 * @param curCoordinate 当前单元格坐标
	 * @param isEnd         是否为最后一个单元格
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
	 * 设置单元格的类型
	 *
	 * @param attribute 属性
	 */
	private void setCellType(Attributes attribute) {
		// numFmtString的值
		numFmtString = "";
		this.cellDataType = CellDataType.of(attribute.getValue(T_ATTR_VALUE));

		// 获取单元格的xf索引，对应style.xml中cellXfs的子元素xf
		if(null != this.stylesTable){
			final String xfIndexStr = attribute.getValue(S_ATTR_VALUE);
			if (null != xfIndexStr) {
				int xfIndex = Integer.parseInt(xfIndexStr);
				final XSSFCellStyle xssfCellStyle = stylesTable.getStyleAt(xfIndex);
				numFmtString = xssfCellStyle.getDataFormatString();
				// 单元格存储格式的索引，对应style.xml中的numFmts元素的子元素索引
				int numFmtIndex = xssfCellStyle.getDataFormat();
				if (numFmtString == null) {
					numFmtString = BuiltinFormats.getBuiltinFormat(numFmtIndex);
				} else if (CellDataType.NUMBER == this.cellDataType && org.apache.poi.ss.usermodel.DateUtil.isADateFormat(numFmtIndex, numFmtString)) {
					cellDataType = CellDataType.DATE;
				}
			}
		}

	}
	// --------------------------------------------------------------------------------------- Private method end
}