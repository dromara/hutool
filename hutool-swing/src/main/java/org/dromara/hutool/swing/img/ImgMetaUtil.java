/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.swing.img;

import org.dromara.hutool.core.exceptions.UtilException;
import org.dromara.hutool.core.io.IORuntimeException;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片元信息工具类<br>
 * 借助metadata-extractor完成图片元信息的读取，如旋转角度等问题
 *
 * @author wdz
 * @since 6.0.0
 */
public class ImgMetaUtil {

	/**
	 * 获取图片文件旋转角度
	 *
	 * @param file 上传图片
	 * @return 旋转角度
	 * @throws IORuntimeException IO异常
	 */
	public static int getOrientation(final File file) throws IORuntimeException {
		final Metadata metadata;
		try {
			metadata = ImageMetadataReader.readMetadata(file);
		} catch (final ImageProcessingException e) {
			throw new UtilException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return getOrientation(metadata);
	}

	/**
	 * 获取图片旋转角度
	 *
	 * @param in 图片流
	 * @return 旋转角度
	 * @throws IORuntimeException IO异常
	 */
	public static int getOrientation(final InputStream in) throws IORuntimeException {
		final Metadata metadata;
		try {
			metadata = ImageMetadataReader.readMetadata(in);
		} catch (final ImageProcessingException e) {
			throw new UtilException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return getOrientation(metadata);
	}

	/**
	 * 获取旋转角度
	 * @param metadata {@link Metadata}
	 * @return 旋转角度，可能为90,180,270
	 */
	private static int getOrientation(final Metadata metadata) {
		for (final Directory directory : metadata.getDirectories()) {
			for (final Tag tag : directory.getTags()) {
				if ("Orientation".equals(tag.getTagName())) {
					final String orientation = tag.getDescription();
					if (orientation.contains("90")) {
						return 90;
					} else if (orientation.contains("180")) {
						return 180;
					} else if (orientation.contains("270")) {
						return 270;
					}
				}
			}
		}
		return 0;
	}
}
