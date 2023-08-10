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

package cn.hutool.extra.qrcode;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Color;
import java.io.File;

public class IssueI7RUIVTest {

	@Test
	@Ignore
	public void generateTest() {
		final QrConfig config = new QrConfig(300, 300);

		// 设置前景色，既二维码颜色（青色）
		config.setForeColor(Color.CYAN);
		// 设置背景色（灰色）
		config.setBackColor(Color.GRAY);

		// 生成二维码到文件，也可以到流
		final File file = QrCodeUtil.generate("https://hutool.cn/", config, FileUtil.file("d:/test/qrcode.jpg"));

		// 识别二维码
		// decode -> "http://hutool.cn/"
		final String decode = QrCodeUtil.decode(ImgUtil.read("d:/test/qrcode.jpg"), true, true);
		Console.log("decode info = " + decode);
	}
}
