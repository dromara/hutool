package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

public class PasswdStrengthTest {
	@Test
	public void strengthTest(){
		String passwd = "2hAj5#mne-ix.86H";
		Assert.assertEquals(13, PasswdStrength.check(passwd));
	}

	@Test
	public void strengthNumberTest(){
		String passwd = "9999999999999";
		Assert.assertEquals(0, PasswdStrength.check(passwd));
	}
}
