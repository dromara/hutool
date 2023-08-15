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
