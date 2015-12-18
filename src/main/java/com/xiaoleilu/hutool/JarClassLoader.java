package com.xiaoleilu.hutool;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * 外部Jar的类加载器
 * @author Looly
 *
 */
public class JarClassLoader extends URLClassLoader{

	//------------------------------------------------------------------- Constructor start
	public JarClassLoader() {
		this(new URL[]{});
	}
	
	public JarClassLoader(URL[] urls) {
		super(urls, ClassUtil.getClassLoader());
	}
	//------------------------------------------------------------------- Constructor end
	
	/**
	 * 加载Jar到ClassPath
	 * @param jarFile jar文件或所在目录
	 * @return JarClassLoader
	 */
	public static JarClassLoader loadJar(File jarFile){
		final JarClassLoader loader = new JarClassLoader();
		try {
			loader.addJar(jarFile);
		} finally {
			FileUtil.close(loader);
		}
		return loader;
	}
	
	/**
	 * 加载Jar文件，或者加载目录
	 * @param jarFile jar文件或者jar文件所在目录
	 */
	public void addJar(File jarFile){
		final List<File> jars = loopJar(jarFile);
			try {
				for (File jar : jars) {
					super.addURL(jar.toURI().toURL());
				}
			} catch (MalformedURLException e) {
				throw new UtilException(e);
			}
	}
	
	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}
	
	//------------------------------------------------------------------- Private method start
	/**
	 * 递归获得Jar文件
	 * @param file jar文件或者包含jar文件的目录
	 * @return jar文件列表
	 */
	private static List<File> loopJar(File file){
		return FileUtil.loopFiles(file, new FileFilter(){
			
			@Override
			public boolean accept(File pathname) {
				final String path = pathname.getPath();
				if(path != null && path.endsWith(".jar")){
					return true;
				}
				return false;
			}
		});
	}
	//------------------------------------------------------------------- Private method end
}
