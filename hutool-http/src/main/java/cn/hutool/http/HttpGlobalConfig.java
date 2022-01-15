package cn.hutool.http;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.http.cookie.GlobalCookieManager;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.HttpURLConnection;

/**
 * HTTP 全局参数配置
 *
 * @author Looly
 * @since 4.6.2
 */
public class HttpGlobalConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * -1: 含义，永不超时。
	 * 如果：设置timeout = 3s(3000 ms), 那一次请求最大超时：就是：6s
	 * 官方含义：timeout of zero is interpreted as an infinite timeout. （0的超时被解释为无限超时。）
	 * 这里实际项目一定要进行修改，防止把系统拖死.
	 * 底层调用：{@link HttpURLConnection#setReadTimeout(int)} 同时设置: 读取超时
	 * 底层调用：{@link HttpURLConnection#setConnectTimeout(int)} 同时设置: 连接超时
	 */
	private static int timeout = -1;
	private static boolean isAllowPatch = false;
	private static String boundary = "--------------------Hutool_" + RandomUtil.randomString(16);
	private static int maxRedirectCount = 0;
	private static boolean ignoreEOFError = true;

	/**
	 * 获取全局默认的超时时长
	 *
	 * @return 全局默认的超时时长
	 */
	public static int getTimeout() {
		return timeout;
	}

	/**
	 * 设置默认的连接和读取超时时长<br>
	 * -1: 含义，永不超时。<br>
	 * 如果：设置timeout = 3s(3000 ms), 那一次请求最大超时：就是：6s<br>
	 * 官方含义：timeout of zero is interpreted as an infinite timeout. （0的超时被解释为无限超时。）<br>
	 * 这里实际项目一定要进行修改，防止把系统拖死.<br>
	 * 底层调用：{@link HttpURLConnection#setReadTimeout(int)} 同时设置: 读取超时<br>
	 * 底层调用：{@link HttpURLConnection#setConnectTimeout(int)} 同时设置: 连接超时
	 *
	 * @param customTimeout 超时时长
	 */
	synchronized public static void setTimeout(int customTimeout) {
		timeout = customTimeout;
	}

	/**
	 * 获取全局默认的Multipart边界
	 *
	 * @return 全局默认的Multipart边界
	 * @since 5.7.17
	 */
	public static String getBoundary() {
		return boundary;
	}

	/**
	 * 设置默认的Multipart边界
	 *
	 * @param customBoundary 自定义Multipart边界
	 * @since 5.7.17
	 */
	synchronized public static void setBoundary(String customBoundary) {
		boundary = customBoundary;
	}

	/**
	 * 获取全局默认的最大重定向次数，如设置0表示不重定向<br>
	 * 如果设置为1，表示重定向一次，即请求两次
	 *
	 * @return 全局默认的最大重定向次数
	 * @since 5.7.19
	 */
	public static int getMaxRedirectCount() {
		return maxRedirectCount;
	}

	/**
	 * 设置默认全局默认的最大重定向次数，如设置0表示不重定向<br>
	 * 如果设置为1，表示重定向一次，即请求两次
	 *
	 * @param customMaxRedirectCount 全局默认的最大重定向次数
	 * @since 5.7.19
	 */
	synchronized public static void setMaxRedirectCount(int customMaxRedirectCount) {
		maxRedirectCount = customMaxRedirectCount;
	}

	/**
	 * 获取是否忽略响应读取时可能的EOF异常。<br>
	 * 在Http协议中，对于Transfer-Encoding: Chunked在正常情况下末尾会写入一个Length为0的的chunk标识完整结束。<br>
	 * 如果服务端未遵循这个规范或响应没有正常结束，会报EOF异常，此选项用于是否忽略这个异常。
	 *
	 * @return 是否忽略响应读取时可能的EOF异常
	 * @since 5.7.20
	 */
	public static boolean isIgnoreEOFError() {
		return ignoreEOFError;
	}

	/**
	 * 设置是否忽略响应读取时可能的EOF异常。<br>
	 * 在Http协议中，对于Transfer-Encoding: Chunked在正常情况下末尾会写入一个Length为0的的chunk标识完整结束。<br>
	 * 如果服务端未遵循这个规范或响应没有正常结束，会报EOF异常，此选项用于是否忽略这个异常。
	 *
	 * @param customIgnoreEOFError 是否忽略响应读取时可能的EOF异常。
	 * @since 5.7.20
	 */
	synchronized public static void setIgnoreEOFError(boolean customIgnoreEOFError) {
		ignoreEOFError = customIgnoreEOFError;
	}

	/**
	 * 获取Cookie管理器，用于自定义Cookie管理
	 *
	 * @return {@link CookieManager}
	 * @since 4.1.0
	 * @see GlobalCookieManager#getCookieManager()
	 */
	public static CookieManager getCookieManager() {
		return GlobalCookieManager.getCookieManager();
	}

	/**
	 * 自定义{@link CookieManager}
	 *
	 * @param customCookieManager 自定义的{@link CookieManager}
	 * @since 4.5.14
	 * @see GlobalCookieManager#setCookieManager(CookieManager)
	 */
	synchronized public static void setCookieManager(CookieManager customCookieManager) {
		GlobalCookieManager.setCookieManager(customCookieManager);
	}

	/**
	 * 关闭Cookie
	 *
	 * @since 4.1.9
	 * @see GlobalCookieManager#setCookieManager(CookieManager)
	 */
	synchronized public static void closeCookie() {
		GlobalCookieManager.setCookieManager(null);
	}

	/**
	 * 增加支持的METHOD方法<br>
	 * 此方法通过注入方式修改{@link HttpURLConnection}中的methods静态属性，增加PATCH方法<br>
	 * see: https://stackoverflow.com/questions/25163131/httpurlconnection-invalid-http-method-patch
	 *
	 * @since 5.7.4
	 */
	synchronized public static void allowPatch() {
		if(isAllowPatch){
			return;
		}
		final Field methodsField = ReflectUtil.getField(HttpURLConnection.class, "methods");
		if (null == methodsField) {
			throw new HttpException("None static field [methods] with Java version: [{}]", System.getProperty("java.version"));
		}

		// 去除final修饰
		ReflectUtil.setFieldValue(methodsField, "modifiers", methodsField.getModifiers() & ~Modifier.FINAL);
		final String[] methods = {
				"GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE", "PATCH"
		};
		ReflectUtil.setFieldValue(null, methodsField, methods);

		// 检查注入是否成功
		final Object staticFieldValue = ReflectUtil.getStaticFieldValue(methodsField);
		if(false == ArrayUtil.equals(methods, staticFieldValue)){
			throw new HttpException("Inject value to field [methods] failed!");
		}

		isAllowPatch = true;
	}
}
