package org.dromara.hutool.http.meta;

import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHeaderUtilTest {
	@Test
	void getFileNameFromDispositionTest() {
		final Map<String, List<String>> headers = new HashMap<>();
		headers.put(HeaderName.CONTENT_DISPOSITION.getValue(),
			ListUtil.of("attachment; filename*=utf-8''%E6%B5%8B%E8%AF%95.xlsx; filename=\"æµ\u008Bè¯\u0095.xlsx\""));
		final String fileNameFromDisposition = HttpHeaderUtil.getFileNameFromDisposition(headers, null);
		Assertions.assertEquals("%E6%B5%8B%E8%AF%95.xlsx", fileNameFromDisposition);
	}

	@Test
	void getFileNameFromDispositionTest2() {
		final Map<String, List<String>> headers = new HashMap<>();
		headers.put(HeaderName.CONTENT_DISPOSITION.getValue(),
			ListUtil.of("attachment; filename*=utf-8''%E6%B5%8B%E8%AF%95.xlsx"));
		final String fileNameFromDisposition = HttpHeaderUtil.getFileNameFromDisposition(headers, null);
		Assertions.assertEquals("%E6%B5%8B%E8%AF%95.xlsx", fileNameFromDisposition);
	}

	@Test
	void getFileNameFromDispositionTest3() {
		final Map<String, List<String>> headers = new HashMap<>();
		headers.put(HeaderName.CONTENT_DISPOSITION.getValue(),
			ListUtil.of("attachment; filename*=\"%E6%B5%8B%E8%AF%95.xlsx\""));
		final String fileNameFromDisposition = HttpHeaderUtil.getFileNameFromDisposition(headers, null);
		Assertions.assertEquals("%E6%B5%8B%E8%AF%95.xlsx", fileNameFromDisposition);
	}

	@Test
	void getFileNameFromDispositionTest4() {
		final Map<String, List<String>> headers = new HashMap<>();
		headers.put(HeaderName.CONTENT_DISPOSITION.getValue(),
			ListUtil.of("attachment; filename=\"%E6%B5%8B%E8%AF%95.xlsx\""));
		final String fileNameFromDisposition = HttpHeaderUtil.getFileNameFromDisposition(headers, null);
		Assertions.assertEquals("%E6%B5%8B%E8%AF%95.xlsx", fileNameFromDisposition);
	}

	@Test
	void getFileNameFromDispositionTest5() {
		final Map<String, List<String>> headers = new HashMap<>();
		headers.put(HeaderName.CONTENT_DISPOSITION.getValue(),
			ListUtil.of("attachment; filename=%E6%B5%8B%E8%AF%95.xlsx"));
		final String fileNameFromDisposition = HttpHeaderUtil.getFileNameFromDisposition(headers, null);
		Assertions.assertEquals("%E6%B5%8B%E8%AF%95.xlsx", fileNameFromDisposition);
	}
}
