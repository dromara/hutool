package cn.hutool.http.client.engine.jdk;

import cn.hutool.core.reflect.FieldUtil;
import cn.hutool.core.reflect.ModifierUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.SystemUtil;
import cn.hutool.http.HttpException;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * 针对{@link HttpURLConnection}相关工具
 *
 * @author looly
 * @since 6.0.0
 */
public class HttpUrlConnectionUtil {

	private static final String[] METHODS = {
			"GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE", "PATCH"
	};

	/**
	 * 增加支持的METHOD方法<br>
	 * 此方法通过注入方式修改{@link HttpURLConnection}中的methods静态属性，增加PATCH方法<br>
	 * see: <a href="https://stackoverflow.com/questions/25163131/httpurlconnection-invalid-http-method-patch">https://stackoverflow.com/questions/25163131/httpurlconnection-invalid-http-method-patch</a>
	 */
	public static void allowPatch() {
		AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
			doAllowPatch();
			return null;
		});
	}

	/**
	 * 增加支持的METHOD方法<br>
	 * 此方法通过注入方式修改{@link HttpURLConnection}中的methods静态属性，增加PATCH方法<br>
	 * see: <a href="https://stackoverflow.com/questions/25163131/httpurlconnection-invalid-http-method-patch">https://stackoverflow.com/questions/25163131/httpurlconnection-invalid-http-method-patch</a>
	 *
	 * @since 5.7.4
	 */
	synchronized private static void doAllowPatch() {
		final Field methodsField = FieldUtil.getField(HttpURLConnection.class, "methods");
		if (null == methodsField) {
			throw new HttpException("None static field [methods] with Java version: [{}]", SystemUtil.get("java.version"));
		}

		final Object staticFieldValue = FieldUtil.getStaticFieldValue(methodsField);
		if (false == ArrayUtil.equals(METHODS, staticFieldValue)) {
			// 去除final修饰
			ModifierUtil.removeFinalModify(methodsField);
			FieldUtil.setStaticFieldValue(methodsField, METHODS);
		}
	}
}
