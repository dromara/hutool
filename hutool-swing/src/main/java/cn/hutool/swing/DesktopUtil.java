package cn.hutool.swing;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.net.url.URLUtil;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * 桌面相关工具（平台相关）<br>
 * Desktop 类允许 Java 应用程序启动已在本机桌面上注册的关联应用程序，以处理 URI 或文件。
 *
 * @author looly
 * @since 4.5.7
 */
public class DesktopUtil {

	/**
	 * 获得{@link Desktop}
	 *
	 * @return {@link Desktop}
	 */
	public static Desktop getDsktop() {
		return Desktop.getDesktop();
	}

	/**
	 * 使用平台默认浏览器打开指定URL地址
	 *
	 * @param url URL地址
	 */
	public static void browse(final String url) {
		browse(URLUtil.toURI(url));
	}

	/**
	 * 使用平台默认浏览器打开指定URI地址
	 *
	 * @param uri URI地址
	 * @since 4.6.3
	 */
	public static void browse(final URI uri) {
		final Desktop dsktop = getDsktop();
		try {
			dsktop.browse(uri);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 启动关联应用程序来打开文件
	 *
	 * @param file URL地址
	 */
	public static void open(final File file) {
		final Desktop dsktop = getDsktop();
		try {
			dsktop.open(file);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 启动关联编辑器应用程序并打开用于编辑的文件
	 *
	 * @param file 文件
	 */
	public static void edit(final File file) {
		final Desktop dsktop = getDsktop();
		try {
			dsktop.edit(file);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 使用关联应用程序的打印命令, 用本机桌面打印设备来打印文件
	 *
	 * @param file 文件
	 */
	public static void print(final File file) {
		final Desktop dsktop = getDsktop();
		try {
			dsktop.print(file);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 使用平台默认邮件客户端打开邮件程序
	 *
	 * @param mailAddress 邮件地址
	 */
	public static void mail(final String mailAddress) {
		final Desktop dsktop = getDsktop();
		try {
			dsktop.mail(URLUtil.toURI(mailAddress));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
