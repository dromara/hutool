package cn.hutool.http;

/**
 * 返回状态码
 * @author Looly
 *
 */
interface Status {
	/**
	 * HTTP Status-Code 200: OK.
	 */
	int HTTP_OK = 200;

	/**
	 * HTTP Status-Code 201: Created.
	 */
	int HTTP_CREATED = 201;

	/**
	 * HTTP Status-Code 202: Accepted.
	 */
	int HTTP_ACCEPTED = 202;

	/**
	 * HTTP Status-Code 203: Non-Authoritative Information.
	 */
	int HTTP_NOT_AUTHORITATIVE = 203;

	/**
	 * HTTP Status-Code 204: No Content.
	 */
	int HTTP_NO_CONTENT = 204;

	/**
	 * HTTP Status-Code 205: Reset Content.
	 */
	int HTTP_RESET = 205;

	/**
	 * HTTP Status-Code 206: Partial Content.
	 */
	int HTTP_PARTIAL = 206;

	/* 3XX: relocation/redirect */

	/**
	 * HTTP Status-Code 300: Multiple Choices.
	 */
	int HTTP_MULT_CHOICE = 300;

	/**
	 * HTTP Status-Code 301: Moved Permanently.
	 */
	int HTTP_MOVED_PERM = 301;

	/**
	 * HTTP Status-Code 302: Temporary Redirect.
	 */
	int HTTP_MOVED_TEMP = 302;

	/**
	 * HTTP Status-Code 303: See Other.
	 */
	int HTTP_SEE_OTHER = 303;

	/**
	 * HTTP Status-Code 304: Not Modified.
	 */
	int HTTP_NOT_MODIFIED = 304;

	/**
	 * HTTP Status-Code 305: Use Proxy.
	 */
	int HTTP_USE_PROXY = 305;

	/* 4XX: client error */

	/**
	 * HTTP Status-Code 400: Bad Request.
	 */
	int HTTP_BAD_REQUEST = 400;

	/**
	 * HTTP Status-Code 401: Unauthorized.
	 */
	int HTTP_UNAUTHORIZED = 401;

	/**
	 * HTTP Status-Code 402: Payment Required.
	 */
	int HTTP_PAYMENT_REQUIRED = 402;

	/**
	 * HTTP Status-Code 403: Forbidden.
	 */
	int HTTP_FORBIDDEN = 403;

	/**
	 * HTTP Status-Code 404: Not Found.
	 */
	int HTTP_NOT_FOUND = 404;

	/**
	 * HTTP Status-Code 405: Method Not Allowed.
	 */
	int HTTP_BAD_METHOD = 405;

	/**
	 * HTTP Status-Code 406: Not Acceptable.
	 */
	int HTTP_NOT_ACCEPTABLE = 406;

	/**
	 * HTTP Status-Code 407: Proxy Authentication Required.
	 */
	int HTTP_PROXY_AUTH = 407;

	/**
	 * HTTP Status-Code 408: Request Time-Out.
	 */
	int HTTP_CLIENT_TIMEOUT = 408;

	/**
	 * HTTP Status-Code 409: Conflict.
	 */
	int HTTP_CONFLICT = 409;

	/**
	 * HTTP Status-Code 410: Gone.
	 */
	int HTTP_GONE = 410;

	/**
	 * HTTP Status-Code 411: Length Required.
	 */
	int HTTP_LENGTH_REQUIRED = 411;

	/**
	 * HTTP Status-Code 412: Precondition Failed.
	 */
	int HTTP_PRECON_FAILED = 412;

	/**
	 * HTTP Status-Code 413: Request Entity Too Large.
	 */
	int HTTP_ENTITY_TOO_LARGE = 413;

	/**
	 * HTTP Status-Code 414: Request-URI Too Large.
	 */
	int HTTP_REQ_TOO_LONG = 414;

	/**
	 * HTTP Status-Code 415: Unsupported Media Type.
	 */
	int HTTP_UNSUPPORTED_TYPE = 415;

	/* 5XX: server error */

	/**
	 * HTTP Status-Code 500: Internal Server Error.
	 */
	int HTTP_INTERNAL_ERROR = 500;

	/**
	 * HTTP Status-Code 501: Not Implemented.
	 */
	int HTTP_NOT_IMPLEMENTED = 501;

	/**
	 * HTTP Status-Code 502: Bad Gateway.
	 */
	int HTTP_BAD_GATEWAY = 502;

	/**
	 * HTTP Status-Code 503: Service Unavailable.
	 */
	int HTTP_UNAVAILABLE = 503;

	/**
	 * HTTP Status-Code 504: Gateway Timeout.
	 */
	int HTTP_GATEWAY_TIMEOUT = 504;

	/**
	 * HTTP Status-Code 505: HTTP Version Not Supported.
	 */
	int HTTP_VERSION = 505;
}
