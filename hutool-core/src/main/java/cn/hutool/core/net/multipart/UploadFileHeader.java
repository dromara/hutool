package cn.hutool.core.net.multipart;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 上传的文件的头部信息<br>
 * 来自Jodd
 *
 * @author jodd.org
 */
public class UploadFileHeader {

	// String dataHeader;
	String formFieldName;

	String formFileName;
	String path;
	String fileName;

	boolean isFile;
	String contentType;
	String mimeType;
	String mimeSubtype;
	String contentDisposition;

	UploadFileHeader(String dataHeader) {
		processHeaderString(dataHeader);
	}

	// ---------------------------------------------------------------- public interface

	/**
	 * Returns <code>true</code> if uploaded data are correctly marked as a file. This is true if header contains string 'filename'.
	 *
	 * @return 是否为文件
	 */
	public boolean isFile() {
		return isFile;
	}

	/**
	 * 返回表单字段名
	 *
	 * @return 表单字段名
	 */
	public String getFormFieldName() {
		return formFieldName;
	}

	/**
	 * 返回表单中的文件名，来自客户端传入
	 *
	 * @return 表单文件名
	 */
	public String getFormFileName() {
		return formFileName;
	}

	/**
	 * 获取文件名，不包括路径
	 *
	 * @return 文件名
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Returns uploaded content type. It is usually in the following form:<br>
	 * mime_type/mime_subtype.
	 *
	 * @return content type
	 * @see #getMimeType()
	 * @see #getMimeSubtype()
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Returns file types MIME.
	 *
	 * @return types MIME
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Returns file sub type MIME.
	 *
	 * @return sub type MIME
	 */
	public String getMimeSubtype() {
		return mimeSubtype;
	}

	/**
	 * Returns content disposition. Usually it is 'form-data'.
	 *
	 * @return content disposition
	 */
	public String getContentDisposition() {
		return contentDisposition;
	}

	// ---------------------------------------------------------------- Private Method

	/**
	 * 获得头信息字符串字符串中指定的值
	 *
	 * @param dataHeader 头信息
	 * @param fieldName  字段名
	 * @return 字段值
	 */
	private String getDataFieldValue(String dataHeader, String fieldName) {
		String value = null;
		String token = StrUtil.format("{}=\"", fieldName);
		int pos = dataHeader.indexOf(token);
		if (pos > 0) {
			int start = pos + token.length();
			int end = dataHeader.indexOf('"', start);
			if ((start > 0) && (end > 0)) {
				value = dataHeader.substring(start, end);
			}
		}
		return value;
	}

	/**
	 * 头信息中获得content type
	 *
	 * @param dataHeader data header string
	 * @return content type or an empty string if no content type defined
	 */
	private String getContentType(String dataHeader) {
		String token = "Content-Type:";
		int start = dataHeader.indexOf(token);
		if (start == -1) {
			return StrUtil.EMPTY;
		}
		start += token.length();
		return dataHeader.substring(start);
	}

	private String getContentDisposition(String dataHeader) {
		int start = dataHeader.indexOf(':') + 1;
		int end = dataHeader.indexOf(';');
		return dataHeader.substring(start, end);
	}

	private String getMimeType(String ContentType) {
		int pos = ContentType.indexOf('/');
		if (pos == -1) {
			return ContentType;
		}
		return ContentType.substring(1, pos);
	}

	private String getMimeSubtype(String ContentType) {
		int start = ContentType.indexOf('/');
		if (start == -1) {
			return ContentType;
		}
		start++;
		return ContentType.substring(start);
	}

	/**
	 * 处理头字符串，使之转化为字段
	 *
	 * @param dataHeader 头字符串
	 */
	private void processHeaderString(String dataHeader) {
		isFile = dataHeader.indexOf("filename") > 0;
		formFieldName = getDataFieldValue(dataHeader, "name");
		if (isFile) {
			formFileName = getDataFieldValue(dataHeader, "filename");
			if (formFileName == null) {
				return;
			}
			if (formFileName.length() == 0) {
				path = StrUtil.EMPTY;
				fileName = StrUtil.EMPTY;
			}
			int ls = FileUtil.lastIndexOfSeparator(formFileName);
			if (ls == -1) {
				path = StrUtil.EMPTY;
				fileName = formFileName;
			} else {
				path = formFileName.substring(0, ls);
				fileName = formFileName.substring(ls);
			}
			if (fileName.length() > 0) {
				this.contentType = getContentType(dataHeader);
				mimeType = getMimeType(contentType);
				mimeSubtype = getMimeSubtype(contentType);
				contentDisposition = getContentDisposition(dataHeader);
			}
		}
	}
}
