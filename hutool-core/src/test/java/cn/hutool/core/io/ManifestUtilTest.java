package cn.hutool.core.io;

import cn.hutool.core.util.ManifestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.jar.Manifest;

public class ManifestUtilTest {

	@Test
	public void getManiFestTest(){
		final Manifest manifest = ManifestUtil.getManifest(Test.class);
		Assertions.assertNotNull(manifest);
	}
}
