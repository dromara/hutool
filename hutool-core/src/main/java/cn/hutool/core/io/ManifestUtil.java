package cn.hutool.core.io;

import cn.hutool.core.io.resource.ResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
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

	/**
	 * 根据 class 获取 所在 jar 包文件的 Manifest<br>
	 * 此方法主要利用class定位jar包，如引入hutool-all，则传入hutool中任意一个类即可获取这个jar的Manifest信息<br>
	 * 如果这个类不在jar包中，返回{@code null}
	 *
	 * @param cls 类
	 * @return Manifest
	 * @throws IORuntimeException IO异常
	 */
	public static Manifest getManifest(Class<?> cls) throws IORuntimeException {
		URL url = ResourceUtil.getResource(null, cls);
		URLConnection connection;
		try {
			connection = url.openConnection();
		}catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if (connection instanceof JarURLConnection) {
			JarURLConnection conn = (JarURLConnection) connection;
			return getManifest(conn);
		}
		return null;
	}

	/**
	 * 获取 jar 包文件或项目目录下的 Manifest
	 *
	 * @param classpathItem 文件路径
	 * @return Manifest
	 * @throws IORuntimeException IO异常
	 */
	public static Manifest getManifest(File classpathItem) throws IORuntimeException{
		Manifest manifest = null;

		if (classpathItem.isFile()) {
			try (JarFile jarFile = new JarFile(classpathItem)){
				manifest = getManifest(jarFile);
			} catch (final IOException e) {
				throw new IORuntimeException(e);
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
			if (null != manifestFile) {
				try(FileInputStream fis = new FileInputStream(manifestFile)){
					manifest = new Manifest(fis);
				} catch (final IOException e) {
					throw new IORuntimeException(e);
				}
			}
		}

		return manifest;
	}

	/**
	 * 根据 {@link JarURLConnection} 获取 jar 包文件的 Manifest
	 *
	 * @param connection {@link JarURLConnection}
	 * @return Manifest
	 * @throws IORuntimeException IO异常
	 */
	public static Manifest getManifest(JarURLConnection connection) throws IORuntimeException{
		final JarFile jarFile;
		try {
			jarFile = connection.getJarFile();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return getManifest(jarFile);
	}

	/**
	 * 根据 {@link JarURLConnection} 获取 jar 包文件的 Manifest
	 *
	 * @param jarFile {@link JarURLConnection}
	 * @return Manifest
	 * @throws IORuntimeException IO异常
	 */
	public static Manifest getManifest(JarFile jarFile) throws IORuntimeException {
		try {
			return jarFile.getManifest();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
