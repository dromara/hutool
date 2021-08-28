package cn.hutool.core.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.map.MapUtil;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Map;

/**
 * JNDI工具类<br>
 * JNDI是Java Naming and Directory Interface（JAVA命名和目录接口）的英文简写，<br>
 * 它是为JAVA应用程序提供命名和目录访问服务的API（Application Programing Interface，应用程序编程接口）。
 *
 * <p>
 * 见：https://blog.csdn.net/u010430304/article/details/54601302
 * </p>
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

	/**
	 * 获取指定容器环境的对象的属性<br>
	 * 如获取DNS属性，则URI为类似：dns:hutool.cn
	 *
	 * @param uri     URI字符串，格式为[scheme:][name]/[domain]
	 * @param attrIds 需要获取的属性ID名称
	 * @return {@link Attributes}
	 */
	public static Attributes getAttributes(String uri, String... attrIds) {
		try {
			return createInitialDirContext(null).getAttributes(uri, attrIds);
		} catch (NamingException e) {
			throw new UtilException(e);
		}
	}
}
