package cn.hutool.http;

import cn.hutool.core.util.ArrayUtil;
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

	protected static int timeout = -1;
	private static boolean isAllowPatch = false;

	/**
	 * 获取全局默认的超时时长
	 *
	 * @return 全局默认的超时时长
	 */
	public static int getTimeout() {
		return timeout;
	}

	/**
	 * 设置默认的连接和读取超时时长
	 *
	 * @param customTimeout 超时时长
	 */
	synchronized public static void setTimeout(int customTimeout) {
		timeout = customTimeout;
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
	public static void setCookieManager(CookieManager customCookieManager) {
		GlobalCookieManager.setCookieManager(customCookieManager);
	}

	/**
	 * 关闭Cookie
	 *
	 * @since 4.1.9
	 * @see GlobalCookieManager#setCookieManager(CookieManager)
	 */
	public static void closeCookie() {
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
