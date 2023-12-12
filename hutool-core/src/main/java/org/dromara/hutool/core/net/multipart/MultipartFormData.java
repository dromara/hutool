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

package org.dromara.hutool.core.net.multipart;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.map.multi.ListValueMap;
import org.dromara.hutool.core.map.multi.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * HttpRequest解析器<br>
 * 来自Jodd
 *
 * @author jodd.org
 */
public class MultipartFormData {

	/** 请求参数 */
	private final MultiValueMap<String, String> requestParameters = new ListValueMap<>();
	/** 请求文件 */
	private final MultiValueMap<String, UploadFile> requestFiles = new ListValueMap<>();
	/** 上传选项 */
	private final UploadSetting setting;

	/** 是否解析完毕 */
	private boolean loaded;

	// --------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public MultipartFormData() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param uploadSetting 上传设定
	 */
	public MultipartFormData(final UploadSetting uploadSetting) {
		this.setting = uploadSetting == null ? new UploadSetting() : uploadSetting;
	}
	// --------------------------------------------------------------------- Constructor end

	/**
	 * 提取上传的文件和表单数据
	 *
	 * @param inputStream HttpRequest流
	 * @param charset 编码
	 * @throws IOException IO异常
	 */
	public void parseRequestStream(final InputStream inputStream, final Charset charset) throws IOException {
		setLoaded();

		final MultipartRequestInputStream input = new MultipartRequestInputStream(inputStream);
		input.readBoundary();
		while (true) {
			final UploadFileHeader header = input.readDataHeader(charset);
			if (header == null) {
				break;
			}

			if (header.isFile == true) {
				// 文件类型的表单项
				final String fileName = header.fileName;
				if (!fileName.isEmpty() && header.contentType.contains("application/x-macbinary")) {
					input.skipBytes(128);
				}
				final UploadFile newFile = new UploadFile(header, setting);
				if(newFile.processStream(input)){
					putFile(header.formFieldName, newFile);
				}
			} else {
				// 标准表单项
				putParameter(header.formFieldName, input.readString(charset));
			}

			input.skipBytes(1);
			input.mark(1);

			// read byte, but may be end of stream
			final int nextByte = input.read();
			if (nextByte == -1 || nextByte == '-') {
				input.reset();
				break;
			}
			input.reset();
		}
	}

	// ---------------------------------------------------------------- parameters
	/**
	 * 返回单一参数值，如果有多个只返回第一个
	 *
	 * @param paramName 参数名
	 * @return null未找到，否则返回值
	 */
	public String getParam(final String paramName) {
		final Collection<String> values = getListParam(paramName);
		if (CollUtil.isNotEmpty(values)) {
			return CollUtil.get(values, 0);
		}
		return null;
	}

	/**
	 * @return 获得参数名集合
	 */
	public Set<String> getParamNames() {
		return requestParameters.keySet();
	}

	/**
	 * 获得数组表单值
	 *
	 * @param paramName 参数名
	 * @return 数组表单值
	 */
	public String[] getArrayParam(final String paramName) {
		final Collection<String> listParam = getListParam(paramName);
		if(null != listParam){
			return listParam.toArray(new String[0]);
		}
		return null;
	}

	/**
	 * 获得集合表单值
	 *
	 * @param paramName 参数名
	 * @return 数组表单值
	 * @since 5.3.0
	 */
	public Collection<String> getListParam(final String paramName) {
		return requestParameters.get(paramName);
	}

	/**
	 * 获取所有属性的集合
	 *
	 * @return 所有属性的集合
	 */
	public Map<String, String[]> getParamMap() {
		return Convert.toMap(String.class, String[].class, getParamListMap());
	}

	/**
	 * 获取所有属性的集合
	 *
	 * @return 所有属性的集合
	 */
	public MultiValueMap<String, String> getParamListMap() {
		return this.requestParameters;
	}

	// --------------------------------------------------------------------------- Files parameters
	/**
	 * 获取上传的文件
	 *
	 * @param paramName 文件参数名称
	 * @return 上传的文件， 如果无为null
	 */
	public UploadFile getFile(final String paramName) {
		final UploadFile[] values = getFiles(paramName);
		if ((values != null) && (values.length > 0)) {
			return values[0];
		}
		return null;
	}

	/**
	 * 获得某个属性名的所有文件<br>
	 * 当表单中两个文件使用同一个name的时候
	 *
	 * @param paramName 属性名
	 * @return 上传的文件列表
	 */
	public UploadFile[] getFiles(final String paramName) {
		final Collection<UploadFile> fileList = getFileList(paramName);
		if(null != fileList){
			return fileList.toArray(new UploadFile[0]);
		}
		return null;
	}

	/**
	 * 获得某个属性名的所有文件<br>
	 * 当表单中两个文件使用同一个name的时候
	 *
	 * @param paramName 属性名
	 * @return 上传的文件列表
	 * @since 5.3.0
	 */
	public Collection<UploadFile> getFileList(final String paramName) {
		return requestFiles.get(paramName);
	}

	/**
	 * 获取上传的文件属性名集合
	 *
	 * @return 上传的文件属性名集合
	 */
	public Set<String> getFileParamNames() {
		return requestFiles.keySet();
	}

	/**
	 * 获取文件映射
	 *
	 * @return 文件映射
	 */
	public Map<String, UploadFile[]> getFileMap() {
		return Convert.toMap(String.class, UploadFile[].class, getFileListValueMap());
	}

	/**
	 * 获取文件映射
	 *
	 * @return 文件映射
	 */
	public MultiValueMap<String, UploadFile> getFileListValueMap() {
		return this.requestFiles;
	}

	// --------------------------------------------------------------------------- Load
	/**
	 * 是否已被解析
	 *
	 * @return 如果流已被解析返回true
	 */
	public boolean isLoaded() {
		return loaded;
	}

	// ---------------------------------------------------------------- Private method start
	/**
	 * 加入上传文件
	 *
	 * @param name 参数名
	 * @param uploadFile 文件
	 */
	private void putFile(final String name, final UploadFile uploadFile) {
		this.requestFiles.putValue(name, uploadFile);
	}

	/**
	 * 加入普通参数
	 *
	 * @param name 参数名
	 * @param value 参数值
	 */
	private void putParameter(final String name, final String value) {
		this.requestParameters.putValue(name, value);
	}

	/**
	 * 设置使输入流为解析状态，如果已解析，则抛出异常
	 *
	 * @throws IOException IO异常
	 */
	private void setLoaded() throws IOException {
		if (loaded == true) {
			throw new IOException("Multi-part request already parsed.");
		}
		loaded = true;
	}
	// ---------------------------------------------------------------- Private method end
}
