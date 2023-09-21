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

package org.dromara.hutool.http.client.body;

import org.dromara.hutool.core.io.resource.HttpResource;
import org.dromara.hutool.core.io.resource.StringResource;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MultipartBodyTest {

	@Test
	public void buildTest(){
		final Map<String, Object> form = new HashMap<>();
		form.put("pic1", "pic1 content");
		form.put("pic2", new HttpResource(
				new StringResource("pic2 content"), "text/plain"));
		form.put("pic3", new HttpResource(
				new StringResource("pic3 content", "pic3.jpg"), "image/jpeg"));

		final MultipartBody body = MultipartBody.of(form, CharsetUtil.UTF_8);

		Assertions.assertNotNull(body.toString());
//		Console.log(body);
	}
}
