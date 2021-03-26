package cn.hutool.crypto.test.asymmetric;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricCrypto;
import cn.hutool.crypto.asymmetric.ECIES;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.sun.org.apache.bcel.internal.generic.FADD;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyPair;

/**
 * @author vincentruan
 * @version 1.0.0
 */
public class AsymmetricTest {

	@Test
	public void encryptAndDecryptWithHugeFileTest() throws IOException {
		KeyPair pair = SecureUtil.generateKeyPair("RSA");

		testEND(new RSA(pair.getPrivate(), pair.getPublic()), true);
		testEND(new RSA(pair.getPrivate(), pair.getPublic()), false);

		testEND(new ECIES(pair.getPrivate(), pair.getPublic()), true);
		testEND(new ECIES(pair.getPrivate(), pair.getPublic()), false);
	}

	private void testEND(AsymmetricCrypto asymmetricCrypto, boolean reversed) throws IOException {
		File originalFile = new File("F:\\Downloads\\Twk2ndEvolutionCh1.rar");
		String originalMd5 = SecureUtil.md5(originalFile);
		try (BufferedInputStream bis = FileUtil.getInputStream(originalFile)) {
			File encryptedFile = asymmetricCrypto.encrypt(bis, Paths.get("F:\\dm\\fafafa", "Twk2ndEvolutionCh1.rar." + asymmetricCrypto.getClass().getSimpleName() + ".enc"), reversed ? KeyType.PrivateKey : KeyType.PublicKey);
			try (BufferedInputStream encBis = FileUtil.getInputStream(encryptedFile)) {
				File decryptedFile = asymmetricCrypto.decrypt(encBis, Paths.get("F:\\dm\\fafafa", "Twk2ndEvolutionCh1." + asymmetricCrypto.getClass().getSimpleName() + ".dec.rar"), reversed ? KeyType.PublicKey : KeyType.PrivateKey);
				String decryptedMd5 = new MD5().digestHex(decryptedFile);
				Assert.assertEquals(asymmetricCrypto.getClass().getSimpleName() + " md5 check failed.", originalMd5, decryptedMd5);
			}
		}
	}

}
