/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto;

import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.crypto.asymmetric.Sign;
import org.dromara.hutool.crypto.asymmetric.SignAlgorithm;
import org.dromara.hutool.crypto.bc.PemUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;

public class SignUtilTest {

	@Test
	void signTest() {
		final Sign sign = SignUtil.sign(SignAlgorithm.SHA256withRSA);

		// 验证base64转换
		final String base64 = KeyUtil.toBase64(sign.getPublicKey());
		final PublicKey publicKey = KeyUtil.generatePublicKey(SignAlgorithm.SHA256withRSA.getValue(), Base64.decode(base64));
		Assertions.assertEquals(sign.getPublicKey(), publicKey);

		final String signHex = sign.signHex("abcd");
		Assert.notNull(signHex);
	}

	@Test
	void issueI6SRUFTest() {
		// pem格式
		final String publicKey = "-----BEGIN CERTIFICATE-----\n" +
			"MIIEMTCCAxmgAwIBAgIGAXRTgcMnMA0GCSqGSIb3DQEBCwUAMHYxCzAJBgNVBAYT\n" +
			"AkNOMRAwDgYDVQQIDAdCZWlKaW5nMRAwDgYDVQQHDAdCZWlKaW5nMRcwFQYDVQQK\n" +
			"DA5MYWthbGEgQ28uLEx0ZDEqMCgGA1UEAwwhTGFrYWxhIE9yZ2FuaXphdGlvbiBW\n" +
			"YWxpZGF0aW9uIENBMB4XDTIwMTAxMDA1MjQxNFoXDTMwMTAwODA1MjQxNFowZTEL\n" +
			"MAkGA1UEBhMCQ04xEDAOBgNVBAgMB0JlaUppbmcxEDAOBgNVBAcMB0JlaUppbmcx\n" +
			"FzAVBgNVBAoMDkxha2FsYSBDby4sTHRkMRkwFwYDVQQDDBBBUElHVy5MQUtBTEEu\n" +
			"Q09NMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt1zHL54HiI8d2sLJ\n" +
			"lwoQji3/ln0nsvfZ/XVpOjuB+1YR6/0LdxEDMC/hxI6iH2Rm5MjwWz3dmN/6BZeI\n" +
			"gwGeTOWJUZFARo8UduKrlhC6gWMRpAiiGC8wA8stikc5gYB+UeFVZi/aJ0WN0cpP\n" +
			"JYCvPBhxhMvhVDnd4hNohnR1L7k0ypuWg0YwGjC25FaNAEFBYP9EYUyCJjE//9Z7\n" +
			"sMzHR9SJYCqqo6r9bOH9G6sWKuEp+osuAh+kJIxJMHfipw7w3tEcWG0hce9u/el4\n" +
			"cYJtg8/PPMVoccKmeCzMvarr7jdKP4lenJbtwlgyfs+JgNu60KMUJH8RS72wC9NY\n" +
			"uFz09wIDAQABo4HVMIHSMIGSBgNVHSMEgYowgYeAFCnH4DkZPR6CZxRn/kIqVsMo\n" +
			"dJHpoWekZTBjMQswCQYDVQQGEwJDTjEQMA4GA1UECAwHQmVpSmluZzEQMA4GA1UE\n" +
			"BwwHQmVpSmluZzEXMBUGA1UECgwOTGFrYWxhIENvLixMdGQxFzAVBgNVBAMMDkxh\n" +
			"a2FsYSBSb290IENBggYBaiUALIowHQYDVR0OBBYEFJ2Kx9YZfmWpkKFnC33C0r5D\n" +
			"K3rFMAwGA1UdEwEB/wQCMAAwDgYDVR0PAQH/BAQDAgeAMA0GCSqGSIb3DQEBCwUA\n" +
			"A4IBAQBZoeU0XyH9O0LGF9R+JyGwfU/O5amoB97VeM+5n9v2z8OCiIJ8eXVGKN9L\n" +
			"tl9QkpTEanYwK30KkpHcJP1xfVkhPi/cCMgfTWQ5eKYC7Zm16zk7n4CP6IIgZIqm\n" +
			"TVGsIGKk8RzWseyWPB3lfqMDR52V1tdA1S8lJ7a2Xnpt5M2jkDXoArl3SVSwCb4D\n" +
			"AmThYhak48M++fUJNYII9JBGRdRGbfJ2GSFdPXgesUL2CwlReQwbW4GZkYGOg9LK\n" +
			"CNPK6XShlNdvgPv0CCR08KCYRwC3HZ0y1F0NjaKzYdGNPrvOq9lA495ONZCvzYDo\n" +
			"gmsu/kd6eqxTs/JwdaIYr4sCMg8Z\n" +
			"-----END CERTIFICATE-----";

		final PublicKey key = (PublicKey) PemUtil.readPemKey(IoUtil.toUtf8Stream(publicKey));
		new Sign(SignAlgorithm.SHA256withRSA, null, key);
	}
}
