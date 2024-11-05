package org.dromara.hutool.core.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class Ipv6UtilTest {
	@Test
	void bigIntegerToIPv6Test() {
		final BigInteger bigInteger = new BigInteger("21987654321098765432109876543210", 10);
		final String ipv6Address = Ipv6Util.bigIntegerToIPv6(bigInteger);
		Assertions.assertEquals("0:115:85f1:5eb3:c74d:a870:11c6:7eea", ipv6Address);
	}
}
