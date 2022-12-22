package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

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

		Assert.assertTrue(PhoneUtil.isMobile(mobile));
		Assert.assertTrue(PhoneUtil.isTel(tel));
		Assert.assertTrue(PhoneUtil.isPhone(mobile));
		Assert.assertTrue(PhoneUtil.isPhone(tel));

		Assert.assertFalse(PhoneUtil.isMobile(errMobile));
		Assert.assertFalse(PhoneUtil.isTel(errTel));
		Assert.assertFalse(PhoneUtil.isPhone(errMobile));
		Assert.assertFalse(PhoneUtil.isPhone(errTel));
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
			Assert.assertTrue(PhoneUtil.isTel(s));
		}
		for (final String s : errTels) {
			Assert.assertFalse(PhoneUtil.isTel(s));
		}
	}

	@Test
	public void testHide() {
		final String mobile = "13612345678";

		Assert.assertEquals("*******5678", PhoneUtil.hideBefore(mobile));
		Assert.assertEquals("136****5678", PhoneUtil.hideBetween(mobile));
		Assert.assertEquals("1361234****", PhoneUtil.hideAfter(mobile));
	}

	@Test
	public void testSubString() {
		final String mobile = "13612345678";
		Assert.assertEquals("136", PhoneUtil.subBefore(mobile));
		Assert.assertEquals("1234", PhoneUtil.subBetween(mobile));
		Assert.assertEquals("5678", PhoneUtil.subAfter(mobile));
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
			Assert.assertTrue(PhoneUtil.isTel(s));
		}
		for (final String s : errTels) {
			Assert.assertFalse(PhoneUtil.isTel(s));
		}
		Assert.assertEquals("010", PhoneUtil.subTelBefore("010-12345678"));
		Assert.assertEquals("010", PhoneUtil.subTelBefore("01012345678"));
		Assert.assertEquals("12345678", PhoneUtil.subTelAfter("010-12345678"));
		Assert.assertEquals("12345678", PhoneUtil.subTelAfter("01012345678"));

		Assert.assertEquals("0755", PhoneUtil.subTelBefore("0755-7654321"));
		Assert.assertEquals("0755", PhoneUtil.subTelBefore("07557654321"));
		Assert.assertEquals("7654321", PhoneUtil.subTelAfter("0755-7654321"));
		Assert.assertEquals("7654321", PhoneUtil.subTelAfter("07557654321"));
	}
}
