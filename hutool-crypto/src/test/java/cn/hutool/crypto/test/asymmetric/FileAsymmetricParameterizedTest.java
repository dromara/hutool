package cn.hutool.crypto.test.asymmetric;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.ClassScanner;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricCrypto;
import cn.hutool.crypto.asymmetric.ECIES;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.MD5;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.crypto.SecretKey;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文件对称加密算法单元测试
 *
 * @author vincentruan
 */
@RunWith(Parameterized.class)
public class FileAsymmetricParameterizedTest {

	private final AsymmetricCrypto asymmetricCrypto;

	private final boolean reversed;

	public FileAsymmetricParameterizedTest(AsymmetricCrypto asymmetricCrypto, boolean reversed) {
		this.asymmetricCrypto = asymmetricCrypto;
		this.reversed = reversed;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> prepareData() {
		// 测试数据
		Set<Class<?>> subClassSet = ClassScanner.scanPackageBySuper("cn.hutool.crypto.asymmetric", AsymmetricCrypto.class);
		// 将数组转换成集合返回
		List<Object[]> c1 = subClassSet.stream().map(ReflectUtil::newInstanceIfPossible).filter(Objects::nonNull).map(c -> new Object[]{c, true}).collect(Collectors.toList());
		List<Object[]> c2 = subClassSet.stream().map(ReflectUtil::newInstanceIfPossible).filter(Objects::nonNull).map(c -> new Object[]{c, false}).collect(Collectors.toList());
		c1.addAll(c2);
		return c1;
	}

	@Test
	public void testEND() throws IOException {
		String shortClassName = asymmetricCrypto.getClass().getSimpleName();
		System.out.println(shortClassName);
		File originalFile = new File("F:\\dm\\fafafa\\largedata.rar");
		String originalMd5 = SecureUtil.md5(originalFile);
		Path encryptedFilePath = Paths.get("F:\\dm\\fafafa", "largedata.rar." + shortClassName + ".enc");
		Path decryptedFilePath = Paths.get("F:\\dm\\fafafa", "largedata." + shortClassName + ".dec.rar");
		try (BufferedInputStream bis = FileUtil.getInputStream(originalFile)) {
			File encryptedFile = asymmetricCrypto.encrypt(bis, encryptedFilePath, reversed ? KeyType.PrivateKey : KeyType.PublicKey);
			try (BufferedInputStream encBis = FileUtil.getInputStream(encryptedFile)) {
				File decryptedFile = asymmetricCrypto.decrypt(encBis, decryptedFilePath, reversed ? KeyType.PublicKey : KeyType.PrivateKey);
				String decryptedMd5 = new MD5().digestHex(decryptedFile);
				Assert.assertEquals(asymmetricCrypto.getClass().getSimpleName() + " md5 check failed.", originalMd5, decryptedMd5);
			}
		} finally {
			FileUtil.delQuietly(encryptedFilePath);
			FileUtil.delQuietly(decryptedFilePath);
		}

	}
}
