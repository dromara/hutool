/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.qrcode;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

public class Issue3146Test {

	@Test
	@Disabled
	void generateTest() {
		QrCodeUtil.generate("https://www.baidu.com/h5/monitorfile/index.html?sadfsfasdfsafsafasfasfsafasfasdfsafdsafsfasfafsfaasfsdfsfsafasfa",
			QrConfig.of().setWidth(600).setHeight(600).setMargin(0), new File("d:/test/issue3146.jpg"));
	}
}
