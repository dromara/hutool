package cn.hutool.crypto.test.symmetric;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.ClassScanner;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
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
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文件对称加密算法单元测试
 *
 * @author vincentruan
 */
@RunWith(Parameterized.class)
public class FileSymmetricParameterizedTest {

	private final SymmetricCrypto symmetricCrypto;

	public FileSymmetricParameterizedTest(SymmetricCrypto symmetricCrypto) {
		this.symmetricCrypto = symmetricCrypto;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> prepareData() {
		final SecureRandom random = RandomUtil.getSecureRandom("123456".getBytes());
		// 测试数据
		Set<Class<?>> subClassSet = ClassScanner.scanPackageBySuper("cn.hutool.crypto.symmetric", SymmetricCrypto.class);
		// 将数组转换成集合返回
		return subClassSet.stream().map(ReflectUtil::newInstanceIfPossible).filter(Objects::nonNull).map(c -> new Object[]{c}).collect(Collectors.toList());
	}

	@Test
	public void testEND() throws IOException {
		String shortClassName = symmetricCrypto.getClass().getSimpleName();
		File originalFile = new File("F:\\dm\\fafafa\\largedata.rar");
		String originalMd5 = SecureUtil.md5(originalFile);
		Path encryptedFilePath = Paths.get("F:\\dm\\fafafa", "largedata.rar." + shortClassName + ".enc");
		Path decryptedFilePath = Paths.get("F:\\dm\\fafafa", "largedata." + shortClassName + ".dec.rar");
		try (BufferedInputStream bis = FileUtil.getInputStream(originalFile)) {
			File encryptedFile = symmetricCrypto.encrypt(bis, encryptedFilePath);
			try (BufferedInputStream encBis = FileUtil.getInputStream(encryptedFile)) {
				File decryptedFile = symmetricCrypto.decrypt(encBis, decryptedFilePath);
				String decryptedMd5 = new MD5().digestHex(decryptedFile);
				Assert.assertEquals(shortClassName + " md5 check failed.", originalMd5, decryptedMd5);
			}
		} finally {
			FileUtil.delQuietly(encryptedFilePath);
			FileUtil.delQuietly(decryptedFilePath);
		}

	}
}
