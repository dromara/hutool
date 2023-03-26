package cn.hutool.http.client.body;

import cn.hutool.core.io.file.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.stream.SyncInputStream;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.regex.ReUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.client.Response;
import cn.hutool.http.html.HtmlUtil;
import cn.hutool.http.meta.Header;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 响应体部分封装
 *
 * @author looly
 */
public class ResponseBody implements HttpBody, Closeable {

	private final Response response;
	/**
	 * Http请求原始流
	 */
	private final SyncInputStream bodyStream;

	/**
	 * 构造
	 *
	 * @param response         响应体
	 * @param in               HTTP主体响应流
	 * @param isAsync          是否异步模式
	 * @param isIgnoreEOFError 是否忽略EOF错误
	 */
	public ResponseBody(final Response response, final InputStream in, final boolean isAsync, final boolean isIgnoreEOFError) {
		this.response = response;
		this.bodyStream = new SyncInputStream(in, response.contentLength(), isAsync, isIgnoreEOFError);
	}

	@Override
	public String getContentType() {
		return response.header(Header.CONTENT_TYPE);
	}

	@Override
	public InputStream getStream() {
		return this.bodyStream;
	}

	@Override
	public void write(final OutputStream out) {
		write(out, false, null);
	}

	/**
	 * 同步数据到内存，以bytes形式存储
	 *
	 * @return this
	 */
	public ResponseBody sync() {
		this.bodyStream.sync();
		return this;
	}

	/**
	 * 获取响应内容的bytes
	 *
	 * @return 响应内容bytes
	 */
	public byte[] getBytes() {
		return this.bodyStream.readBytes();
	}

	/**
	 * 获取响应字符串，自动识别判断编码
	 *
	 * @return 响应字符串
	 */
	public String getString() {
		return HtmlUtil.getString(getBytes(), response.charset(), true);
	}

	/**
	 * 将响应内容写出到{@link OutputStream}<br>
	 * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
	 * 写出后会关闭Http流（异步模式）
	 *
	 * @param out            写出的流
	 * @param isCloseOut     是否关闭输出流
	 * @param streamProgress 进度显示接口，通过实现此接口显示下载进度
	 * @return 写出bytes数
	 * @since 3.3.2
	 */
	public long write(final OutputStream out, final boolean isCloseOut, final StreamProgress streamProgress) {
		Assert.notNull(out, "[out] must be not null!");
		try {
			return this.bodyStream.copyTo(out, streamProgress);
		} finally {
			if (isCloseOut) {
				IoUtil.closeQuietly(out);
			}
		}
	}

	/**
	 * 将响应内容写出到文件
	 *
	 * @param targetFileOrDir 写出到的文件或目录的路径
	 * @return 写出的文件
	 */
	public File write(final String targetFileOrDir) {
		return write(FileUtil.file(targetFileOrDir));
	}

	/**
	 * 将响应内容写出到文件<br>
	 * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
	 * 写出后会关闭Http流（异步模式）
	 *
	 * @param targetFileOrDir 写出到的文件或目录
	 * @return 写出的文件
	 */
	public File write(final File targetFileOrDir) {
		return write(targetFileOrDir, null);
	}

	/**
	 * 将响应内容写出到文件-避免未完成的文件
	 * 来自：<a href="https://gitee.com/dromara/hutool/pulls/407">https://gitee.com/dromara/hutool/pulls/407</a><br>
	 * 此方法原理是先在目标文件同级目录下创建临时文件，下载之，等下载完毕后重命名，避免因下载错误导致的文件不完整。
	 *
	 * @param targetFileOrDir 写出到的文件或目录
	 * @param streamProgress  进度显示接口，通过实现此接口显示下载进度
	 * @return 写出的文件对象
	 */
	public File write(final File targetFileOrDir, final StreamProgress streamProgress) {
		return write(targetFileOrDir, null, streamProgress);
	}

	/**
	 * 将响应内容写出到文件-避免未完成的文件
	 * 来自：<a href="https://gitee.com/dromara/hutool/pulls/407">https://gitee.com/dromara/hutool/pulls/407</a><br>
	 * 此方法原理是先在目标文件同级目录下创建临时文件，下载之，等下载完毕后重命名，避免因下载错误导致的文件不完整。
	 *
	 * @param targetFileOrDir 写出到的文件或目录
	 * @param tempFileSuffix  临时文件后缀，默认".temp"
	 * @param streamProgress  进度显示接口，通过实现此接口显示下载进度
	 * @return 写出的文件对象
	 * @since 5.7.12
	 */
	public File write(final File targetFileOrDir, final String tempFileSuffix, final StreamProgress streamProgress) {
		Assert.notNull(targetFileOrDir, "[targetFileOrDir] must be not null!");
		File outFile = getTargetFile(targetFileOrDir, null);
		// 目标文件真实名称
		final String fileName = outFile.getName();

		// 临时文件
		outFile = new File(outFile.getParentFile(), FileNameUtil.addTempSuffix(fileName, tempFileSuffix));

		try {
			outFile = writeDirect(outFile, null, streamProgress);
			// 重命名下载好的临时文件
			return FileUtil.rename(outFile, fileName, true);
		} catch (final Throwable e) {
			// 异常则删除临时文件
			FileUtil.del(outFile);
			throw new HttpException(e);
		}
	}

	/**
	 * 将响应内容直接写出到文件，目标为目录则从Content-Disposition中获取文件名
	 *
	 * @param targetFileOrDir 写出到的文件
	 * @param customParamName 自定义的Content-Disposition中文件名的参数名
	 * @param streamProgress  进度显示接口，通过实现此接口显示下载进度
	 * @return 写出的文件
	 */
	public File writeDirect(final File targetFileOrDir, final String customParamName, final StreamProgress streamProgress) {
		Assert.notNull(targetFileOrDir, "[targetFileOrDir] must be not null!");

		final File outFile = getTargetFile(targetFileOrDir, customParamName);
		write(FileUtil.getOutputStream(outFile), true, streamProgress);

		return outFile;
	}

	@Override
	public void close() throws IOException {
		this.bodyStream.close();
	}

	@Override
	public String toString() {
		return getString();
	}

	// region ---------------------------------------------------------------------------- Private Methods

	/**
	 * 从响应头补全下载文件名，返回补全名称后的文件
	 *
	 * @param targetFileOrDir 目标文件夹或者目标文件
	 * @param customParamName 自定义的参数名称，如果传入{@code null}，默认使用"filename"
	 * @return File 保存的文件
	 * @since 5.4.1
	 */
	private File getTargetFile(final File targetFileOrDir, final String customParamName) {
		if (false == targetFileOrDir.isDirectory()) {
			// 非目录直接返回
			return targetFileOrDir;
		}

		// 从头信息中获取文件名
		final String fileName = getFileNameFromDisposition(ObjUtil.defaultIfNull(customParamName, "filename"));
		if (StrUtil.isBlank(fileName)) {
			throw new HttpException("Can`t get file name from [Content-Disposition]!");
		}
		return FileUtil.file(targetFileOrDir, fileName);
	}

	/**
	 * 从Content-Disposition头中获取文件名
	 *
	 * @param paramName 文件名的参数名
	 * @return 文件名，empty表示无
	 * @since 5.8.10
	 */
	private String getFileNameFromDisposition(final String paramName) {
		String fileName = null;
		final String disposition = response.header(Header.CONTENT_DISPOSITION);
		if (StrUtil.isNotBlank(disposition)) {
			fileName = ReUtil.get(paramName + "=\"(.*?)\"", disposition, 1);
			if (StrUtil.isBlank(fileName)) {
				fileName = StrUtil.subAfter(disposition, paramName + "=", true);
			}
		}
		return fileName;
	}
	// endregion ---------------------------------------------------------------------------- Private Methods
}
