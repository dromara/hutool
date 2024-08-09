package cn.hutool.core.text;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PasswdStrengthTest {
	@Test
	public void strengthTest(){
		String passwd = "2hAj5#mne-ix.86H";
		assertEquals(13, PasswdStrength.check(passwd));
	}

	@Test
	public void strengthNumberTest(){
		String passwd = "9999999999999";
		assertEquals(0, PasswdStrength.check(passwd));
	}
}
