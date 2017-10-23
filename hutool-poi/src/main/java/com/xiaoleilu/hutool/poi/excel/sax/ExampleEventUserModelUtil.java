package com.xiaoleilu.hutool.poi.excel.sax;

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
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.xiaoleilu.hutool.lang.Console;

public class ExampleEventUserModelUtil {

	private static XSSFCellStyle style;

	/**
	 * 处理一个sheet
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void processOneSheet(String filename) throws Exception {

		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst, r.getStylesTable());
		Iterator<InputStream> sheets = r.getSheetsData();
		while (sheets.hasNext()) {
			InputStream sheet = sheets.next();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
		}

	}

	/**
	 * 处理所有sheet
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void processAllSheets(String filename) throws Exception {

		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst, r.getStylesTable());

		Iterator<InputStream> sheets = r.getSheetsData();
		while (sheets.hasNext()) {
			System.out.println("Processing new sheet:\n");
			InputStream sheet = sheets.next();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
			System.out.println("-----------");
		}
	}

	/**
	 * 获取解析器
	 * 
	 * @param sst
	 * @return
	 * @throws org.xml.sax.SAXException
	 */
	public XMLReader fetchSheetParser(SharedStringsTable sst, StylesTable stylesTable) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		ContentHandler handler = new SheetHandler(sst, stylesTable);
		parser.setContentHandler(handler);
		return parser;
	}

	/**
	 * 自定义解析处理器 See org.xml.sax.helpers.DefaultHandler javadocs
	 */
	private static class SheetHandler extends DefaultHandler {

		private boolean isEnd;
		private String cs;
		private SharedStringsTable sst;
		private String lastContents;
		private boolean nextIsString;

		/** 一行的数据列表 */
		private List<String> rowlist = new ArrayList<String>();
		private int curRow = 0;
		private int curCol = 0;

		/** 前一个元素位置，用来计算其中空的单元格数量，如A6和A8等 */
		private String preRef = null;
		/** 当前元素位置，用来计算其中空的单元格数量，如A6和A8等 */
		private String ref = null;
		/** 定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格 */
		private String maxRef = null;

		private CellDataType nextDataType = CellDataType.SSTINDEX;
		private StylesTable stylesTable;

		/**
		 * 构造
		 * 
		 * @param sst
		 */
		private SheetHandler(SharedStringsTable sst, StylesTable stylesTable) {
			this.sst = sst;
			this.stylesTable = stylesTable;
		}

		/**
		 * 解析一个element的开始时触发事件
		 */
		@Override
		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

			isEnd = false;
			// c => cell
			if (name.equals("c")) {
				// 前一个单元格的位置
				if (this.preRef == null) {
					this.preRef = attributes.getValue("r");
				} else {
					this.preRef = this.ref;
				}
				// 当前单元格的位置，r代表单元格坐标
				ref = attributes.getValue("r");

				this.setNextDataType(attributes);

				// t => 元素类型
				String cellType = attributes.getValue("t");
				if (cellType == null) { // 处理空单元格问题
					nextIsString = true;
					cs = "x";
				} else if (cellType != null && cellType.equals("s")) {
					//字符串类型
					cs = "s";
					nextIsString = true;
				} else {
					nextIsString = false;
					cs = "";
				}

			}
			// Clear contents cache
			lastContents = "";
		}

		/**
		 * 根据element属性设置数据类型
		 * 
		 * @param attributes
		 */
		private void setNextDataType(Attributes attributes) {

			nextDataType = CellDataType.NUMBER;
			// t=>type
			String cellType = attributes.getValue("t");
			if ("b".equals(cellType)) {
				nextDataType = CellDataType.BOOL;
			} else if ("e".equals(cellType)) {
				nextDataType = CellDataType.ERROR;
			} else if ("inlineStr".equals(cellType)) {
				nextDataType = CellDataType.INLINESTR;
			} else if ("s".equals(cellType)) {
				nextDataType = CellDataType.SSTINDEX;
			} else if ("str".equals(cellType)) {
				nextDataType = CellDataType.FORMULA;
			}

			// s=>stype
			String cellStyleStr = attributes.getValue("s");
			if (cellStyleStr != null) {
				int styleIndex = Integer.parseInt(cellStyleStr);
				style = stylesTable.getStyleAt(styleIndex);
				String formatString = style.getDataFormatString();
				if ("m/d/yy" == formatString) {
					// 日期
					nextDataType = CellDataType.DATE;
					// full format is "yyyy-MM-dd hh:mm:ss.SSS";
					formatString = "yyyy-MM-dd";
				}
				if (formatString == null) {
					nextDataType = CellDataType.NULL;
					formatString = BuiltinFormats.getBuiltinFormat(style.getDataFormat());
				}
			}
		}

		/**
		 * 解析一个element元素结束时触发事件
		 */
		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
			// Process the last contents as required.
			// Do now, as characters() may be called more than once
			if (nextIsString) {
				if ("s".equals(cs)) {
					int idx = Integer.parseInt(lastContents);
					lastContents = new XSSFRichTextString(this.sst.getEntryAt(idx)).toString();
					nextIsString = false;
				}
				if ("c".equals(name) && "x".equals(cs)) {
					if (false == isEnd) {
						rowlist.add(curCol, "");
						curCol++;
					}

				}
			}

			isEnd = true;

			// v => contents of a cell
			// Output after we've seen the string contents
			if ("v".equals(name) || "t".equals(name)) {
				final String value = ExcelSaxUtil.getDataValue(nextDataType, lastContents.trim(), style);
				// 补全单元格之间的空单元格
				if (!ref.equals(preRef)) {
					int len = ExcelSaxUtil.countNullCell(preRef, ref);
					for (int i = 0; i < len; i++) {
						rowlist.add(curCol, "");
						curCol++;
					}
				}
				rowlist.add(curCol, value);
				curCol++;
			} else {
				// 如果标签名称为 row，这说明已到行尾，调用 optRows() 方法
				if (name.equals("row")) {
					String value = "";
					// 默认第一行为表头，以该行单元格数目为最大数目
					if (curRow == 0) {
						maxRef = ref;
					}
					// 补全一行尾部可能缺失的单元格
					if (maxRef != null) {
						int len = ExcelSaxUtil.countNullCell(ref, maxRef);

						for (int i = 0; i <= len; i++) {
							// rowlist.add(curCol, "");
							// curCol++;
						}
					}
					// 拼接一行的数据
					for (int i = 0; i < rowlist.size(); i++) {
						if (rowlist.get(i).contains(",")) {
							value += "\"" + rowlist.get(i) + "\",";

						} else {
							if (i == rowlist.size() - 1) {
								value += rowlist.get(i);
							} else {
								value += rowlist.get(i) + ",";
							}
						}
					}
					// 加换行符
					value += "\n";
					// try {
					// writer.write(value);
					// } catch (IOException e) {
					// e.printStackTrace();
					// }
					curRow++;
					// System.out.println(curRow + rowlist.toString()+"------");
					// 一行的末尾重置一些数据
					Console.log(value);
					rowlist.clear();
					curCol = 0;
					preRef = null;
					ref = null;
				}
			}
		}

		/**
		 * 获取element的文本数据
		 */
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			lastContents += new String(ch, start, length);
		}
	}
	public static void main(String[] args) throws Exception {
		ExampleEventUserModelUtil util = new ExampleEventUserModelUtil();
		util.processOneSheet("d:/text.xlsx");
	}

}