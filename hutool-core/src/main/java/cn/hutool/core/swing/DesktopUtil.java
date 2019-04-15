package cn.hutool.core.swing;

import java.awt.Desktop;
import java.io.IOException;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.URLUtil;

/**
 * 桌面相关工具（平台相关）
 * 
 * @author looly
 * @since 4.5.7
 */
public class DesktopUtil {

	/**
	 * 使用平台默认浏览器打开指定URL地址
	 * 
	 * @param url URL地址
	 */
	public static void browse(String url) {
		try {
			Desktop.getDesktop().browse(URLUtil.toURI(url));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
