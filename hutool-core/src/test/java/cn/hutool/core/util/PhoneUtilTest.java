package cn.hutool.core.util;

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
		String mobile = "13612345678";
		String tel = "010-88993108";
		String errMobile = "136123456781";
		String errTel = "010-889931081";

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
		ArrayList<String> tels = new ArrayList<>();
		tels.add("010-12345678");
		tels.add("020-9999999");
		tels.add("0755-7654321");
		ArrayList<String> errTels = new ArrayList<>();
		errTels.add("010 12345678");
		errTels.add("A20-9999999");
		errTels.add("0755-7654.321");
		errTels.add("13619887123");
		for (String s : tels) {
			Assertions.assertTrue(PhoneUtil.isTel(s));
		}
		for (String s : errTels) {
			Assertions.assertFalse(PhoneUtil.isTel(s));
		}
	}

	@Test
	public void testHide() {
		String mobile = "13612345678";

		Assertions.assertEquals("*******5678", PhoneUtil.hideBefore(mobile));
		Assertions.assertEquals("136****5678", PhoneUtil.hideBetween(mobile));
		Assertions.assertEquals("1361234****", PhoneUtil.hideAfter(mobile));
	}

	@Test
	public void testSubString() {
		String mobile = "13612345678";
		Assertions.assertEquals("136", PhoneUtil.subBefore(mobile));
		Assertions.assertEquals("1234", PhoneUtil.subBetween(mobile));
		Assertions.assertEquals("5678", PhoneUtil.subAfter(mobile));
	}

	@Test
	public void testNewTel() {
		ArrayList<String> tels = new ArrayList<>();
		tels.add("010-12345678");
		tels.add("01012345678");
		tels.add("020-9999999");
		tels.add("0209999999");
		tels.add("0755-7654321");
		tels.add("07557654321");
		ArrayList<String> errTels = new ArrayList<>();
		errTels.add("010 12345678");
		errTels.add("A20-9999999");
		errTels.add("0755-7654.321");
		errTels.add("13619887123");
		for (String s : tels) {
			Assertions.assertTrue(PhoneUtil.isTel(s));
		}
		for (String s : errTels) {
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
}
