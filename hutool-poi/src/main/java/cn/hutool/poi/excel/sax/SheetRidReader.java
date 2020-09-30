package cn.hutool.poi.excel.sax;

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
import java.util.HashMap;
import java.util.Map;

/**
 * 在Sax方式读取Excel时，读取sheet标签中sheetId和rid的对应关系，类似于:
 * <pre>
 * &lt;sheet name="Sheet6" sheetId="4" r:id="6"/&gt;
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

	private final static String TAG_NAME = "sheet";
	private final static String RID_ATTR = "r:id";
	private final static String SHEET_ID_ATTR = "sheetId";
	private final static String NAME_ATTR = "name";

	private final Map<String, String> ID_RID_MAP = new HashMap<>();
	private final Map<String, String> NAME_RID_MAP = new HashMap<>();

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
	 * 根据sheetId获取rid
	 *
	 * @param sheetId Sheet的ID
	 * @return rid
	 */
	public String getRidBySheetId(String sheetId) {
		return ID_RID_MAP.get(sheetId);
	}

	/**
	 * 根据sheet name获取rid
	 *
	 * @param sheetName Sheet的name
	 * @return rid
	 */
	public String getRidByName(String sheetName) {
		return NAME_RID_MAP.get(sheetName);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if (TAG_NAME.equalsIgnoreCase(localName)) {
			final int length = attributes.getLength();
			String sheetId = null;
			String rid = null;
			String name = null;
			for (int i = 0; i < length; i++) {
				switch (attributes.getLocalName(i)) {
					case SHEET_ID_ATTR:
						sheetId = attributes.getValue(i);
						break;
					case RID_ATTR:
						rid = attributes.getValue(i);
						break;
					case NAME_ATTR:
						name = attributes.getValue(i);
						break;
				}
				if (StrUtil.isNotEmpty(sheetId)) {
					ID_RID_MAP.put(sheetId, rid);
				}
				if (StrUtil.isNotEmpty(name)) {
					NAME_RID_MAP.put(name, rid);
				}
			}
		}
	}
}
