package cn.hutool.poi.excel.sax;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.exceptions.POIException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 在Sax方式读取Excel时，读取sheet标签中sheetId和rid的对应关系，类似于:
 * <pre>
 * &lt;sheet name="Sheet6" sheetId="4" r:id="rId6"/&gt;
 * </pre>
 * <p>
 * 读取结果为：
 *
 * <pre>
 *     {"4": "6"}
 * </pre>
 *
 * @author looly
 * @since 5.4.4
 */
public class SheetRidReader extends DefaultHandler {

	/**
	 * 从{@link XSSFReader}中解析sheet名、sheet id等相关信息
	 *
	 * @param reader {@link XSSFReader}
	 * @return SheetRidReader
	 * @since 5.7.17
	 */
	public static SheetRidReader parse(XSSFReader reader) {
		return new SheetRidReader().read(reader);
	}

	private final static String TAG_NAME = "sheet";
	private final static String RID_ATTR = "r:id";
	private final static String SHEET_ID_ATTR = "sheetId";
	private final static String NAME_ATTR = "name";

	private final Map<Integer, Integer> ID_RID_MAP = new LinkedHashMap<>();
	private final Map<String, Integer> NAME_RID_MAP = new LinkedHashMap<>();

	/**
	 * 读取Wordkbook的XML中sheet标签中sheetId和rid的对应关系
	 *
	 * @param xssfReader XSSF读取器
	 * @return this
	 */
	public SheetRidReader read(XSSFReader xssfReader) {
		InputStream workbookData = null;
		try {
			workbookData = xssfReader.getWorkbookData();
			ExcelSaxUtil.readFrom(workbookData, this);
		} catch (InvalidFormatException e) {
			throw new POIException(e);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(workbookData);
		}
		return this;
	}

	/**
	 * 根据sheetId获取rid，从1开始
	 *
	 * @param sheetId Sheet的ID，从1开始
	 * @return rid，从1开始
	 */
	public Integer getRidBySheetId(int sheetId) {
		return ID_RID_MAP.get(sheetId);
	}

	/**
	 * 根据sheetId获取rid，从0开始
	 *
	 * @param sheetId Sheet的ID，从0开始
	 * @return rid，从0开始
	 * @since 5.5.5
	 */
	public Integer getRidBySheetIdBase0(int sheetId) {
		final Integer rid = getRidBySheetId(sheetId + 1);
		if (null != rid) {
			return rid - 1;
		}
		return null;
	}

	/**
	 * 根据sheet name获取rid，从1开始
	 *
	 * @param sheetName Sheet的name
	 * @return rid，从1开始
	 */
	public Integer getRidByName(String sheetName) {
		return NAME_RID_MAP.get(sheetName);
	}

	/**
	 * 根据sheet name获取rid，从0开始
	 *
	 * @param sheetName Sheet的name
	 * @return rid，从0开始
	 * @since 5.5.5
	 */
	public Integer getRidByNameBase0(String sheetName) {
		final Integer rid = getRidByName(sheetName);
		if (null != rid) {
			return rid - 1;
		}
		return null;
	}

	/**
	 * 通过sheet的序号获取rid
	 *
	 * @param index 序号，从0开始
	 * @return rid
	 * @since 5.5.7
	 */
	public Integer getRidByIndex(int index) {
		return CollUtil.get(this.NAME_RID_MAP.values(), index);
	}

	/**
	 * 通过sheet的序号获取rid
	 *
	 * @param index 序号，从0开始
	 * @return rid，从0开始
	 * @since 5.5.7
	 */
	public Integer getRidByIndexBase0(int index) {
		final Integer rid = CollUtil.get(this.NAME_RID_MAP.values(), index);
		if (null != rid) {
			return rid - 1;
		}
		return null;
	}

	/**
	 * 获取所有sheet名称
	 *
	 * @return sheet名称
	 * @since 5.7.17
	 */
	public List<String> getSheetNames() {
		return ListUtil.toList(this.NAME_RID_MAP.keySet());
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if (TAG_NAME.equalsIgnoreCase(localName)) {
			final String ridStr = attributes.getValue(RID_ATTR);
			if (StrUtil.isEmpty(ridStr)) {
				return;
			}
			final int rid = Integer.parseInt(StrUtil.removePrefixIgnoreCase(ridStr, Excel07SaxReader.RID_PREFIX));

			// sheet名和rid映射
			final String name = attributes.getValue(NAME_ATTR);
			if (StrUtil.isNotEmpty(name)) {
				NAME_RID_MAP.put(name, rid);
			}

			// sheetId和rid映射
			final String sheetIdStr = attributes.getValue(SHEET_ID_ATTR);
			if (StrUtil.isNotEmpty(sheetIdStr)) {
				ID_RID_MAP.put(Integer.parseInt(sheetIdStr), rid);
			}
		}
	}
}
