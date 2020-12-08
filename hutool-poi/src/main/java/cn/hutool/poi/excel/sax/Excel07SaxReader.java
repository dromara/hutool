package cn.hutool.poi.excel.sax;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import cn.hutool.poi.exceptions.POIException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Sax方式读取Excel文件<br>
 * Excel2007格式说明见：http://www.cnblogs.com/wangmingshun/p/6654143.html
 *
 * @author Looly
 * @since 3.1.2
 */
public class Excel07SaxReader implements ExcelSaxReader<Excel07SaxReader> {

	// sheet r:Id前缀
	private static final String RID_PREFIX = "rId";
	private final SheetDataSaxHandler handler;

	/**
	 * 构造
	 *
	 * @param rowHandler 行处理器
	 */
	public Excel07SaxReader(RowHandler rowHandler) {
		this.handler = new SheetDataSaxHandler(rowHandler);
	}

	/**
	 * 设置行处理器
	 *
	 * @param rowHandler 行处理器
	 * @return this
	 */
	public Excel07SaxReader setRowHandler(RowHandler rowHandler) {
		this.handler.setRowHandler(rowHandler);
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
			this.handler.stylesTable = xssfReader.getStylesTable();
		} catch (Exception e) {
			//ignore
		}

		// 获取共享字符串表
		try {
			this.handler.sharedStringsTable = xssfReader.getSharedStringsTable();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} catch (InvalidFormatException e) {
			throw new POIException(e);
		}

		return readSheets(xssfReader, idOrRid);
	}
	// ------------------------------------------------------------------------------ Read end

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
		this.handler.sheetIndex = Integer.parseInt(StrUtil.removePrefixIgnoreCase(idOrRid, RID_PREFIX));
		InputStream sheetInputStream = null;
		try {
			if (this.handler.sheetIndex > -1) {
				// 根据 rId# 或 rSheet# 查找sheet
				sheetInputStream = xssfReader.getSheet(RID_PREFIX + (this.handler.sheetIndex + 1));
				ExcelSaxUtil.readFrom(sheetInputStream, this.handler);
				this.handler.rowHandler.doAfterAllAnalysed();
			} else {
				this.handler.sheetIndex = -1;
				// 遍历所有sheet
				final Iterator<InputStream> sheetInputStreams = xssfReader.getSheetsData();
				while (sheetInputStreams.hasNext()) {
					// 重新读取一个sheet时行归零
					this.handler.index = 0;
					this.handler.sheetIndex++;
					sheetInputStream = sheetInputStreams.next();
					ExcelSaxUtil.readFrom(sheetInputStream, this.handler);
					this.handler.rowHandler.doAfterAllAnalysed();
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
	// --------------------------------------------------------------------------------------- Private method end
}