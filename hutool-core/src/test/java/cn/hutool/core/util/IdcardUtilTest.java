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
		boolean valid = IdcardUtil.isValidCard(ID_18);
		Assert.assertTrue(valid);

		boolean valid15 = IdcardUtil.isValidCard(ID_15);
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
		String convert15To18 = IdcardUtil.convert15To18(ID_15);
		Assert.assertEquals("150102198807303035", convert15To18);

		String convert15To18Second = IdcardUtil.convert15To18("330102200403064");
		Assert.assertEquals("33010219200403064X", convert15To18Second);
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

	@Test
	public void getCityCodeByIdCardTest() {
		String codeByIdCard = IdcardUtil.getCityCodeByIdCard(ID_18);
		Assert.assertEquals("32108", codeByIdCard);
	}

	@Test
	public void getGenderByIdCardTest() {
		int gender = IdcardUtil.getGenderByIdCard(ID_18);
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
	}

	@Test
	public void isValidHKCardIdTest(){
		String hkCard="P174468(6)";
		boolean flag=IdcardUtil.isValidHKCard(hkCard);
		Assert.assertTrue(flag);
	}

	@Test
	public void isValidTWCardIdTest() {
		String twCard = "B221690311";
		boolean flag = IdcardUtil.isValidTWCard(twCard);
		Assert.assertTrue(flag);
		String errTwCard1 = "M517086311";
		flag = IdcardUtil.isValidTWCard(errTwCard1);
		Assert.assertFalse(flag);
		String errTwCard2 = "B2216903112";
		flag = IdcardUtil.isValidTWCard(errTwCard2);
		Assert.assertFalse(flag);
	}
}
