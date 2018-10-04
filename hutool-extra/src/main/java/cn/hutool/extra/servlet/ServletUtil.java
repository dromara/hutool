package cn.hutool.extra.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.multipart.MultipartFormData;
import cn.hutool.extra.servlet.multipart.UploadSetting;

/**
 * Servlet相关工具类封装
 * 
 * @author looly
 * @since 3.2.0
 */
public class ServletUtil {

	public static final String METHOD_DELETE = "DELETE";
	public static final String METHOD_HEAD = "HEAD";
	public static final String METHOD_GET = "GET";
	public static final String METHOD_OPTIONS = "OPTIONS";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_PUT = "PUT";
	public static final String METHOD_TRACE = "TRACE";

	// --------------------------------------------------------- getParam start
	/**
	 * 获得所有请求参数
	 * 
	 * @param request 请求对象{@link ServletRequest}
	 * @return Map
	 */
	public static Map<String, String[]> getParams(ServletRequest request) {
		final Map<String, String[]> map = request.getParameterMap();
		return Collections.unmodifiableMap(map);
	}

	/**
	 * 获得所有请求参数
	 * 
	 * @param request 请求对象{@link ServletRequest}
	 * @return Map
	 */
	public static Map<String, String> getParamMap(ServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		for (Map.Entry<String, String[]> entry : getParams(request).entrySet()) {
			params.put(entry.getKey(), ArrayUtil.join(entry.getValue(), StrUtil.COMMA));
		}
		return params;
	}

	/**
	 * 获取请求体<br>
	 * 调用该方法后，getParam方法将失效
	 * 
	 * @param request {@link ServletRequest}
	 * @return 获得请求体
	 * @since 4.0.2
	 */
	public static String getBody(ServletRequest request) {
		try {
			return IoUtil.read(request.getReader());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取请求体byte[]<br>
	 * 调用该方法后，getParam方法将失效
	 * 
	 * @param request {@link ServletRequest}
	 * @return 获得请求体byte[]
	 * @since 4.0.2
	 */
	public static byte[] getBodyBytes(ServletRequest request) {
		try {
			return IoUtil.readBytes(request.getInputStream());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	// --------------------------------------------------------- getParam end

	// --------------------------------------------------------- fillBean start
	/**
	 * ServletRequest 参数转Bean
	 * 
	 * @param <T> Bean类型
	 * @param request ServletRequest
	 * @param bean Bean
	 * @param copyOptions 注入时的设置
	 * @return Bean
	 * @since 3.0.4
	 */
	public static <T> T fillBean(final ServletRequest request, T bean, CopyOptions copyOptions) {
		final String beanName = StrUtil.lowerFirst(bean.getClass().getSimpleName());
		return BeanUtil.fillBean(bean, new ValueProvider<String>() {
			@Override
			public Object value(String key, Type valueType) {
				String value = request.getParameter(key);
				if (StrUtil.isEmpty(value)) {
					// 使用类名前缀尝试查找值
					value = request.getParameter(beanName + StrUtil.DOT + key);
					if (StrUtil.isEmpty(value)) {
						// 此处取得的值为空时跳过，包括null和""
						value = null;
					}
				}
				return value;
			}

			@Override
			public boolean containsKey(String key) {
				// 对于Servlet来说，返回值null意味着无此参数
				return (null != request.getParameter(key)) || (null != request.getParameter(beanName + StrUtil.DOT + key));
			}
		}, copyOptions);
	}

	/**
	 * ServletRequest 参数转Bean
	 * 
	 * @param <T> Bean类型
	 * @param request {@link ServletRequest}
	 * @param bean Bean
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T fillBean(ServletRequest request, T bean, boolean isIgnoreError) {
		return fillBean(request, bean, CopyOptions.create().setIgnoreError(isIgnoreError));
	}

	/**
	 * ServletRequest 参数转Bean
	 * 
	 * @param <T> Bean类型
	 * @param request ServletRequest
	 * @param beanClass Bean Class
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T toBean(ServletRequest request, Class<T> beanClass, boolean isIgnoreError) {
		return fillBean(request, ReflectUtil.newInstance(beanClass), isIgnoreError);
	}
	// --------------------------------------------------------- fillBean end

	/**
	 * 获取客户端IP<br>
	 * 默认检测的Header：<br>
	 * 1、X-Forwarded-For<br>
	 * 2、X-Real-IP<br>
	 * 3、Proxy-Client-IP<br>
	 * 4、WL-Proxy-Client-IP<br>
	 * otherHeaderNames参数用于自定义检测的Header
	 * 
	 * @param request 请求对象{@link HttpServletRequest}
	 * @param otherHeaderNames 其他自定义头文件
	 * @return IP地址
	 */
	public static String getClientIP(HttpServletRequest request, String... otherHeaderNames) {
		String[] headers = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };
		if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
			headers = ArrayUtil.addAll(headers, otherHeaderNames);
		}

		String ip;
		for (String header : headers) {
			ip = request.getHeader(header);
			if (false == isUnknow(ip)) {
				return getMultistageReverseProxyIp(ip);
			}
		}

		ip = request.getRemoteAddr();
		return getMultistageReverseProxyIp(ip);
	}

	/**
	 * 获得MultiPart表单内容，多用于获得上传的文件 在同一次请求中，此方法只能被执行一次！
	 * 
	 * @param request {@link ServletRequest}
	 * @return MultipartFormData
	 * @throws IORuntimeException IO异常
	 * @since 4.0.2
	 */
	public static MultipartFormData getMultipart(ServletRequest request) throws IORuntimeException {
		return getMultipart(request, new UploadSetting());
	}

	/**
	 * 获得multipart/form-data 表单内容<br>
	 * 包括文件和普通表单数据<br>
	 * 在同一次请求中，此方法只能被执行一次！
	 * 
	 * @param request {@link ServletRequest}
	 * @param uploadSetting 上传文件的设定，包括最大文件大小、保存在内存的边界大小、临时目录、扩展名限定等
	 * @return MultiPart表单
	 * @throws IORuntimeException IO异常
	 * @since 4.0.2
	 */
	public static MultipartFormData getMultipart(ServletRequest request, UploadSetting uploadSetting) throws IORuntimeException {
		final MultipartFormData formData = new MultipartFormData(uploadSetting);
		try {
			formData.parseRequest(request);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		return formData;
	}

	// --------------------------------------------------------- Header start
	/**
	 * 忽略大小写获得请求header中的信息
	 * 
	 * @param request 请求对象{@link HttpServletRequest}
	 * @param nameIgnoreCase 忽略大小写头信息的KEY
	 * @return header值
	 */
	public final static String getHeaderIgnoreCase(HttpServletRequest request, String nameIgnoreCase) {
		Enumeration<String> names = request.getHeaderNames();
		String name = null;
		while (names.hasMoreElements()) {
			name = names.nextElement();
			if (name != null && name.equalsIgnoreCase(nameIgnoreCase)) {
				return request.getHeader(name);
			}
		}

		return null;
	}

	/**
	 * 获得请求header中的信息
	 * 
	 * @param request 请求对象{@link HttpServletRequest}
	 * @param name 头信息的KEY
	 * @param charset 字符集
	 * @return header值
	 */
	public final static String getHeader(HttpServletRequest request, String name, String charset) {
		final String header = request.getHeader(name);
		if (null != header) {
			try {
				return new String(header.getBytes(CharsetUtil.ISO_8859_1), charset);
			} catch (UnsupportedEncodingException e) {
				throw new UtilException(StrUtil.format("Error charset {} for http request header.", charset));
			}
		}
		return null;
	}

	/**
	 * 客户浏览器是否为IE
	 * 
	 * @param request 请求对象{@link HttpServletRequest}
	 * @return 客户浏览器是否为IE
	 */
	public static boolean isIE(HttpServletRequest request) {
		String userAgent = getHeaderIgnoreCase(request, "User-Agent");
		if (StrUtil.isNotBlank(userAgent)) {
			userAgent = userAgent.toUpperCase();
			if (userAgent.contains("MSIE") || userAgent.contains("TRIDENT")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否为GET请求
	 * 
	 * @param request 请求对象{@link HttpServletRequest}
	 * @return 是否为GET请求
	 */
	public static boolean isGetMethod(HttpServletRequest request) {
		return METHOD_GET.equalsIgnoreCase(request.getMethod());
	}

	/**
	 * 是否为POST请求
	 * 
	 * @param request 请求对象{@link HttpServletRequest}
	 * @return 是否为POST请求
	 */
	public static boolean isPostMethod(HttpServletRequest request) {
		return METHOD_POST.equalsIgnoreCase(request.getMethod());
	}

	/**
	 * 是否为Multipart类型表单，此类型表单用于文件上传
	 * 
	 * @param request 请求对象{@link HttpServletRequest}
	 * @return 是否为Multipart类型表单，此类型表单用于文件上传
	 */
	public static boolean isMultipart(HttpServletRequest request) {
		if (false == isPostMethod(request)) {
			return false;
		}

		String contentType = request.getContentType();
		if (StrUtil.isBlank(contentType)) {
			return false;
		}
		if (contentType.toLowerCase().startsWith("multipart/")) {
			return true;
		}

		return false;
	}
	// --------------------------------------------------------- Header end

	// --------------------------------------------------------- Cookie start
	/**
	 * 获得指定的Cookie
	 * 
	 * @param httpServletRequest {@link HttpServletRequest}
	 * @param name cookie名
	 * @return Cookie对象
	 */
	public final static Cookie getCookie(HttpServletRequest httpServletRequest, String name) {
		final Map<String, Cookie> cookieMap = readCookieMap(httpServletRequest);
		return cookieMap == null ? null : cookieMap.get(name);
	}

	/**
	 * 将cookie封装到Map里面
	 * 
	 * @param httpServletRequest {@link HttpServletRequest}
	 * @return Cookie map
	 */
	public final static Map<String, Cookie> readCookieMap(HttpServletRequest httpServletRequest) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = httpServletRequest.getCookies();
		if (null == cookies) {
			return null;
		}
		for (Cookie cookie : cookies) {
			cookieMap.put(cookie.getName().toLowerCase(), cookie);
		}
		return cookieMap;
	}

	/**
	 * 设定返回给客户端的Cookie
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @param cookie Servlet Cookie对象
	 */
	public final static void addCookie(HttpServletResponse response, Cookie cookie) {
		response.addCookie(cookie);
	}

	/**
	 * 设定返回给客户端的Cookie
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @param name Cookie名
	 * @param value Cookie值
	 */
	public final static void addCookie(HttpServletResponse response, String name, String value) {
		response.addCookie(new Cookie(name, value));
	}

	/**
	 * 设定返回给客户端的Cookie
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @param name cookie名
	 * @param value cookie值
	 * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. &gt;0 : Cookie存在的秒数.
	 * @param path Cookie的有效路径
	 * @param domain the domain name within which this cookie is visible; form is according to RFC 2109
	 */
	public final static void addCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds, String path, String domain) {
		Cookie cookie = new Cookie(name, value);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setPath(path);
		addCookie(response, cookie);
	}

	/**
	 * 设定返回给客户端的Cookie<br>
	 * Path: "/"<br>
	 * No Domain
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @param name cookie名
	 * @param value cookie值
	 * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. &gt;0 : Cookie存在的秒数.
	 */
	public final static void addCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		addCookie(response, name, value, maxAgeInSeconds, "/", null);
	}

	// --------------------------------------------------------- Cookie end
	// --------------------------------------------------------- Response start
	/**
	 * 获得PrintWriter
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @return 获得PrintWriter
	 * @throws IORuntimeException IO异常
	 */
	public static PrintWriter getWriter(HttpServletResponse response) throws IORuntimeException {
		try {
			return response.getWriter();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 返回数据给客户端
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @param text 返回的内容
	 * @param contentType 返回的类型
	 */
	public static void write(HttpServletResponse response, String text, String contentType) {
		response.setContentType(contentType);
		Writer writer = null;
		try {
			writer = response.getWriter();
			writer.write(text);
			writer.flush();
		} catch (IOException e) {
			throw new UtilException(e);
		} finally {
			IoUtil.close(writer);
		}
	}

	/**
	 * 返回文件给客户端
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @param file 写出的文件对象
	 * @since 4.1.15
	 */
	public static void write(HttpServletResponse response, File file) {
		final String fileName = file.getName();
		final String contentType = ObjectUtil.defaultIfNull(FileUtil.getMimeType(fileName), "application/octet-stream");
		BufferedInputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			write(response, in, contentType, fileName);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 返回数据给客户端
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @param in 需要返回客户端的内容
	 * @param contentType 返回的类型
	 * @param fileName 文件名
	 * @since 4.1.15
	 */
	public static void write(HttpServletResponse response, InputStream in, String contentType, String fileName) {
		final String charset = ObjectUtil.defaultIfNull(response.getCharacterEncoding(), CharsetUtil.UTF_8);
		response.setHeader("Content-Disposition", StrUtil.format("attachment;filename={}", URLUtil.encode(fileName, charset)));
		response.setContentType(contentType);
		write(response, in);
	}

	/**
	 * 返回数据给客户端
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @param in 需要返回客户端的内容
	 * @param contentType 返回的类型
	 */
	public static void write(HttpServletResponse response, InputStream in, String contentType) {
		response.setContentType(contentType);
		write(response, in);
	}

	/**
	 * 返回数据给客户端
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @param in 需要返回客户端的内容
	 */
	public static void write(HttpServletResponse response, InputStream in) {
		write(response, in, IoUtil.DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 返回数据给客户端
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @param in 需要返回客户端的内容
	 * @param bufferSize 缓存大小
	 */
	public static void write(HttpServletResponse response, InputStream in, int bufferSize) {
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			IoUtil.copy(in, out, bufferSize);
		} catch (IOException e) {
			throw new UtilException(e);
		} finally {
			IoUtil.close(out);
			IoUtil.close(in);
		}
	}

	/**
	 * 设置响应的Header
	 * 
	 * @param response 响应对象{@link HttpServletResponse}
	 * @param name 名
	 * @param value 值，可以是String，Date， int
	 */
	public static void setHeader(HttpServletResponse response, String name, Object value) {
		if (value instanceof String) {
			response.setHeader(name, (String) value);
		} else if (Date.class.isAssignableFrom(value.getClass())) {
			response.setDateHeader(name, ((Date) value).getTime());
		} else if (value instanceof Integer || "int".equals(value.getClass().getSimpleName().toLowerCase())) {
			response.setIntHeader(name, (Integer) value);
		} else {
			response.setHeader(name, value.toString());
		}
	}
	// --------------------------------------------------------- Response end

	// --------------------------------------------------------- Private methd start
	/**
	 * 从多级反向代理中获得第一个非unknown IP地址
	 * 
	 * @param ip 获得的IP地址
	 * @return 第一个非unknown IP地址
	 */
	private static String getMultistageReverseProxyIp(String ip) {
		// 多级反向代理检测
		if (ip != null && ip.indexOf(",") > 0) {
			final String[] ips = ip.trim().split(",");
			for (String subIp : ips) {
				if (false == isUnknow(subIp)) {
					ip = subIp;
					break;
				}
			}
		}
		return ip;
	}

	/**
	 * 检测给定字符串是否为未知，多用于检测HTTP请求相关<br>
	 * 
	 * @param checkString 被检测的字符串
	 * @return 是否未知
	 */
	private static boolean isUnknow(String checkString) {
		return StrUtil.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString);
	}
	// --------------------------------------------------------- Private methd end

}
