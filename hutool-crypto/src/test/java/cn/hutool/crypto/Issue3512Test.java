package cn.hutool.crypto;

import cn.hutool.crypto.asymmetric.SignAlgorithm;
import org.junit.Test;

public class Issue3512Test {
	@Test
	public void signTest() {
		SecureUtil.sign(SignAlgorithm.SHA256withRSA);
	}
}
