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

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.text.NamingCase;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Issue2202Test {

	/**
	 * https://github.com/dromara/hutool/issues/2202
	 */
	@Test
	public void mapToBeanWithFieldNameEditorTest(){
		final Map<String, String> headerMap = new HashMap<>(5);
		headerMap.put("wechatpay-serial", "serial");
		headerMap.put("wechatpay-nonce", "nonce");
		headerMap.put("wechatpay-timestamp", "timestamp");
		headerMap.put("wechatpay-signature", "signature");
		final ResponseSignVerifyParams case1 = BeanUtil.toBean(headerMap, ResponseSignVerifyParams.class,
				CopyOptions.of().setFieldEditor(entry -> {
					entry.setKey(NamingCase.toCamelCase(entry.getKey().toString(), '-'));
					return entry;
				}));

		Assertions.assertEquals("serial", case1.getWechatpaySerial());
		Assertions.assertEquals("nonce", case1.getWechatpayNonce());
		Assertions.assertEquals("timestamp", case1.getWechatpayTimestamp());
		Assertions.assertEquals("signature", case1.getWechatpaySignature());
	}

	@Data
	static class ResponseSignVerifyParams {
		private String wechatpaySerial;
		private String wechatpaySignature;
		private String wechatpayTimestamp;
		private String wechatpayNonce;
		private String body;
	}
}
