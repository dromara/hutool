package cn.hutool.extra.servlet;

import cn.hutool.core.util.ByteUtil;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * ServletUtil工具类测试
 *
 * @author dazer
 * @see ServletUtil
 * @see JakartaServletUtil
 */
public class ServletUtilTest {

	@Test
	@Ignore
	public void writeTest() {
		final HttpServletResponse response = null;
		final byte[] bytes = ByteUtil.toUtf8Bytes("地球是我们共同的家园，需要大家珍惜.");

		//下载文件
		// 这里没法直接测试，直接写到这里，方便调用；
		//noinspection ConstantConditions
		if (response != null) {
			final String fileName = "签名文件.pdf";
			final String contentType = "application/pdf";// application/octet-stream、image/jpeg、image/gif
			response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 必须设置否则乱码; 但是 safari乱码
			ServletUtil.write(response, new ByteArrayInputStream(bytes), contentType, fileName);
		}
	}

	@Test
	@Ignore
	public void jakartaWriteTest() {
		final jakarta.servlet.http.HttpServletResponse response = null;
		final byte[] bytes = ByteUtil.toUtf8Bytes("地球是我们共同的家园，需要大家珍惜.");

		//下载文件
		// 这里没法直接测试，直接写到这里，方便调用；
		//noinspection ConstantConditions
		if (response != null) {
			final String fileName = "签名文件.pdf";
			final String contentType = "application/pdf";// application/octet-stream、image/jpeg、image/gif
			response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // 必须设置否则乱码; 但是 safari乱码
			JakartaServletUtil.write(response, new ByteArrayInputStream(bytes), contentType, fileName);
		}
	}
}
