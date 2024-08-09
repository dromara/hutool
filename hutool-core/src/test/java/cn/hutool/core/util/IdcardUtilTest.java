package cn.hutool.core.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 身份证单元测试
 *
 * @author Looly
 *
 */
public class IdcardUtilTest {

	private static final String ID_18 = "321083197812162119";
	/**
	 * 新版外国人永久居留身份证号码
	 */
	private static final String FOREIGN_ID_18 = "932682198501010017";
	private static final String ID_15 = "150102880730303";

	@Test
	public void isValidCardTest() {
		boolean valid = IdcardUtil.isValidCard(ID_18);
		assertTrue(valid);

		boolean valid15 = IdcardUtil.isValidCard(ID_15);
		assertTrue(valid15);

		assertTrue(IdcardUtil.isValidCard(FOREIGN_ID_18));

		// 无效
		String idCard = "360198910283844";
		assertFalse(IdcardUtil.isValidCard(idCard));

		// 生日无效
		idCard = "201511221897205960";
		assertFalse(IdcardUtil.isValidCard(idCard));

		// 生日无效
		idCard = "815727834224151";
		assertFalse(IdcardUtil.isValidCard(idCard));
	}

	@Test
	public void convert15To18Test() {
		String convert15To18 = IdcardUtil.convert15To18(ID_15);
		assertEquals("150102198807303035", convert15To18);

		String convert15To18Second = IdcardUtil.convert15To18("330102200403064");
		assertEquals("33010219200403064X", convert15To18Second);
	}

	@Test
	public void convert18To15Test() {
		String idcard15 = IdcardUtil.convert18To15("150102198807303035");
		assertEquals(ID_15, idcard15);
	}

	@Test
	public void getAgeByIdCardTest() {
		DateTime date = DateUtil.parse("2017-04-10");

		int age = IdcardUtil.getAgeByIdCard(ID_18, date);
		assertEquals(age, 38);
		assertEquals(IdcardUtil.getAgeByIdCard(FOREIGN_ID_18, date), 32);

		int age2 = IdcardUtil.getAgeByIdCard(ID_15, date);
		assertEquals(age2, 28);
	}

	@Test
	public void issue3651Test() {
		DateTime date = DateUtil.parse("2014-07-11");
		int age = IdcardUtil.getAgeByIdCard("321083200807112111", date);
		assertEquals(5, age);

		date = DateUtil.parse("2014-07-31");
		age = IdcardUtil.getAgeByIdCard("321083200807312113", date);
		assertEquals(5, age);
	}

	@Test
	public void getBirthByIdCardTest() {
		String birth = IdcardUtil.getBirthByIdCard(ID_18);
		assertEquals(birth, "19781216");

		String birth2 = IdcardUtil.getBirthByIdCard(ID_15);
		assertEquals(birth2, "19880730");
	}

	@Test
	public void getProvinceByIdCardTest() {
		String province = IdcardUtil.getProvinceByIdCard(ID_18);
		assertEquals(province, "江苏");

		String province2 = IdcardUtil.getProvinceByIdCard(ID_15);
		assertEquals(province2, "内蒙古");
	}

	@Test
	public void getCityCodeByIdCardTest() {
		String codeByIdCard = IdcardUtil.getCityCodeByIdCard(ID_18);
		assertEquals("3210", codeByIdCard);
	}

	@Test
	public void getDistrictCodeByIdCardTest() {
		String codeByIdCard = IdcardUtil.getDistrictCodeByIdCard(ID_18);
		assertEquals("321083", codeByIdCard);
	}

	@Test
	public void getGenderByIdCardTest() {
		int gender = IdcardUtil.getGenderByIdCard(ID_18);
		assertEquals(1, gender);
	}

	@Test
	public void isValidCard18Test(){
		boolean isValidCard18 = IdcardUtil.isValidCard18("3301022011022000D6");
		assertFalse(isValidCard18);

		// 不忽略大小写情况下，X严格校验必须大写
		isValidCard18 = IdcardUtil.isValidCard18("33010219200403064x", false);
		assertFalse(isValidCard18);
		isValidCard18 = IdcardUtil.isValidCard18("33010219200403064X", false);
		assertTrue(isValidCard18);

		// 非严格校验下大小写皆可
		isValidCard18 = IdcardUtil.isValidCard18("33010219200403064x");
		assertTrue(isValidCard18);
		isValidCard18 = IdcardUtil.isValidCard18("33010219200403064X");
		assertTrue(isValidCard18);

		// 香港人在大陆身份证
		isValidCard18 = IdcardUtil.isValidCard18("81000019980902013X");
		assertTrue(isValidCard18);

		// 澳门人在大陆身份证
		isValidCard18 = IdcardUtil.isValidCard18("820000200009100032");
		assertTrue(isValidCard18);

		// 台湾人在大陆身份证
		isValidCard18 = IdcardUtil.isValidCard18("830000200209060065");
		assertTrue(isValidCard18);

		// 新版外国人永久居留身份证
		isValidCard18 = IdcardUtil.isValidCard18("932682198501010017");
		assertTrue(isValidCard18);
	}

	@Test
	public void isValidHKCardIdTest(){
		String hkCard="P174468(6)";
		boolean flag=IdcardUtil.isValidHKCard(hkCard);
		assertTrue(flag);
	}

	@Test
	public void isValidTWCardIdTest() {
		String twCard = "B221690311";
		boolean flag = IdcardUtil.isValidTWCard(twCard);
		assertTrue(flag);
		String errTwCard1 = "M517086311";
		flag = IdcardUtil.isValidTWCard(errTwCard1);
		assertFalse(flag);
		String errTwCard2 = "B2216903112";
		flag = IdcardUtil.isValidTWCard(errTwCard2);
		assertFalse(flag);
	}

	@Test
	public void issueI88YKMTest() {
		assertTrue(IdcardUtil.isValidCard("111111111111111"));
	}

	@Test
	public void issueIAFOLITest() {
		String idcard = "H01487002";
		assertFalse(IdcardUtil.isValidHKCard(idcard));
		assertNull(IdcardUtil.isValidCard10(idcard));
		assertFalse(IdcardUtil.isValidCard(idcard));
	}
}
