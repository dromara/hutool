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

package org.dromara.hutool.swing.img;

import org.dromara.hutool.swing.img.color.ColorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Color;

public class ColorUtilTest {

	@Test
	public void getColorTest(){
		final Color blue = ColorUtil.getColor("blue");
		Assertions.assertEquals(Color.BLUE, blue);
	}

	@Test
	public void toCssRgbTest(){
		final String s = ColorUtil.toCssRgb(Color.BLUE);
		Assertions.assertEquals("rgb(0,0,255)", s);
	}

	@Test
	public void toCssRgbaTest(){
		final String s = ColorUtil.toCssRgba(Color.BLUE);
		Assertions.assertEquals("rgba(0,0,255,1.0)", s);
	}
}
