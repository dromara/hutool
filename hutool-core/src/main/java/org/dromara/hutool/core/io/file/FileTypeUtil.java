/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.io.file;

import org.dromara.hutool.core.codec.binary.HexUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 文件类型判断工具类
 *
 * <p>此工具根据文件的前几位bytes猜测文件类型，对于文本、zip判断不准确，对于视频、图片类型判断准确</p>
 *
 * <p>需要注意的是，xlsx、docx等Office2007格式，全部识别为zip，因为新版采用了OpenXML格式，这些格式本质上是XML文件打包为zip</p>
 *
 * @author Looly
 */
public class FileTypeUtil {

	private static final Map<String, String> FILE_TYPE_MAP = new ConcurrentSkipListMap<>();

	/**
	 * 增加文件类型映射<br>
	 * 如果已经存在将覆盖之前的映射
	 *
	 * @param fileStreamHexHead 文件流头部Hex信息
	 * @param extName           文件扩展名
	 * @return 之前已经存在的文件扩展名
	 */
	public static String putFileType(final String fileStreamHexHead, final String extName) {
		return FILE_TYPE_MAP.put(fileStreamHexHead, extName);
	}

	/**
	 * 移除文件类型映射
	 *
	 * @param fileStreamHexHead 文件流头部Hex信息
	 * @return 移除的文件扩展名
	 */
	public static String removeFileType(final String fileStreamHexHead) {
		return FILE_TYPE_MAP.remove(fileStreamHexHead);
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 *
	 * @param fileStreamHexHead 文件流头部16进制字符串
	 * @return 文件类型，未找到为{@code null}
	 */
	public static String getType(final String fileStreamHexHead) {
		for (final Entry<String, String> fileTypeEntry : FILE_TYPE_MAP.entrySet()) {
			if (StrUtil.startWithIgnoreCase(fileStreamHexHead, fileTypeEntry.getKey())) {
				return fileTypeEntry.getValue();
			}
		}
		final byte[] bytes = (HexUtil.decode(fileStreamHexHead));
		return FileMagicNumber.getMagicNumber(bytes).getExtension();
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 *
	 * @param in           文件流
	 * @param fileHeadSize 自定义读取文件头部的大小
	 * @return 文件类型，未找到为{@code null}
	 * @throws IORuntimeException IO异常
	 */
	public static String getType(final InputStream in, final int fileHeadSize) throws IORuntimeException {
		return getType((IoUtil.readHex(in, fileHeadSize, false)));
	}

	/**
	 * 根据文件流的头部信息获得文件类型<br>
	 * 注意此方法会读取头部一些bytes，造成此流接下来读取时缺少部分bytes<br>
	 * 因此如果想复用此流，流需支持{@link InputStream#reset()}方法。
	 *
	 * @param in      {@link InputStream}
	 * @param isExact 是否精确匹配，如果为false，使用前64个bytes匹配，如果为true，使用前8192bytes匹配
	 * @return 类型，文件的扩展名，in为{@code null}或未找到为{@code null}
	 * @throws IORuntimeException 读取流引起的异常
	 */
	public static String getType(final InputStream in, final boolean isExact) throws IORuntimeException {
		if (null == in) {
			return null;
		}
		return isExact
			? getType(readHex8192Upper(in))
			: getType(readHex64Upper(in));
	}

	/**
	 * 根据文件流的头部信息获得文件类型<br>
	 * 注意此方法会读取头部64个bytes，造成此流接下来读取时缺少部分bytes<br>
	 * 因此如果想复用此流，流需支持{@link InputStream#reset()}方法。
	 *
	 * @param in {@link InputStream}
	 * @return 类型，文件的扩展名，未找到为{@code null}
	 * @throws IORuntimeException 读取流引起的异常
	 */
	public static String getType(final InputStream in) throws IORuntimeException {
		return getType(in, false);
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 * 注意此方法会读取头部64个bytes，造成此流接下来读取时缺少部分bytes<br>
	 * 因此如果想复用此流，流需支持{@link InputStream#reset()}方法。
	 *
	 * <pre>
	 *     1、无法识别类型默认按照扩展名识别
	 *     2、xls、doc、msi头信息无法区分，按照扩展名区分
	 *     3、zip可能为docx、xlsx、pptx、jar、war、ofd头信息无法区分，按照扩展名区分
	 * </pre>
	 *
	 * @param in       {@link InputStream}
	 * @param filename 文件名
	 * @return 类型，文件的扩展名，未找到为{@code null}
	 * @throws IORuntimeException 读取流引起的异常
	 */
	public static String getType(final InputStream in, final String filename) throws IORuntimeException {
		return getType(in, filename, false);
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 * 注意此方法会读取头部一些bytes，造成此流接下来读取时缺少部分bytes<br>
	 * 因此如果想复用此流，流需支持{@link InputStream#reset()}方法。
	 *
	 * <pre>
	 *     1、无法识别类型默认按照扩展名识别
	 *     2、xls、doc、msi头信息无法区分，按照扩展名区分
	 *     3、zip可能为docx、xlsx、pptx、jar、war、ofd头信息无法区分，按照扩展名区分
	 * </pre>
	 *
	 * @param in       {@link InputStream}
	 * @param filename 文件名
	 * @param isExact  是否精确匹配，如果为false，使用前64个bytes匹配，如果为true，使用前8192bytes匹配
	 * @return 类型，文件的扩展名，未找到为{@code null}
	 * @throws IORuntimeException 读取流引起的异常
	 */
	public static String getType(final InputStream in, final String filename, final boolean isExact) throws IORuntimeException {
		String typeName = getType(in, isExact);
		if (null == typeName) {
			// 未成功识别类型，扩展名辅助识别
			typeName = FileNameUtil.extName(filename);
		} else if ("zip".equals(typeName)) {
			// zip可能为docx、xlsx、pptx、jar、war、ofd等格式，扩展名辅助判断
			final String extName = FileNameUtil.extName(filename);
			if ("docx".equalsIgnoreCase(extName)) {
				typeName = "docx";
			} else if ("xlsx".equalsIgnoreCase(extName)) {
				typeName = "xlsx";
			} else if ("pptx".equalsIgnoreCase(extName)) {
				typeName = "pptx";
			} else if ("jar".equalsIgnoreCase(extName)) {
				typeName = "jar";
			} else if ("war".equalsIgnoreCase(extName)) {
				typeName = "war";
			} else if ("ofd".equalsIgnoreCase(extName)) {
				typeName = "ofd";
			} else if ("apk".equalsIgnoreCase(extName)) {
				typeName = "apk";
			}
		} else if ("jar".equals(typeName)) {
			// wps编辑过的.xlsx文件与.jar的开头相同,通过扩展名判断
			final String extName = FileNameUtil.extName(filename);
			if ("xlsx".equalsIgnoreCase(extName)) {
				typeName = "xlsx";
			} else if ("docx".equalsIgnoreCase(extName)) {
				// issue#I47JGH
				typeName = "docx";
			} else if ("pptx".equalsIgnoreCase(extName)) {
				// issue#I5A0GO
				typeName = "pptx";
			} else if ("zip".equalsIgnoreCase(extName)) {
				typeName = "zip";
			} else if ("apk".equalsIgnoreCase(extName)) {
				typeName = "apk";
			}
		}
		return typeName;
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 *
	 * <pre>
	 *     1、无法识别类型默认按照扩展名识别
	 *     2、xls、doc、msi头信息无法区分，按照扩展名区分
	 *     3、zip可能为jar、war头信息无法区分，按照扩展名区分
	 * </pre>
	 *
	 * @param file    文件 {@link File}
	 * @param isExact 是否精确匹配，如果为false，使用前64个bytes匹配，如果为true，使用前8192bytes匹配
	 * @return 类型，文件的扩展名，未找到为{@code null}
	 * @throws IORuntimeException 读取文件引起的异常
	 */
	public static String getType(final File file, final boolean isExact) throws IORuntimeException {
		if (false == FileUtil.isFile(file)) {
			throw new IllegalArgumentException("Not a regular file!");
		}
		InputStream in = null;
		try {
			in = IoUtil.toStream(file);
			return getType(in, file.getName(), isExact);
		} finally {
			IoUtil.closeQuietly(in);
		}
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 *
	 * <pre>
	 *     1、无法识别类型默认按照扩展名识别
	 *     2、xls、doc、msi头信息无法区分，按照扩展名区分
	 *     3、zip可能为jar、war头信息无法区分，按照扩展名区分
	 * </pre>
	 *
	 * @param file 文件 {@link File}
	 * @return 类型，文件的扩展名，未找到为{@code null}
	 * @throws IORuntimeException 读取文件引起的异常
	 */
	public static String getType(final File file) throws IORuntimeException {
		return getType(file, false);
	}

	/**
	 * 通过路径获得文件类型
	 *
	 * @param path    路径，绝对路径或相对ClassPath的路径
	 * @param isExact 是否精确匹配，如果为false，使用前64个bytes匹配，如果为true，使用前8192bytes匹配
	 * @return 类型
	 * @throws IORuntimeException 读取文件引起的异常
	 */
	public static String getTypeByPath(final String path, final boolean isExact) throws IORuntimeException {
		return getType(FileUtil.file(path), isExact);
	}

	/**
	 * 通过路径获得文件类型
	 *
	 * @param path 路径，绝对路径或相对ClassPath的路径
	 * @return 类型
	 * @throws IORuntimeException 读取文件引起的异常
	 */
	public static String getTypeByPath(final String path) throws IORuntimeException {
		return getTypeByPath(path, false);
	}

	/**
	 * 从流中读取前8192个byte并转换为16进制，字母部分使用大写
	 *
	 * @param in {@link InputStream}
	 * @return 16进制字符串
	 * @throws IORuntimeException IO异常
	 */
	private static String readHex8192Upper(final InputStream in) throws IORuntimeException {
		try {
			return IoUtil.readHex(in, Math.min(8192, in.available()), false);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 从流中读取前64个byte并转换为16进制，字母部分使用大写
	 *
	 * @param in {@link InputStream}
	 * @return 16进制字符串
	 * @throws IORuntimeException IO异常
	 */
	private static String readHex64Upper(final InputStream in) throws IORuntimeException {
		return IoUtil.readHex(in, 64, false);
	}
}
