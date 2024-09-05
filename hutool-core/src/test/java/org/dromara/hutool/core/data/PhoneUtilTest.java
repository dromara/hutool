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

package org.dromara.hutool.core.data;

import org.dromara.hutool.core.data.PhoneUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * {@link PhoneUtil} 单元测试类
 *
 * @author dahuoyzs
 */
public class PhoneUtilTest {

	@Test
	public void testCheck() {
		final String mobile = "13612345678";
		final String tel = "010-88993108";
		final String errMobile = "136123456781";
		final String errTel = "010-889931081";

		Assertions.assertTrue(PhoneUtil.isMobile(mobile));
		Assertions.assertTrue(PhoneUtil.isTel(tel));
		Assertions.assertTrue(PhoneUtil.isPhone(mobile));
		Assertions.assertTrue(PhoneUtil.isPhone(tel));

		Assertions.assertFalse(PhoneUtil.isMobile(errMobile));
		Assertions.assertFalse(PhoneUtil.isTel(errTel));
		Assertions.assertFalse(PhoneUtil.isPhone(errMobile));
		Assertions.assertFalse(PhoneUtil.isPhone(errTel));
	}

	@Test
	public void testTel() {
		final ArrayList<String> tels = new ArrayList<>();
		tels.add("010-12345678");
		tels.add("020-9999999");
		tels.add("0755-7654321");
		final ArrayList<String> errTels = new ArrayList<>();
		errTels.add("010 12345678");
		errTels.add("A20-9999999");
		errTels.add("0755-7654.321");
		errTels.add("13619887123");
		for (final String s : tels) {
			Assertions.assertTrue(PhoneUtil.isTel(s));
		}
		for (final String s : errTels) {
			Assertions.assertFalse(PhoneUtil.isTel(s));
		}
	}

	@Test
	public void testHide() {
		final String mobile = "13612345678";

		Assertions.assertEquals("*******5678", PhoneUtil.hideBefore(mobile));
		Assertions.assertEquals("136****5678", PhoneUtil.hideBetween(mobile));
		Assertions.assertEquals("1361234****", PhoneUtil.hideAfter(mobile));
	}

	@Test
	public void testSubString() {
		final String mobile = "13612345678";
		Assertions.assertEquals("136", PhoneUtil.subBefore(mobile));
		Assertions.assertEquals("1234", PhoneUtil.subBetween(mobile));
		Assertions.assertEquals("5678", PhoneUtil.subAfter(mobile));
	}

	@Test
	public void testNewTel() {
		final ArrayList<String> tels = new ArrayList<>();
		tels.add("010-12345678");
		tels.add("01012345678");
		tels.add("020-9999999");
		tels.add("0209999999");
		tels.add("0755-7654321");
		tels.add("07557654321");
		final ArrayList<String> errTels = new ArrayList<>();
		errTels.add("010 12345678");
		errTels.add("A20-9999999");
		errTels.add("0755-7654.321");
		errTels.add("13619887123");
		for (final String s : tels) {
			Assertions.assertTrue(PhoneUtil.isTel(s));
		}
		for (final String s : errTels) {
			Assertions.assertFalse(PhoneUtil.isTel(s));
		}
		Assertions.assertEquals("010", PhoneUtil.subTelBefore("010-12345678"));
		Assertions.assertEquals("010", PhoneUtil.subTelBefore("01012345678"));
		Assertions.assertEquals("12345678", PhoneUtil.subTelAfter("010-12345678"));
		Assertions.assertEquals("12345678", PhoneUtil.subTelAfter("01012345678"));

		Assertions.assertEquals("0755", PhoneUtil.subTelBefore("0755-7654321"));
		Assertions.assertEquals("0755", PhoneUtil.subTelBefore("07557654321"));
		Assertions.assertEquals("7654321", PhoneUtil.subTelAfter("0755-7654321"));
		Assertions.assertEquals("7654321", PhoneUtil.subTelAfter("07557654321"));
	}

	@Test
	public void isTel400800Test() {
		boolean tel400800 = PhoneUtil.isTel400800("400-860-8608");//800-830-3811
		Assertions.assertTrue(tel400800);

		tel400800 = PhoneUtil.isTel400800("400-8608608");//800-830-3811
		Assertions.assertTrue(tel400800);
	}
}
