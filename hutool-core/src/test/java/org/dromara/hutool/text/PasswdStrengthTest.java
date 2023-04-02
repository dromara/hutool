package org.dromara.hutool.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswdStrengthTest {
	@Test
	public void strengthTest(){
		final String passwd = "2hAj5#mne-ix.86H";
		Assertions.assertEquals(13, PasswdStrength.check(passwd));
	}

	@Test
	public void strengthNumberTest(){
		final String passwd = "9999999999999";
		Assertions.assertEquals(0, PasswdStrength.check(passwd));
	}
}
