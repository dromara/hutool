package cn.hutool.core.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

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
		final boolean valid = IdcardUtil.isValidCard(ID_18);
		Assert.assertTrue(valid);

		final boolean valid15 = IdcardUtil.isValidCard(ID_15);
		Assert.assertTrue(valid15);

		// 无效
		String idCard = "360198910283844";
		Assert.assertFalse(IdcardUtil.isValidCard(idCard));

		// 生日无效
		idCard = "201511221897205960";
		Assert.assertFalse(IdcardUtil.isValidCard(idCard));

		// 生日无效
		idCard = "815727834224151";
		Assert.assertFalse(IdcardUtil.isValidCard(idCard));
	}

	@Test
	public void convert15To18Test() {
		final String convert15To18 = IdcardUtil.convert15To18(ID_15);
		Assert.assertEquals("150102198807303035", convert15To18);

		final String convert15To18Second = IdcardUtil.convert15To18("330102200403064");
		Assert.assertEquals("33010219200403064X", convert15To18Second);
	}

	@Test
	public void convert18To15Test() {
		String idcard15 = IdcardUtil.convert18To15("150102198807303035");
		Assert.assertEquals(ID_15, idcard15);
	}

	@Test
	public void getAgeTest() {
		final DateTime date = DateUtil.parse("2017-04-10");

		final int age = IdcardUtil.getAge(ID_18, date);
		Assert.assertEquals(age, 38);

		final int age2 = IdcardUtil.getAge(ID_15, date);
		Assert.assertEquals(age2, 28);
	}

	@Test
	public void getBirthTest() {
		final String birth = IdcardUtil.getBirth(ID_18);
		Assert.assertEquals(birth, "19781216");

		final String birth2 = IdcardUtil.getBirth(ID_15);
		Assert.assertEquals(birth2, "19880730");
	}

	@Test
	public void getProvinceTest() {
		final String province = IdcardUtil.getProvince(ID_18);
		Assert.assertEquals(province, "江苏");

		final String province2 = IdcardUtil.getProvince(ID_15);
		Assert.assertEquals(province2, "内蒙古");
	}

	@Test
	public void getCityCodeTest() {
		final String code = IdcardUtil.getCityCode(ID_18);
		Assert.assertEquals("3210", code);
	}

	@Test
	public void getDistrictCodeTest() {
		final String code = IdcardUtil.getDistrictCode(ID_18);
		Assert.assertEquals("321083", code);
	}

	@Test
	public void getGenderTest() {
		final int gender = IdcardUtil.getGender(ID_18);
		Assert.assertEquals(1, gender);
	}

	@Test
	public void isValidCard18Test(){
		boolean isValidCard18 = IdcardUtil.isValidCard18("3301022011022000D6");
		Assert.assertFalse(isValidCard18);

		// 不忽略大小写情况下，X严格校验必须大写
		isValidCard18 = IdcardUtil.isValidCard18("33010219200403064x", false);
		Assert.assertFalse(isValidCard18);
		isValidCard18 = IdcardUtil.isValidCard18("33010219200403064X", false);
		Assert.assertTrue(isValidCard18);

		// 非严格校验下大小写皆可
		isValidCard18 = IdcardUtil.isValidCard18("33010219200403064x");
		Assert.assertTrue(isValidCard18);
		isValidCard18 = IdcardUtil.isValidCard18("33010219200403064X");
		Assert.assertTrue(isValidCard18);

		// 香港人在大陆身份证
		isValidCard18 = IdcardUtil.isValidCard18("81000019980902013X");
		Assert.assertTrue(isValidCard18);

		// 澳门人在大陆身份证
		isValidCard18 = IdcardUtil.isValidCard18("820000200009100032");
		Assert.assertTrue(isValidCard18);

		// 台湾人在大陆身份证
		isValidCard18 = IdcardUtil.isValidCard18("830000200209060065");
		Assert.assertTrue(isValidCard18);
	}

	@Test
	public void isValidHKCardIdTest(){
		final String hkCard="P174468(6)";
		final boolean flag=IdcardUtil.isValidHKCard(hkCard);
		Assert.assertTrue(flag);
	}

	@Test
	public void isValidTWCardIdTest() {
		final String twCard = "B221690311";
		boolean flag = IdcardUtil.isValidTWCard(twCard);
		Assert.assertTrue(flag);
		final String errTwCard1 = "M517086311";
		flag = IdcardUtil.isValidTWCard(errTwCard1);
		Assert.assertFalse(flag);
		final String errTwCard2 = "B2216903112";
		flag = IdcardUtil.isValidTWCard(errTwCard2);
		Assert.assertFalse(flag);
	}
}
