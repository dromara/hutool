package cn.hutool.system;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.Serializable;

/**
 * 代表当前用户的信息。
 */
public class UserInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private final String USER_NAME;
	private final String USER_HOME;
	private final String USER_DIR;
	private final String JAVA_IO_TMPDIR;
	private final String USER_LANGUAGE;
	private final String USER_COUNTRY;

	public UserInfo(){
		USER_NAME = fixPath(SystemUtil.get("user.name", false));
		USER_HOME = fixPath(SystemUtil.get("user.home", false));
		USER_DIR = fixPath(SystemUtil.get("user.dir", false));
		JAVA_IO_TMPDIR = fixPath(SystemUtil.get("java.io.tmpdir", false));
		USER_LANGUAGE = SystemUtil.get("user.language", false);

		// JDK1.4 {@code user.country}，JDK1.2 {@code user.region}
		String userCountry = SystemUtil.get("user.country", false);
		if(null == userCountry){
			userCountry = SystemUtil.get("user.country", false);
		}
		USER_COUNTRY = userCountry;
	}

	/**
	 * 取得当前登录用户的名字（取自系统属性：{@code user.name}）。
	 *
	 * <p>
	 * 例如：{@code "admin"}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 * @since Java 1.1
	 */
	public final String getName() {
		return USER_NAME;
	}

	/**
	 * 取得当前登录用户的home目录（取自系统属性：{@code user.home}）。
	 *
	 * <p>
	 * 例如：{@code "/home/admin/"}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 * @since Java 1.1
	 */
	public final String getHomeDir() {
		return USER_HOME;
	}

	/**
	 * 取得当前目录（取自系统属性：{@code user.dir}）。
	 *
	 * <p>
	 * 例如：{@code "/home/admin/working/"}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 * @since Java 1.1
	 */
	public final String getCurrentDir() {
		return USER_DIR;
	}

	/**
	 * 取得临时目录（取自系统属性：{@code java.io.tmpdir}）。
	 *
	 * <p>
	 * 例如：{@code "/tmp/"}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 *
	 */
	public final String getTempDir() {
		return JAVA_IO_TMPDIR;
	}

	/**
	 * 取得当前登录用户的语言设置（取自系统属性：{@code user.language}）。
	 *
	 * <p>
	 * 例如：{@code "zh"}、{@code "en"}等
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 */
	public final String getLanguage() {
		return USER_LANGUAGE;
	}

	/**
	 * 取得当前登录用户的国家或区域设置（取自系统属性：JDK1.4 {@code user.country}或JDK1.2 {@code user.region}）。
	 *
	 * <p>
	 * 例如：{@code "CN"}、{@code "US"}等
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 */
	public final String getCountry() {
		return USER_COUNTRY;
	}

	/**
	 * 将当前用户的信息转换成字符串。
	 *
	 * @return 用户信息的字符串表示
	 */
	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();

		SystemUtil.append(builder, "User Name:        ", getName());
		SystemUtil.append(builder, "User Home Dir:    ", getHomeDir());
		SystemUtil.append(builder, "User Current Dir: ", getCurrentDir());
		SystemUtil.append(builder, "User Temp Dir:    ", getTempDir());
		SystemUtil.append(builder, "User Language:    ", getLanguage());
		SystemUtil.append(builder, "User Country:     ", getCountry());

		return builder.toString();
	}

	/**
	 * 修正路径，包括：
	 *
	 * <ul>
	 *     <li>1. 末尾补充 /</li>
	 * </ul>
	 * @param path 路径
	 * @return 修正后的路径
	 * @since 5.6.4
	 */
	private static String fixPath(String path){
		return StrUtil.addSuffixIfNot(path, File.separator);
	}
}
