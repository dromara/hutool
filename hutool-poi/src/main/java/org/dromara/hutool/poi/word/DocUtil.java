/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.poi.word;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.poi.POIException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.IOException;

/**
 * Word Document工具
 *
 * @author looly
 * @since 4.4.1
 */
public class DocUtil {

	/**
	 * 创建{@link XWPFDocument}，如果文件已存在则读取之，否则创建新的
	 *
	 * @param file docx文件
	 * @return {@link XWPFDocument}
	 */
	public static XWPFDocument create(final File file) {
		try {
			return FileUtil.exists(file) ? new XWPFDocument(OPCPackage.open(file)) : new XWPFDocument();
		} catch (final InvalidFormatException e) {
			throw new POIException(e);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
