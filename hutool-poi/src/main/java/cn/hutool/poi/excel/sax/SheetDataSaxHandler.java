package cn.hutool.poi.excel.sax;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.cell.FormulaCellValue;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * sheetData标签内容读取处理器
 *
 * <pre>
 * &lt;sheetData&gt;&lt;/sheetData&gt;
 * </pre>
 * @since 5.5.3
 */
public class SheetDataSaxHandler extends DefaultHandler {

	// 单元格的格式表，对应style.xml
	protected StylesTable stylesTable;
	// excel 2007 的共享字符串表,对应sharedString.xml
	protected SharedStringsTable sharedStringsTable;
	// sheet的索引，从0开始
	protected int sheetIndex;

	// 当前非空行
	protected int index;
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
	// 是否处于sheetData标签内，sax只解析此标签内的内容，其它标签忽略
	private boolean isInSheetData;

	// 上一次的内容
	private final StrBuilder lastContent = StrUtil.strBuilder();
	// 上一次的内容
	private final StrBuilder lastFormula = StrUtil.strBuilder();
	// 存储每行的列元素
	private List<Object> rowCellList = new ArrayList<>();

	public SheetDataSaxHandler(RowHandler rowHandler){
		this.rowHandler = rowHandler;
	}

	/**
	 * 行处理器
	 */
	protected RowHandler rowHandler;

	/**
	 * 设置行处理器
	 *
	 * @param rowHandler 行处理器
	 */
	public void setRowHandler(RowHandler rowHandler) {
		this.rowHandler = rowHandler;
	}

	/**
	 * 读到一个xml开始标签时的回调处理方法
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if ("sheetData".equals(qName)) {
			this.isInSheetData = true;
			return;
		}

		if (false == this.isInSheetData) {
			// 非sheetData标签，忽略解析
			return;
		}

		final ElementName name = ElementName.of(qName);
		this.curElementName = name;

		if (null != name) {
			switch (name) {
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
		if ("sheetData".equals(qName)) {
			// sheetData结束，不再解析别的标签
			this.isInSheetData = false;
			return;
		}

		if (false == this.isInSheetData) {
			// 非sheetData标签，忽略解析
			return;
		}

		this.curElementName = null;
		if (ElementName.c.match(qName)) { // 单元格结束
			endCell();
		} else if (ElementName.row.match(qName)) {// 行结束
			endRow();
		}
		// 其它标签忽略
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		if (false == this.isInSheetData) {
			// 非sheetData标签，忽略解析
			return;
		}

		final ElementName elementName = this.curElementName;
		if (null != elementName) {
			switch (elementName) {
				case v:
					// 得到单元格内容的值
					lastContent.append(ch, start, length);
					break;
				case f:
					// 得到单元格内容的值
					lastFormula.append(ch, start, length);
					break;
			}
		} else{
			// 按理说内容应该为"<v>内容</v>"，但是某些特别的XML内容不在v或f标签中，此处做一些兼容
			// issue#1303@Github
			lastContent.append(ch, start, length);
		}
	}

	// --------------------------------------------------------------------------------------- Private method start

	/**
	 * 行开始
	 *
	 * @param attributes 属性列表
	 */
	private void startRow(Attributes attributes) {
		final String rValue = AttributeName.r.getValue(attributes);
		if (null != rValue) {
			this.rowNumber = Long.parseLong(rValue) - 1;
		}
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
	 * 一个单元格结尾
	 */
	private void endCell() {
		// 补全单元格之间的空格
		fillBlankCell(preCoordinate, curCoordinate, false);

		final String contentStr = StrUtil.trim(lastContent);
		Object value = ExcelSaxUtil.getDataValue(this.cellDataType, contentStr, this.sharedStringsTable, this.numFmtString);
		if (false == this.lastFormula.isEmpty()) {
			value = new FormulaCellValue(StrUtil.trim(lastFormula), value);
		}
		addCellValue(curCell++, value);
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
				addCellValue(curCell++, StrUtil.EMPTY);
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
		numFmtString = StrUtil.EMPTY;
		this.cellDataType = CellDataType.of(AttributeName.t.getValue(attributes));

		// 获取单元格的xf索引，对应style.xml中cellXfs的子元素xf
		if (null != this.stylesTable) {
			final String xfIndexStr = AttributeName.s.getValue(attributes);
			if (null != xfIndexStr) {
				this.xssfCellStyle = stylesTable.getStyleAt(Integer.parseInt(xfIndexStr));
				// 单元格存储格式的索引，对应style.xml中的numFmts元素的子元素索引
				final int numFmtIndex = xssfCellStyle.getDataFormat();
				this.numFmtString = ObjectUtil.defaultIfNull(
						xssfCellStyle.getDataFormatString(),
						() -> BuiltinFormats.getBuiltinFormat(numFmtIndex));
				if (CellDataType.NUMBER == this.cellDataType && ExcelSaxUtil.isDateFormat(numFmtIndex, numFmtString)) {
					cellDataType = CellDataType.DATE;
				}
			}
		}

	}

	// --------------------------------------------------------------------------------------- Private method end
}
