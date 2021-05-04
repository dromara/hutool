package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;


public class RuleRandomUtilTest {
	@Test
	public void testCheckParam() {
		// RuleRandomUtil.checkSingleRuleParam()
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("["));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[]a"));
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[UUID]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[Date:yyyymmdd]"));
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[Date|yyyymmdd]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[UUID:1,123]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[asdasfafawf]"));

		// checkMultiRuleParam
		Assert.assertNotNull(RuleRandomUtil.getMultiRuleRandom("[UUID]  [Date]  [Time]  [Datetime]  [RegionCN]   [ProvinceCN]  [CityCN]  [CountyCN]  [PostalCodeCN]  [FirstNameEN]  [LastNameEN]  [NameEN] asdad [FirstNameCN]  [LastNameCN]  [NameCN]"));
	}

	@Test
	public void testGetSingleRuleRandom() {
		// UUID
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[UUID]"));
		// Date
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[Date]"));
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[Date|yymmdd]"));
		Assert.assertTrue(RuleRandomUtil.getSingleRuleRandom("[Date]").length() == 10);
		Assert.assertTrue(RuleRandomUtil.getSingleRuleRandom("[Date|yyyy--mm---dd]").length() == 13);
		// Time
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[Time]"));
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[Time|hh:mm]"));
		Assert.assertTrue(RuleRandomUtil.getSingleRuleRandom("[Time|hh:mm]").length() == 5);
		Assert.assertTrue(RuleRandomUtil.getSingleRuleRandom("[Time]").length() == 8);
		Assert.assertTrue(RuleRandomUtil.getSingleRuleRandom("[Time|hh::mm:::ss]").length() == 11);
		// Datetime
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[Datetime]"));
		Assert.assertTrue(RuleRandomUtil.getSingleRuleRandom("[Datetime]").length() == 19);
		Assert.assertTrue(RuleRandomUtil.getSingleRuleRandom("[Datetime|yyyy-dd--mm    hh::mm:::ss]").length() == 26);
		// RegionCN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[RegionCN]"));
		Assert.assertTrue(RuleRandomUtil.getSingleRuleRandom("[RegionCN]").length() == 2);
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[RegionCN|asd]"));
		// ProvinceCN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[ProvinceCN]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[ProvinceCN|asd]"));
		// CityCN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[CityCN]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[CityCN|ad]"));
		// CountyCN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[CountyCN]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[CountyCN|ad]"));
		// PostalCodeCN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[PostalCodeCN]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[PostalCodeCN|ad]"));
		// FirstNameEN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[FirstNameEN]"));
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[FirstNameEN|male]"));
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[FirstNameEN|female]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[FirstNameEN|ad]"));
		// LastNameEN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[LastNameEN]"));
		// NameEN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[NameEN]"));
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[NameEN|male]"));
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[NameEN|female]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[NameEN|ad]"));
		// FirstNameCN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[FirstNameCN]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[FirstNameCN|male]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[FirstNameCN|female]"));
		// LastNameCN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[LastNameCN]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[LastNameCN|male]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[LastNameCN|female]"));
		// NameCN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[NameCN]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[NameCN|male]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[NameCN|female]"));
		// ColorCN
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[ColorCN]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[ColorCN|male]"));
		// ColorEn
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[ColorEn]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[ColorEn|male]"));
		// IP
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[IP]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[IP|male]"));
		// Port
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[Port]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[Port|male]"));
		// NetAddr
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[NetAddr]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[NetAddr|male]"));
		// Domain
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[Domain]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[Domain|male]"));
		// URL
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[URL]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[URL|male]"));
		// EmailAddr
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[EmailAddr]"));
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("[EmailAddr|male]"));
	}

	@Test
	public void testGetMultiRuleRandom() {
		Assert.assertNotNull(RuleRandomUtil.getMultiRuleRandom("[UUID]  [Date]  [Time]  [Datetime]  [RegionCN]   [ProvinceCN]  [CityCN]  [CountyCN]  [PostalCodeCN]  [FirstNameEN]  [LastNameEN]  [NameEN] asdad [FirstNameCN]  [LastNameCN]  [NameCN]"));
	}

	@Test
	public void testAddRuleMethod() {
		Assert.assertThrows(IllegalArgumentException.class, () -> RuleRandomUtil.getSingleRuleRandom("TestAddRuleMethod"));
		RuleRandomUtil.addRuleMethod("TestAddRuleMethod", (paramList) -> RandomUtil.randomBoolean() ? "a" : "b");
		Assert.assertNotNull(RuleRandomUtil.getSingleRuleRandom("[TestAddRuleMethod]"));
	}
}
