package cn.hutool.system;

import java.io.Serializable;

/**
 * 代表当前用户的信息。
 */
public class UserInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private final String USER_NAME = SystemUtil.get("user.name", false);
	private final String USER_HOME = SystemUtil.get("user.home", false);
	private final String USER_DIR = SystemUtil.get("user.dir", false);
	private final String USER_LANGUAGE = SystemUtil.get("user.language", false);
	private final String USER_COUNTRY = ((SystemUtil.get("user.country", false) == null) ? SystemUtil.get("user.region", false) : SystemUtil.get("user.country", false));
	private final String JAVA_IO_TMPDIR = SystemUtil.get("java.io.tmpdir", false);

	/**
	 * 取得当前登录用户的名字（取自系统属性：<code>user.name</code>）。
	 * 
	 * <p>
	 * 例如：<code>"admin"</code>
	 * </p>
	 * 
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 * 
	 * @since Java 1.1
	 */
	public final String getName() {
		return USER_NAME;
	}

	/**
	 * 取得当前登录用户的home目录（取自系统属性：<code>user.home</code>）。
	 * 
	 * <p>
	 * 例如：<code>"/home/admin"</code>
	 * </p>
	 * 
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 * 
	 * @since Java 1.1
	 */
	public final String getHomeDir() {
		return USER_HOME;
	}

	/**
	 * 取得当前目录（取自系统属性：<code>user.dir</code>）。
	 * 
	 * <p>
	 * 例如：<code>"/home/admin/working"</code>
	 * </p>
	 * 
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 * 
	 * @since Java 1.1
	 */
	public final String getCurrentDir() {
		return USER_DIR;
	}

	/**
	 * 取得临时目录（取自系统属性：<code>java.io.tmpdir</code>）。
	 * 
	 * <p>
	 * 例如：<code>"/tmp"</code>
	 * </p>
	 * 
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 * 
	 * 
	 */
	public final String getTempDir() {
		return JAVA_IO_TMPDIR;
	}

	/**
	 * 取得当前登录用户的语言设置（取自系统属性：<code>user.language</code>）。
	 * 
	 * <p>
	 * 例如：<code>"zh"</code>、<code>"en"</code>等
	 * </p>
	 * 
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 * 
	 */
	public final String getLanguage() {
		return USER_LANGUAGE;
	}

	/**
	 * 取得当前登录用户的国家或区域设置（取自系统属性：JDK1.4 <code>user.country</code>或JDK1.2 <code>user.region</code>）。
	 * 
	 * <p>
	 * 例如：<code>"CN"</code>、<code>"US"</code>等
	 * </p>
	 * 
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
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

}
