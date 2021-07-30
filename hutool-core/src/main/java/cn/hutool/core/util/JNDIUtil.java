package cn.hutool.core.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.map.MapUtil;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Map;

/**
 * JNDI工具类<br>
 * JNDI是Java Naming and Directory Interface（JAVA命名和目录接口）的英文简写，<br>
 * 它是为JAVA应用程序提供命名和目录访问服务的API（Application Programing Interface，应用程序编程接口）。
 *
 * @author loolY
 * @since 5.7.7
 */
public class JNDIUtil {

	/**
	 * 创建{@link InitialDirContext}
	 *
	 * @param environment 环境参数，{code null}表示无参数
	 * @return {@link InitialDirContext}
	 */
	public static InitialDirContext createInitialDirContext(Map<String, String> environment) {
		try {
			if (MapUtil.isEmpty(environment)) {
				return new InitialDirContext();
			}
			return new InitialDirContext(Convert.convert(Hashtable.class, environment));
		} catch (NamingException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 创建{@link InitialContext}
	 *
	 * @param environment 环境参数，{code null}表示无参数
	 * @return {@link InitialContext}
	 */
	public static InitialContext createInitialContext(Map<String, String> environment) {
		try {
			if (MapUtil.isEmpty(environment)) {
				return new InitialContext();
			}
			return new InitialContext(Convert.convert(Hashtable.class, environment));
		} catch (NamingException e) {
			throw new UtilException(e);
		}
	}
}
