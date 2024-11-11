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

import org.dromara.hutool.core.data.masking.MaskingManager;
import org.dromara.hutool.core.data.masking.MaskingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 脱敏工具类 MaskingUtil 安全测试
 *
 * @author dazer and nuesoft
 * @see MaskingUtil
 */
public class MaskingUtilTest {

	@Test
	public void maskingTest() {
		assertEquals("", MaskingUtil.masking(MaskingType.CLEAR_TO_EMPTY, "100"));
		Assertions.assertNull(MaskingUtil.masking(MaskingType.CLEAR_TO_NULL, "100"));

		assertEquals("0", MaskingUtil.masking(MaskingType.USER_ID, "100"));
		assertEquals("段**", MaskingUtil.masking(MaskingType.CHINESE_NAME, "段正淳"));
		assertEquals("5***************1X", MaskingUtil.masking(MaskingType.ID_CARD, "51343620000320711X"));
		assertEquals("0915*****79", MaskingUtil.masking(MaskingType.FIXED_PHONE, "09157518479"));
		assertEquals("180****1999", MaskingUtil.masking(MaskingType.MOBILE_PHONE, "18049531999"));
		assertEquals("北京市海淀区马********", MaskingUtil.masking(MaskingType.ADDRESS, "北京市海淀区马连洼街道289号"));
		assertEquals("d*************@gmail.com.cn", MaskingUtil.masking(MaskingType.EMAIL, "duandazhi-jack@gmail.com.cn"));
		assertEquals("**********", MaskingUtil.masking(MaskingType.PASSWORD, "1234567890"));
		assertEquals("**********", MaskingUtil.masking(MaskingType.PASSWORD, "123"));
		assertEquals("1101 **** **** **** 3256", MaskingUtil.masking(MaskingType.BANK_CARD, "11011111222233333256"));
		assertEquals("6227 **** **** **** 123", MaskingUtil.masking(MaskingType.BANK_CARD, "6227880100100105123"));
		assertEquals("192.*.*.*", MaskingUtil.masking(MaskingType.IPV4, "192.168.1.1"));
		assertEquals("2001:*:*:*:*:*:*:*", MaskingUtil.masking(MaskingType.IPV6, "2001:0db8:86a3:08d3:1319:8a2e:0370:7344"));
	}

	@Test
	public void maskingWithMaskCharTest() {
		final MaskingManager manager = MaskingManager.ofDefault('#');

		assertEquals("", manager.masking(MaskingType.CLEAR_TO_EMPTY.name(), "100"));
		Assertions.assertNull(manager.masking(MaskingType.CLEAR_TO_NULL.name(), "100"));

		assertEquals("0", manager.masking(MaskingType.USER_ID.name(), "100"));
		assertEquals("段##", manager.masking(MaskingType.CHINESE_NAME.name(), "段正淳"));
		assertEquals("5###############1X", manager.masking(MaskingType.ID_CARD.name(), "51343620000320711X"));
		assertEquals("0915#####79", manager.masking(MaskingType.FIXED_PHONE.name(), "09157518479"));
		assertEquals("180####1999", manager.masking(MaskingType.MOBILE_PHONE.name(), "18049531999"));
		assertEquals("北京市海淀区马########", manager.masking(MaskingType.ADDRESS.name(), "北京市海淀区马连洼街道289号"));
		assertEquals("d#############@gmail.com.cn", manager.masking(MaskingType.EMAIL.name(), "duandazhi-jack@gmail.com.cn"));
		assertEquals("##########", manager.masking(MaskingType.PASSWORD.name(), "1234567890"));
		assertEquals("##########", manager.masking(MaskingType.PASSWORD.name(), "123"));
		assertEquals("1101 #### #### #### 3256", manager.masking(MaskingType.BANK_CARD.name(), "11011111222233333256"));
		assertEquals("6227 #### #### #### 123", manager.masking(MaskingType.BANK_CARD.name(), "6227880100100105123"));
		assertEquals("192.#.#.#", manager.masking(MaskingType.IPV4.name(), "192.168.1.1"));
		assertEquals("2001:#:#:#:#:#:#:#", manager.masking(MaskingType.IPV6.name(), "2001:0db8:86a3:08d3:1319:8a2e:0370:7344"));
	}

	@Test
	public void userIdTest() {
		assertEquals(Long.valueOf(0L), MaskingUtil.userId());
	}

	@Test
	public void chineseNameTest() {
		assertEquals("段**", MaskingUtil.chineseName("段正淳"));
	}

	@Test
	public void idCardNumTest() {
		assertEquals("5***************1X", MaskingUtil.idCardNum("51343620000320711X", 1, 2));
	}

	@Test
	public void fixedPhoneTest() {
		assertEquals("0915*****79", MaskingUtil.fixedPhone("09157518479"));
	}

	@Test
	public void mobilePhoneTest() {
		assertEquals("180****1999", MaskingUtil.mobilePhone("18049531999"));
	}

	@Test
	public void addressTest() {
		assertEquals("北京市海淀区马连洼街*****", MaskingUtil.address("北京市海淀区马连洼街道289号", 5));
		assertEquals("***************", MaskingUtil.address("北京市海淀区马连洼街道289号", 50));
		assertEquals("北京市海淀区马连洼街道289号", MaskingUtil.address("北京市海淀区马连洼街道289号", 0));
		assertEquals("北京市海淀区马连洼街道289号", MaskingUtil.address("北京市海淀区马连洼街道289号", -1));
	}

	@Test
	public void emailTest() {
		assertEquals("d********@126.com", MaskingUtil.email("duandazhi@126.com"));
		assertEquals("d********@gmail.com.cn", MaskingUtil.email("duandazhi@gmail.com.cn"));
		assertEquals("d*************@gmail.com.cn", MaskingUtil.email("duandazhi-jack@gmail.com.cn"));
	}

	@Test
	public void passwordTest() {
		assertEquals("**********", MaskingUtil.password("1234567890"));
	}

	@Test
	public void carLicenseTest() {
		assertEquals("", MaskingUtil.carLicense(null));
		assertEquals("", MaskingUtil.carLicense(""));
		assertEquals("苏D4***0", MaskingUtil.carLicense("苏D40000"));
		assertEquals("陕A1****D", MaskingUtil.carLicense("陕A12345D"));
		assertEquals("京A123", MaskingUtil.carLicense("京A123"));
	}

	@Test
	public void bankCardTest() {
		Assertions.assertNull(MaskingUtil.bankCard(null));
		assertEquals("", MaskingUtil.bankCard(""));
		assertEquals("1234 **** **** **** **** 9", MaskingUtil.bankCard("1234 2222 3333 4444 6789 9"));
		assertEquals("1234 **** **** **** **** 91", MaskingUtil.bankCard("1234 2222 3333 4444 6789 91"));
		assertEquals("1234 **** **** **** 6789", MaskingUtil.bankCard("1234 2222 3333 4444 6789"));
		assertEquals("1234 **** **** **** 678", MaskingUtil.bankCard("1234 2222 3333 4444 678"));
	}

	@Test
	void customTest() {
		final MaskingManager maskingManager = MaskingManager.ofDefault('#');
		maskingManager.register("custom", (str)->"custom" + str);

		final String masking = maskingManager.masking("custom", "hutool");
		Assertions.assertEquals("customhutool", masking);
	}
}
