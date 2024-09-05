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

package org.dromara.hutool.extra.qrcode;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.dromara.hutool.core.io.file.FileUtil.file;

public class Discussions3030Test {
	@Test
	@Disabled
	public void name() {
		//扫描二维码后   对应的链接正常
		String path = "https://juejin.cn/backend?name=%E5%BC%A0%E7%8F%8A&school=%E5%8E%A6%E9%97%A8%E5%A4%A7%E5%AD%A6";
		QrCodeUtil.generate(path, QrConfig.of(), file("d:/test/3030.png"));
	}
}
