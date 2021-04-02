package cn.hutool.core.date.chinese;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class SolarTermsTest {


	@Test
	public void getTermTest() {
		Assert.assertEquals(SolarTerms.DONG_ZHI, SolarTerms.getTerm(2021, 1, 4));
		Assert.assertEquals(SolarTerms.XIAO_HAN, SolarTerms.getTerm(2021, 1, 5));
		Assert.assertEquals(SolarTerms.XIAO_HAN, SolarTerms.getTerm(2021, 1, 19));
		Assert.assertEquals(SolarTerms.DA_HAN, SolarTerms.getTerm(2021, 1, 20));
		Assert.assertEquals(SolarTerms.DA_HAN, SolarTerms.getTerm(2021, 2, 2));
		Assert.assertEquals(SolarTerms.LI_CHUN, SolarTerms.getTerm(2021, 2, 3));
		Assert.assertEquals(SolarTerms.LI_CHUN, SolarTerms.getTerm(2021, 2, 17));
		Assert.assertEquals(SolarTerms.YU_SHUI, SolarTerms.getTerm(2021, 2, 18));
		Assert.assertEquals(SolarTerms.YU_SHUI, SolarTerms.getTerm(2021, 3, 4));
		Assert.assertEquals(SolarTerms.JING_ZHE, SolarTerms.getTerm(2021, 3, 5));
		Assert.assertEquals(SolarTerms.JING_ZHE, SolarTerms.getTerm(2021, 3, 19));
		Assert.assertEquals(SolarTerms.CHUN_FEN, SolarTerms.getTerm(2021, 3, 20));
		Assert.assertEquals(SolarTerms.CHUN_FEN, SolarTerms.getTerm(2021, 4, 3));
		Assert.assertEquals(SolarTerms.QING_MING, SolarTerms.getTerm(2021, 4, 4));
		Assert.assertEquals(SolarTerms.QING_MING, SolarTerms.getTerm(2021, 4, 10));
		Assert.assertEquals(SolarTerms.QING_MING, SolarTerms.getTerm(2021, 4, 19));
		Assert.assertEquals(SolarTerms.GU_YU, SolarTerms.getTerm(2021, 4, 20));
		Assert.assertEquals(SolarTerms.GU_YU, SolarTerms.getTerm(2021, 4, 29));
		Assert.assertEquals(SolarTerms.GU_YU, SolarTerms.getTerm(2021, 5, 4));
		Assert.assertEquals(SolarTerms.LI_XIA, SolarTerms.getTerm(2021, 5, 5));
		Assert.assertEquals(SolarTerms.LI_XIA, SolarTerms.getTerm(2021, 5, 9));
		Assert.assertEquals(SolarTerms.LI_XIA, SolarTerms.getTerm(2021, 5, 20));
		Assert.assertEquals(SolarTerms.XIAO_MAN, SolarTerms.getTerm(2021, 5, 21));
		Assert.assertEquals(SolarTerms.XIAO_MAN, SolarTerms.getTerm(2021, 6, 4));
		Assert.assertEquals(SolarTerms.MANG_ZHONG, SolarTerms.getTerm(2021, 6, 5));
		Assert.assertEquals(SolarTerms.MANG_ZHONG, SolarTerms.getTerm(2021, 6, 20));
		Assert.assertEquals(SolarTerms.XIA_ZHI, SolarTerms.getTerm(2021, 6, 21));
		Assert.assertEquals(SolarTerms.XIA_ZHI, SolarTerms.getTerm(2021, 7, 6));
		Assert.assertEquals(SolarTerms.XIAO_SHU, SolarTerms.getTerm(2021, 7, 7));
		Assert.assertEquals(SolarTerms.XIAO_SHU, SolarTerms.getTerm(2021, 7, 21));
		Assert.assertEquals(SolarTerms.DA_SHU, SolarTerms.getTerm(2021, 7, 22));
		Assert.assertEquals(SolarTerms.DA_SHU, SolarTerms.getTerm(2021, 8, 6));
		Assert.assertEquals(SolarTerms.LI_QIU, SolarTerms.getTerm(2021, 8, 7));
		Assert.assertEquals(SolarTerms.CHU_SHU, SolarTerms.getTerm(2021, 8, 23));
		Assert.assertEquals(SolarTerms.CHU_SHU, SolarTerms.getTerm(2021, 9, 6));
		Assert.assertEquals(SolarTerms.BAI_LU, SolarTerms.getTerm(2021, 9, 7));
		Assert.assertEquals(SolarTerms.BAI_LU, SolarTerms.getTerm(2021, 9, 22));
		Assert.assertEquals(SolarTerms.QIU_FEN, SolarTerms.getTerm(2021, 9, 23));
		Assert.assertEquals(SolarTerms.QIU_FEN, SolarTerms.getTerm(2021, 10, 7));
		Assert.assertEquals(SolarTerms.HAN_LU, SolarTerms.getTerm(2021, 10, 8));
		Assert.assertEquals(SolarTerms.HAN_LU, SolarTerms.getTerm(2021, 10, 22));
		Assert.assertEquals(SolarTerms.SHUANG_JIANG, SolarTerms.getTerm(2021, 10, 23));
		Assert.assertEquals(SolarTerms.SHUANG_JIANG, SolarTerms.getTerm(2021, 11, 6));
		Assert.assertEquals(SolarTerms.LI_DONG, SolarTerms.getTerm(2021, 11, 7));
		Assert.assertEquals(SolarTerms.LI_DONG, SolarTerms.getTerm(2021, 11, 21));
		Assert.assertEquals(SolarTerms.XIAO_XUE, SolarTerms.getTerm(2021, 11, 22));
		Assert.assertEquals(SolarTerms.XIAO_XUE, SolarTerms.getTerm(2021, 12, 6));
		Assert.assertEquals(SolarTerms.DA_XUE, SolarTerms.getTerm(2021, 12, 7));
		Assert.assertEquals(SolarTerms.DA_XUE, SolarTerms.getTerm(2021, 12, 20));
		Assert.assertEquals(SolarTerms.DONG_ZHI, SolarTerms.getTerm(2021, 12, 21));
	}


	@Test
	public void getTermByDateTest() {
		Assert.assertEquals(SolarTerms.CHUN_FEN, SolarTerms.getTerm(DateUtil.parseDate("2021-04-02")));
	}


	@Test
	public void getTermByChineseDateTest() {
		Assert.assertEquals(SolarTerms.QING_MING, SolarTerms.getTerm(new ChineseDate(2021, 2, 25)));
	}
}
