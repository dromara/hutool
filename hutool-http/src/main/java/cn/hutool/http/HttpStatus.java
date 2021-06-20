package cn.hutool.http;

/**
 * HTTP状态码
 *
 * @author Looly
 * @see java.net.HttpURLConnection
 *
 */
public class HttpStatus {

	/* 2XX: generally "OK" */

	/**
	 * HTTP Status-Code 200: OK.
	 */
	public static final int HTTP_OK = 200;

	/**
	 * HTTP Status-Code 201: Created.
	 */
	public static final int HTTP_CREATED = 201;

	/**
	 * HTTP Status-Code 202: Accepted.
	 */
	public static final int HTTP_ACCEPTED = 202;

	/**
	 * HTTP Status-Code 203: Non-Authoritative Information.
	 */
	public static final int HTTP_NOT_AUTHORITATIVE = 203;

	/**
	 * HTTP Status-Code 204: No Content.
	 */
	public static final int HTTP_NO_CONTENT = 204;

	/**
	 * HTTP Status-Code 205: Reset Content.
	 */
	public static final int HTTP_RESET = 205;

	/**
	 * HTTP Status-Code 206: Partial Content.
	 */
	public static final int HTTP_PARTIAL = 206;

	/* 3XX: relocation/redirect */

	/**
	 * HTTP Status-Code 300: Multiple Choices.
	 */
	public static final int HTTP_MULT_CHOICE = 300;

	/**
	 * HTTP Status-Code 301: Moved Permanently.
	 */
	public static final int HTTP_MOVED_PERM = 301;

	/**
	 * HTTP Status-Code 302: Temporary Redirect.
	 */
	public static final int HTTP_MOVED_TEMP = 302;

	/**
	 * HTTP Status-Code 303: See Other.
	 */
	public static final int HTTP_SEE_OTHER = 303;

	/**
	 * HTTP Status-Code 304: Not Modified.
	 */
	public static final int HTTP_NOT_MODIFIED = 304;

	/**
	 * HTTP Status-Code 305: Use Proxy.
	 */
	public static final int HTTP_USE_PROXY = 305;

	/**
	 * HTTP 1.1 Status-Code 307: Temporary Redirect.<br>
	 * 见：RFC-7231
	 */
	public static final int HTTP_TEMP_REDIRECT = 307;

	/**
	 * HTTP 1.1 Status-Code 308: Permanent Redirect 永久重定向<br>
	 * 见：RFC-7231
	 */
	public static final int HTTP_PERMANENT_REDIRECT = 308;

	/* 4XX: client error */

	/**
	 * HTTP Status-Code 400: Bad Request.
	 */
	public static final int HTTP_BAD_REQUEST = 400;

	/**
	 * HTTP Status-Code 401: Unauthorized.
	 */
	public static final int HTTP_UNAUTHORIZED = 401;

	/**
	 * HTTP Status-Code 402: Payment Required.
	 */
	public static final int HTTP_PAYMENT_REQUIRED = 402;

	/**
	 * HTTP Status-Code 403: Forbidden.
	 */
	public static final int HTTP_FORBIDDEN = 403;

	/**
	 * HTTP Status-Code 404: Not Found.
	 */
	public static final int HTTP_NOT_FOUND = 404;

	/**
	 * HTTP Status-Code 405: Method Not Allowed.
	 */
	public static final int HTTP_BAD_METHOD = 405;

	/**
	 * HTTP Status-Code 406: Not Acceptable.
	 */
	public static final int HTTP_NOT_ACCEPTABLE = 406;

	/**
	 * HTTP Status-Code 407: Proxy Authentication Required.
	 */
	public static final int HTTP_PROXY_AUTH = 407;

	/**
	 * HTTP Status-Code 408: Request Time-Out.
	 */
	public static final int HTTP_CLIENT_TIMEOUT = 408;

	/**
	 * HTTP Status-Code 409: Conflict.
	 */
	public static final int HTTP_CONFLICT = 409;

	/**
	 * HTTP Status-Code 410: Gone.
	 */
	public static final int HTTP_GONE = 410;

	/**
	 * HTTP Status-Code 411: Length Required.
	 */
	public static final int HTTP_LENGTH_REQUIRED = 411;

	/**
	 * HTTP Status-Code 412: Precondition Failed.
	 */
	public static final int HTTP_PRECON_FAILED = 412;

	/**
	 * HTTP Status-Code 413: Request Entity Too Large.
	 */
	public static final int HTTP_ENTITY_TOO_LARGE = 413;

	/**
	 * HTTP Status-Code 414: Request-URI Too Large.
	 */
	public static final int HTTP_REQ_TOO_LONG = 414;

	/**
	 * HTTP Status-Code 415: Unsupported Media Type.
	 */
	public static final int HTTP_UNSUPPORTED_TYPE = 415;

	/* 5XX: server error */

	/**
	 * HTTP Status-Code 500: Internal Server Error.
	 */
	public static final int HTTP_INTERNAL_ERROR = 500;

	/**
	 * HTTP Status-Code 501: Not Implemented.
	 */
	public static final int HTTP_NOT_IMPLEMENTED = 501;

	/**
	 * HTTP Status-Code 502: Bad Gateway.
	 */
	public static final int HTTP_BAD_GATEWAY = 502;

	/**
	 * HTTP Status-Code 503: Service Unavailable.
	 */
	public static final int HTTP_UNAVAILABLE = 503;

	/**
	 * HTTP Status-Code 504: Gateway Timeout.
	 */
	public static final int HTTP_GATEWAY_TIMEOUT = 504;

	/**
	 * HTTP Status-Code 505: HTTP Version Not Supported.
	 */
	public static final int HTTP_VERSION = 505;

	/**
	 * 是否为重定向状态码
	 * @param responseCode 被检查的状态码
	 * @return 是否为重定向状态码
	 * @since 5.6.3
	 */
	public static boolean isRedirected(int responseCode){
		return responseCode == HTTP_MOVED_PERM
				|| responseCode == HTTP_MOVED_TEMP
				|| responseCode == HTTP_SEE_OTHER
				// issue#1504@Github，307和308是RFC 7538中http 1.1定义的规范
				|| responseCode == HTTP_TEMP_REDIRECT
				|| responseCode == HTTP_PERMANENT_REDIRECT;

	}
}
