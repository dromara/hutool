package cn.hutool.poi.excel.sax;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.cell.FormulaCellValue;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import cn.hutool.poi.exceptions.POIException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
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
public class Excel07SaxReader extends DefaultHandler implements ExcelSaxReader<Excel07SaxReader> {

	// sheet r:Id前缀
	private static final String RID_PREFIX = "rId";

	// 单元格的格式表，对应style.xml
	private StylesTable stylesTable;
	// excel 2007 的共享字符串表,对应sharedString.xml
	private SharedStringsTable sharedStringsTable;
	// sheet的索引
	private int sheetIndex;

	// 当前非空行
	private int index;
	// 当前列
	private int curCell;
	// 单元数据类型
	private CellDataType cellDataType;
	// 当前行号，从0开始
	private long rowNumber;
	// 当前列坐标， 如A1，B5
	private String curCoordinate;
	// 当前节点名称
	private ElementName curElementName;
	// 前一个列的坐标
	private String preCoordinate;
	// 行的最大列坐标
	private String maxCellCoordinate;
	// 单元格样式
	private XSSFCellStyle xssfCellStyle;
	// 单元格存储的格式化字符串，nmtFmt的formatCode属性的值
	private String numFmtString;

	// 上一次的内容
	private final StrBuilder lastContent = StrUtil.strBuilder();
	// 上一次的内容
	private final StrBuilder lastFormula = StrUtil.strBuilder();
	// 存储每行的列元素
	private List<Object> rowCellList = new ArrayList<>();

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
		return read(file, RID_PREFIX + rid);
	}

	@Override
	public Excel07SaxReader read(File file, String idOrRid) throws POIException {
		try {
			return read(OPCPackage.open(file), idOrRid);
		} catch (InvalidFormatException e) {
			throw new POIException(e);
		}
	}

	@Override
	public Excel07SaxReader read(InputStream in, int rid) throws POIException {
		return read(in, RID_PREFIX + rid);
	}

	@Override
	public Excel07SaxReader read(InputStream in, String idOrRid) throws POIException {
		try (final OPCPackage opcPackage = OPCPackage.open(in)) {
			return read(opcPackage, idOrRid);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} catch (InvalidFormatException e) {
			throw new POIException(e);
		}
	}

	/**
	 * 开始读取Excel，Sheet编号从0开始计数
	 *
	 * @param opcPackage {@link OPCPackage}，Excel包，读取后不关闭
	 * @param rid        Excel中的sheet rid编号，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	public Excel07SaxReader read(OPCPackage opcPackage, int rid) throws POIException {
		return read(opcPackage, RID_PREFIX + rid);
	}

	/**
	 * 开始读取Excel，Sheet编号从0开始计数
	 *
	 * @param opcPackage {@link OPCPackage}，Excel包，读取后不关闭
	 * @param idOrRid    Excel中的sheet id或者rid编号，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	public Excel07SaxReader read(OPCPackage opcPackage, String idOrRid) throws POIException {
		try {
			return read(new XSSFReader(opcPackage), idOrRid);
		} catch (OpenXML4JException e) {
			throw new POIException(e);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 开始读取Excel，Sheet编号从0开始计数
	 *
	 * @param xssfReader {@link XSSFReader}，Excel读取器
	 * @param idOrRid    Excel中的sheet id或者rid编号，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 * @since 5.4.4
	 */
	public Excel07SaxReader read(XSSFReader xssfReader, String idOrRid) throws POIException {
		// 获取共享样式表
		try {
			stylesTable = xssfReader.getStylesTable();
		} catch (Exception e) {
			//ignore
		}

		// 获取共享字符串表
		try {
			this.sharedStringsTable = xssfReader.getSharedStringsTable();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} catch (InvalidFormatException e) {
			throw new POIException(e);
		}

		return readSheets(xssfReader, idOrRid);
	}
	// ------------------------------------------------------------------------------ Read end

	/**
	 * 读到一个xml开始标签时的回调处理方法
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		final ElementName name = ElementName.of(localName);
		this.curElementName = name;

		if(null != name){
			switch (name){
				case row:
					// 行开始
					startRow(attributes);
					break;
				case c:
					// 单元格元素
					startCell(attributes);
					break;
			}
		}
	}

	/**
	 * 标签结束的回调处理方法
	 */
	@Override
	public void endElement(String uri, String localName, String qName) {
		this.curElementName = null;
		if (ElementName.c.match(localName)) { // 单元格结束
			endCell();
		} else if (ElementName.row.match(localName)) {// 行结束
			endRow();
		}
	}

	/**
	 * s标签结束的回调处理方法
	 */
	@Override
	public void characters(char[] ch, int start, int length) {
		switch (this.curElementName){
			case v:
				// 得到单元格内容的值
				lastContent.append(ch, start, length);
				break;
			case f:
				// 得到单元格内容的值
				lastFormula.append(ch, start, length);
				break;
		}
	}

	// --------------------------------------------------------------------------------------- Private method start

	/**
	 * 开始读取Excel，Sheet编号从0开始计数
	 *
	 * @param xssfReader {@link XSSFReader}，Excel读取器
	 * @param idOrRid    Excel中的sheet id或者rid编号，rid必须加rId前缀，例如rId0，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 * @since 5.4.4
	 */
	private Excel07SaxReader readSheets(XSSFReader xssfReader, String idOrRid) throws POIException {
		// 将sheetId转换为rid
		if (NumberUtil.isInteger(idOrRid)) {
			final SheetRidReader ridReader = new SheetRidReader();
			final String rid = ridReader.read(xssfReader).getRidBySheetId(idOrRid);
			if (StrUtil.isNotEmpty(rid)) {
				idOrRid = rid;
			}
		}
		this.sheetIndex = Integer.parseInt(StrUtil.removePrefixIgnoreCase(idOrRid, RID_PREFIX));
		InputStream sheetInputStream = null;
		try {
			if (this.sheetIndex > -1) {
				// 根据 rId# 或 rSheet# 查找sheet
				sheetInputStream = xssfReader.getSheet(RID_PREFIX + (this.sheetIndex + 1));
				ExcelSaxUtil.readFrom(sheetInputStream, this);
				rowHandler.doAfterAllAnalysed();
			} else {
				this.sheetIndex = -1;
				// 遍历所有sheet
				final Iterator<InputStream> sheetInputStreams = xssfReader.getSheetsData();
				while (sheetInputStreams.hasNext()) {
					// 重新读取一个sheet时行归零
					index = 0;
					this.sheetIndex++;
					sheetInputStream = sheetInputStreams.next();
					ExcelSaxUtil.readFrom(sheetInputStream, this);
					rowHandler.doAfterAllAnalysed();
				}
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new POIException(e);
		} finally {
			IoUtil.close(sheetInputStream);
		}
		return this;
	}

	/**
	 * 行开始
	 *
	 * @param attributes 属性列表
	 */
	private void startRow(Attributes attributes) {
		this.rowNumber = Long.parseLong(AttributeName.r.getValue(attributes)) - 1;
	}

	/**
	 * 单元格开始
	 *
	 * @param attributes 属性列表
	 */
	private void startCell(Attributes attributes) {
		// 获取当前列坐标
		final String tempCurCoordinate = AttributeName.r.getValue(attributes);
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

		// 清空之前的数据
		lastContent.reset();
		lastFormula.reset();
	}

	/**
	 * 一个单元格结尾
	 */
	private void endCell() {
		// 补全单元格之间的空格
		fillBlankCell(preCoordinate, curCoordinate, false);

		final String contentStr = StrUtil.trim(lastContent);
		Object value = ExcelSaxUtil.getDataValue(this.cellDataType, contentStr, this.sharedStringsTable, this.numFmtString);
		if(false == this.lastFormula.isEmpty()){
			value = new FormulaCellValue(StrUtil.trim(lastFormula), value);
		}
		addCellValue(curCell++, value);
	}

	/**
	 * 一行结尾
	 */
	private void endRow() {
		// 最大列坐标以第一个非空行的为准
		if (index == 0) {
			maxCellCoordinate = curCoordinate;
		}

		// 补全一行尾部可能缺失的单元格
		if (maxCellCoordinate != null) {
			fillBlankCell(curCoordinate, maxCellCoordinate, true);
		}

		rowHandler.handle(sheetIndex, rowNumber, rowCellList);

		// 一行结束
		// 新建一个新列，之前的列抛弃（可能被回收或rowHandler处理）
		rowCellList = new ArrayList<>(curCell + 1);
		// 行数增加
		index++;
		// 当前列置0
		curCell = 0;
		// 置空当前列坐标和前一列坐标
		curCoordinate = null;
		preCoordinate = null;
	}

	/**
	 * 在一行中的指定列增加值
	 *
	 * @param index 位置
	 * @param value 值
	 */
	private void addCellValue(int index, Object value) {
		this.rowCellList.add(index, value);
		this.rowHandler.handleCell(this.sheetIndex, this.rowNumber, index, value, this.xssfCellStyle);
	}

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
				addCellValue(curCell++, "");
			}
		}
	}

	/**
	 * 设置单元格的类型
	 *
	 * @param attributes 属性
	 */
	private void setCellType(Attributes attributes) {
		// numFmtString的值
		numFmtString = "";
		this.cellDataType = CellDataType.of(AttributeName.t.getValue(attributes));

		// 获取单元格的xf索引，对应style.xml中cellXfs的子元素xf
		if (null != this.stylesTable) {
			final String xfIndexStr = AttributeName.s.getValue(attributes);
			if (null != xfIndexStr) {
				int xfIndex = Integer.parseInt(xfIndexStr);
				this.xssfCellStyle = stylesTable.getStyleAt(xfIndex);
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