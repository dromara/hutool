package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * 身份证单元测试
 * 
 * @author Looly
 *
 */
public class IdcardUtilTest {
	private static final String ID_18 = "321083197812162119";
	private static final String ID_15 = "150102880730303";

	@Test
	public void isValidCardTest() {
		boolean valid = IdcardUtil.isValidCard(ID_18);
		Assert.assertTrue(valid);

		boolean valid15 = IdcardUtil.isValidCard(ID_15);
		Assert.assertTrue(valid15);
	}

	@Test
	public void convert15To18Test() {
		String convert15To18 = IdcardUtil.convert15To18(ID_15);
		Assert.assertEquals("150102198807303035", convert15To18);

		String convert15To18Second = IdcardUtil.convert15To18("330102200403064");
		Assert.assertEquals("33010219200403064x", convert15To18Second);
	}

	@Test
	public void getAgeByIdCardTest() {
		DateTime date = DateUtil.parse("2017-04-10");

		int age = IdcardUtil.getAgeByIdCard(ID_18, date);
		Assert.assertEquals(age, 38);

		int age2 = IdcardUtil.getAgeByIdCard(ID_15, date);
		Assert.assertEquals(age2, 28);
	}

	@Test
	public void getBirthByIdCardTest() {
		String birth = IdcardUtil.getBirthByIdCard(ID_18);
		Assert.assertEquals(birth, "19781216");

		String birth2 = IdcardUtil.getBirthByIdCard(ID_15);
		Assert.assertEquals(birth2, "19880730");
	}

	@Test
	public void getProvinceByIdCardTest() {
		String province = IdcardUtil.getProvinceByIdCard(ID_18);
		Assert.assertEquals(province, "江苏");

		String province2 = IdcardUtil.getProvinceByIdCard(ID_15);
		Assert.assertEquals(province2, "内蒙古");
	}

}
