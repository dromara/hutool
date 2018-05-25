package cn.hutool.extra.servlet.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import cn.hutool.core.util.ArrayUtil;

/**
 * HttpRequest解析器<br>
 * 来自Jodd
 * 
 * @author jodd.org
 */
public class MultipartFormData {

	/** 请求参数 */
	private Map<String, String[]> requestParameters = new HashMap<String, String[]>();
	/** 请求文件 */
	private Map<String, UploadFile[]> requestFiles = new HashMap<String, UploadFile[]>();

	/** 是否解析完毕 */
	private boolean loaded;
	/** 上传选项 */
	private UploadSetting setting;

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
	public MultipartFormData(UploadSetting uploadSetting) {
		this.setting = uploadSetting == null ? new UploadSetting() : uploadSetting;
	}
	// --------------------------------------------------------------------- Constructor end

	/**
	 * 解析上传文件和表单数据
	 * 
	 * @param request Http请求
	 * @throws IOException IO异常
	 */
	public void parseRequest(ServletRequest request) throws IOException {
		parseRequestStream(request.getInputStream(), request.getCharacterEncoding());
	}

	/**
	 * 提取上传的文件和表单数据
	 * 
	 * @param inputStream HttpRequest流
	 * @param charset 编码
	 * @throws IOException IO异常
	 */
	public void parseRequestStream(InputStream inputStream, String charset) throws IOException {
		setLoaded();

		MultipartRequestInputStream input = new MultipartRequestInputStream(inputStream);
		input.readBoundary();
		while (true) {
			UploadFileHeader header = input.readDataHeader(charset);
			if (header == null) {
				break;
			}

			if (header.isFile == true) {
				// 文件类型的表单项
				String fileName = header.fileName;
				if (fileName.length() > 0 && header.contentType.contains("application/x-macbinary")) {
					input.skipBytes(128);
				}
				UploadFile newFile = new UploadFile(header, setting);
				newFile.processStream(input);

				putFile(header.formFieldName, newFile);
			} else {
				// 标准表单项
				ByteArrayOutputStream fbos = new ByteArrayOutputStream(1024);
				input.copy(fbos);
				String value = (charset != null) ? new String(fbos.toByteArray(), charset) : new String(fbos.toByteArray());
				putParameter(header.formFieldName, value);
			}

			input.skipBytes(1);
			input.mark(1);

			// read byte, but may be end of stream
			int nextByte = input.read();
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
	public String getParam(String paramName) {
		if (requestParameters == null) {
			return null;
		}
		String[] values = requestParameters.get(paramName);
		if (ArrayUtil.isNotEmpty(values)) {
			return values[0];
		}
		return null;
	}

	/**
	 * @return 获得参数名集合
	 */
	public Set<String> getParamNames() {
		if (requestParameters == null) {
			return Collections.emptySet();
		}
		return requestParameters.keySet();
	}

	/**
	 * 获得数组表单值
	 * 
	 * @param paramName 参数名
	 * @return 数组表单值
	 */
	public String[] getArrayParam(String paramName) {
		if (requestParameters == null) {
			return null;
		}
		return requestParameters.get(paramName);
	}

	/**
	 * 获取所有属性的集合
	 * 
	 * @return 所有属性的集合
	 */
	public Map<String, String[]> getParamMap() {
		return requestParameters;
	}

	// --------------------------------------------------------------------------- Files parameters
	/**
	 * 获取上传的文件
	 * 
	 * @param paramName 文件参数名称
	 * @return 上传的文件， 如果无为null
	 */
	public UploadFile getFile(String paramName) {
		UploadFile[] values = getFiles(paramName);
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
	public UploadFile[] getFiles(String paramName) {
		if (requestFiles == null) {
			return null;
		}
		return requestFiles.get(paramName);
	}

	/**
	 * 获取上传的文件属性名集合
	 * 
	 * @return 上传的文件属性名集合
	 */
	public Set<String> getFileParamNames() {
		if (requestFiles == null) {
			return Collections.emptySet();
		}
		return requestFiles.keySet();
	}

	/**
	 * 获取文件映射
	 * 
	 * @return 文件映射
	 */
	public Map<String, UploadFile[]> getFileMap() {
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
	private void putFile(String name, UploadFile uploadFile) {
		UploadFile[] fileUploads = requestFiles.get(name);
		fileUploads = fileUploads == null ? new UploadFile[] { uploadFile } : ArrayUtil.append(fileUploads, uploadFile);
		requestFiles.put(name, fileUploads);
	}

	/**
	 * 加入普通参数
	 * 
	 * @param name 参数名
	 * @param value 参数值
	 */
	private void putParameter(String name, String value) {
		String[] params = requestParameters.get(name);
		params = params == null ? new String[] { value } : ArrayUtil.append(params, value);
		requestParameters.put(name, params);
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
