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

	/** 没有引入POI的错误消息 */
	public static final String NO_POI_ERROR_MSG = "You need to add dependency of 'poi-ooxml' to your project, and version >= 4.1.2";

	/**
	 * 检查POI包的引入情况
	 */
	public static void checkPoiImport() {
		try {
			Class.forName("org.apache.poi.ss.usermodel.Workbook", false, ClassLoaderUtil.getClassLoader());
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			throw new DependencyException(e, NO_POI_ERROR_MSG);
		}
	}
}
