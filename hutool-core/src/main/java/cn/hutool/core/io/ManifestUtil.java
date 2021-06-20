package cn.hutool.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Jar包中manifest.mf文件获取和解析工具类
 * 来自Jodd
 *
 * @author looly, jodd
 * @since 5.7.0
 */
public class ManifestUtil {
	private static final String[] MANIFEST_NAMES = {"Manifest.mf", "manifest.mf", "MANIFEST.MF"};

	public static Manifest getManifest(File classpathItem) {
		Manifest manifest = null;

		if (classpathItem.isFile()) {
			JarFile jar = null;
			try {
				jar = new JarFile(classpathItem);
				manifest = jar.getManifest();
			} catch (final IOException ignore) {
			} finally {
				IoUtil.close(jar);
			}
		} else {
			final File metaDir = new File(classpathItem, "META-INF");
			File manifestFile = null;
			if (metaDir.isDirectory()) {
				for (final String name : MANIFEST_NAMES) {
					final File mFile = new File(metaDir, name);
					if (mFile.isFile()) {
						manifestFile = mFile;
						break;
					}
				}
			}
			if (manifestFile != null) {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(manifestFile);
					manifest = new Manifest(fis);
				} catch (final IOException ignore) {
				}
				finally {
					IoUtil.close(fis);
				}
			}
		}

		return manifest;
	}
}
