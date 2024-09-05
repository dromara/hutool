/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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
		Assertions.assertEquals("测试.xlsx", fileNameFromDisposition);
	}

	@Test
	void getFileNameFromDispositionTest2() {
		final Map<String, List<String>> headers = new HashMap<>();
		headers.put(HeaderName.CONTENT_DISPOSITION.getValue(),
			ListUtil.of("attachment; filename*=utf-8''%E6%B5%8B%E8%AF%95.xlsx"));
		final String fileNameFromDisposition = HttpHeaderUtil.getFileNameFromDisposition(headers, null);
		Assertions.assertEquals("测试.xlsx", fileNameFromDisposition);
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
