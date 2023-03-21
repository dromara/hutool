package cn.hutool.crypto.asymmetric;

import cn.hutool.crypto.SecureUtil;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("FieldCanBeLocal")
public class IssueI6OQJATest {

	private static final String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOgjgkzy33Kg3XzV4r0dpa3o6klM85TVn5jS6woBsTRuU0SsDJzqjXdF34G9uZgbHN74RHoOjO2sRM98tnjEJ8WvqqPBOimvpBeTqVGrWRXtelXhPXaSfYdipGIp2stHr270GTg5+chTrfOn7rp1PA09AoRM+HULaU31St0wntf/AgMBAAECgYBwb9qJ6M1f2RjOgU58aSK5dGoeLN6CRWIzBF4Bj8ZD7ff4+Bh33Ie+sKJMVhfR27gFK10HfYq3B8ygbvh20BOumU9U6xFMOff5yPjOoCAFfa7k69hjPaq8Ls/H9kT4sG+djZAyc43JVjUv0J9VFRlCtgEJHNpWUlTPLaqc+1ScEQJBAP1Ewd2nStmXjgHeMiB+NBhY0QSIN5HBW07MlmsMbJir+OWN0t8YKoYZXynei6UDu6wrwTCCDRhcSCpFy9bA9+cCQQDqpGouK8zhvcM/yT1C+f/Hh9cFIlqLKsHssmSva0lTKVE5O7104VXEwNjufjRwaGLc0bRgs/aJh8W7EcGp2zwpAkEAjsk40xIB7PK4qOzwLcl47VEFZhy114K/S4mkM+3pO5mY1TJD9GrXborXT++bowibwdFZNVPctiMwvERlS0m3eQJBAKEKvyV5QmEdEMjSoY06cGbNwLHxZhtl+TsvJROQmv7MuMaDTgDON0OW6Eynqe4Mdu3/r8E/QtIZsYg3I6gkpCECQQDijoNBop46kR/udUEPCaMjy3lzUVklAGAMKE+mc7n50+A3CeMDXAYU/OCf7vMjo2Uq44CE/yIHtYURn0usCzoB";
	private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDoI4JM8t9yoN181eK9HaWt6OpJTPOU1Z+Y0usKAbE0blNErAyc6o13Rd+BvbmYGxze+ER6DoztrETPfLZ4xCfFr6qjwTopr6QXk6lRq1kV7XpV4T12kn2HYqRiKdrLR69u9Bk4OfnIU63zp+66dTwNPQKETPh1C2lN9UrdMJ7X/wIDAQAB";

	@Test
	public void genKeyTest() {
		Assert.assertEquals(passCryto("123"), passCryto("123"));
	}

	@SuppressWarnings("SameParameterValue")
	private String passCryto(final String value){
		return SecureUtil.rsa(privateKey, publicKey)
				.encryptBase64(value, KeyType.PrivateKey);
	}
}
