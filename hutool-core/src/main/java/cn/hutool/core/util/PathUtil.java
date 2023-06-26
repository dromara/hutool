package cn.hutool.core.util;

/**
 * 路径工具类
 *
 * @author zhinushannan
 */
public class PathUtil {

	/**
	 * 检测指定 路径 是否匹配通配符 wildcard
	 *
	 * @param wildcard 通配符
	 * @param path     路径
	 * @return 是否匹配
	 */
	public static boolean matches(String wildcard, String path) {
		if (wildcard.equals("**")) {
			return true;
		}

		String[] wildcards = wildcard.split("/");
		String[] paths = path.split("/");

		int wildcardIndex = 0;
		int pathIndex = 0;
		int wildcardLen = wildcards.length;
		int pathLen = paths.length;

		while (wildcardIndex < wildcardLen && pathIndex < pathLen) {
			String wildcardSplit = wildcards[wildcardIndex];
			if (wildcardSplit.equals("**")) {
				return true;
			}

			String pathSplit = paths[pathIndex];

			if (!(wildcardSplit.equals("*") || wildcardSplit.equals(pathSplit))) {
				return false;
			}

			wildcardIndex++;
			pathIndex++;
		}

		return wildcardIndex == wildcardLen && pathIndex == pathLen;
	}

}
