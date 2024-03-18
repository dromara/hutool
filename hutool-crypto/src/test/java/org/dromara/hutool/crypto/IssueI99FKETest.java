package org.dromara.hutool.crypto;

import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.KeyPair;

public class IssueI99FKETest {
	@Test
	void getSm2KeyTest() {
		final KeyPair pair = KeyUtil.generateKeyPair("SM2");
		final ECPrivateKey aPrivate = (ECPrivateKey) pair.getPrivate();
		final BigInteger d = aPrivate.getD();
		Assertions.assertNotNull(d);

		final ECPublicKey aPublic = (ECPublicKey) pair.getPublic();
		final ECPoint point = aPublic.getQ();
		final BigInteger x = point.getXCoord().toBigInteger();
		final BigInteger y = point.getYCoord().toBigInteger();
		Assertions.assertNotNull(x);
		Assertions.assertNotNull(y);
	}
}
