package org.dromara.hutool.core.io;

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
