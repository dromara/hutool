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
