package com.xiaoleilu.hutool.poi.excel.sax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.poi.exceptions.POIException;

/**
 * Sax方式读取Excel文件<br>
 * Excel2007格式说明见：http://www.cnblogs.com/wangmingshun/p/6654143.html
 * 
 * @author Looly
 * @since 3.1.2
 */
public class ExcelSaxReader {

	/** Apache  Xerces*/
	private static final String CLASS_SAXPARSER = "org.apache.xerces.parsers.SAXParser";

	private XSSFReader reader;
	private int sheelIndex;
	private InputStream sheetIn;
	private InputSource sheetSource;
	private XMLReader parser;

	// --------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * 
	 * @param excelFile Excel文件
	 * @param sheetIndex Sheet序号
	 */
	public ExcelSaxReader(File excelFile, int sheetIndex) {
		this.sheelIndex = sheetIndex;
		try {
			init(OPCPackage.open(excelFile));
		} catch (InvalidFormatException e) {
			throw new POIException(e);
		}
	}

	/**
	 * 构造
	 * 
	 * @param excelStream Excel流
	 * @param sheetIndex Sheet序号
	 */
	public ExcelSaxReader(InputStream excelStream, int sheetIndex) {
		this.sheelIndex = sheetIndex;
		try {
			init(OPCPackage.open(excelStream));
		} catch (InvalidFormatException e) {
			throw new POIException(e);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 构造
	 * 
	 * @param pkg {@link OPCPackage}
	 * @param sheetIndex Sheet序号
	 */
	public ExcelSaxReader(OPCPackage pkg, int sheetIndex) {
		this.sheelIndex = sheetIndex;
		init(pkg);
	}
	// --------------------------------------------------------------------------------------------- Constructor end

	/**
	 * 开始读取Excel流
	 * 
	 * @return this
	 */
	public ExcelSaxReader read() {
		try {
			this.parser.parse(this.sheetSource);
		} catch (SAXException e) {
			throw new POIException(e);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(this.sheetIn);
		}
		return this;
	}

	// --------------------------------------------------------------------------------------------- Private method start
	/**
	 * 初始化
	 * 
	 * @param pkg {@link OPCPackage}
	 */
	private void init(OPCPackage pkg) {
		Iterator<InputStream> sheetsData;
		try {
			this.reader = new XSSFReader(pkg);

			sheetsData = this.reader.getSheetsData();
			int index = 0;
			while (sheetsData.hasNext()) {
				if (index == this.sheelIndex) {
					this.sheetIn = sheetsData.next();
					this.sheetSource = new InputSource(this.sheetIn);
				}
				index++;
			}
			this.parser = fetchSheetParser(this.reader.getSharedStringsTable(), this.reader.getStylesTable());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} catch (OpenXML4JException | SAXException e) {
			throw new POIException(e);
		}
	}

	/**
	 * 获得XML解析器
	 * 
	 * @param table @{SharedStringsTable}
	 * @param stylesTable 样式表
	 * @return {@link XMLReader}
	 * @throws SAXException
	 */
	private XMLReader fetchSheetParser(SharedStringsTable table, StylesTable stylesTable) throws SAXException {
		final XMLReader parser = XMLReaderFactory.createXMLReader(CLASS_SAXPARSER);
		parser.setContentHandler(new SheetHandler(table, stylesTable));
		return parser;
	}
	// --------------------------------------------------------------------------------------------- Private method end
}
