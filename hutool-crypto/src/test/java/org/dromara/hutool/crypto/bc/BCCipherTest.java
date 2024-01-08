package org.dromara.hutool.crypto.bc;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DefaultBufferedBlockCipher;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.crypto.CipherMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BCCipherTest {
	@Test
	void sm4Test() {
		final byte[] data = ByteUtil.toUtf8Bytes("我是测试Hutool的字符串00");

		final BufferedBlockCipher blockCipher = new DefaultBufferedBlockCipher(CBCBlockCipher.newInstance(new SM4Engine()));
		final BCCipher bcCipher = new BCCipher(blockCipher);
		bcCipher.init(CipherMode.ENCRYPT, new BCCipher.BCParameters(new KeyParameter(ByteUtil.toUtf8Bytes("1234567890000000"))));
		final byte[] encryptData = bcCipher.processFinal(data);

		bcCipher.init(CipherMode.DECRYPT, new BCCipher.BCParameters(new KeyParameter(ByteUtil.toUtf8Bytes("1234567890000000"))));
		final byte[] decryptData = bcCipher.processFinal(encryptData);

		Assertions.assertArrayEquals(data, decryptData);
	}
}
