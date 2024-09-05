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

import java.io.File;

public class IssuesI76SZBTest {
	@Test
	@Disabled
	void generateTest() {
		final QrConfig qrConfig = new QrConfig(300, 300);
		final File file = new File("d:/test/out.png");
		qrConfig.setImg(new File("d:/test/b2dd3614_868440.png"));
		QrCodeUtil.generate("111", qrConfig, file);
	}
}
