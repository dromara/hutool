package cn.hutool.poi;

import cn.hutool.core.exceptions.DependencyException;
import cn.hutool.core.util.ClassLoaderUtil;

/**
 * POI引入检查器
 * 
 * @author looly
 * @since 4.0.10
 */
public class PoiChecker {
	
	/**
	 * 检查POI包的引入情况
	 */
	public static void checkPoiImport() {
		try {
			Class.forName("org.apache.poi.ss.usermodel.Workbook", false, ClassLoaderUtil.getClassLoader());
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			throw new DependencyException(e, "You need to add POI dependency 'poi-ooxml' to your project, and version >= 3.17");
		}
	}
	
	/**
	 * 当对应的包或类未找到时，抛出指定异常
	 */
	public static DependencyException transError(NoClassDefFoundError e) {
		switch (e.getMessage()) {
		case "org/apache/poi/ss/usermodel/Workbook":
			return new DependencyException(e, "You need to add POI dependency 'poi-ooxml' to your project, and version >= 3.17");
		case "org/apache/poi/poifs/filesystem/FileMagic":
			return new DependencyException(e, "You need to add POI dependency 'poi-ooxml' to your project, and version >= 3.17");

		default:
			return new DependencyException(e);
		}
	}
}
