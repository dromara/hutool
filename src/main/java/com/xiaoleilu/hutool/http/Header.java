package com.xiaoleilu.hutool.http;

/**
 * Http Header
 * @author Looly
 *
 */
public enum Header {
	/**Accept*/
	ACCEPT("Accept"),
	/**Accept-Encoding*/
	ACCEPT_ENCODING("Accept-Encoding"),
	/**User-Agent*/
	USER_AGENT("User-Agent"),
	/**Content-Type*/
	CONTENT_TYPE("Content-Type"),
	/**Content-Length*/
	CONTENT_LENGTH("Content-Length"),
	/**Content-Encoding*/
	CONTENT_ENCODING("Content-Encoding"),
	/**Host*/
	HOST("Host"),
	/**ETag*/
	ETAG("ETag"),
	/**Connection*/
	CONNECTION("Connection"),
	/**Keep-Alive*/
	KEEP_ALIVE("Keep-Alive"),
	/**Cookie*/
	COOKIE("Cookie"),
	/**Cookie*/
	SET_COOKIE("Set-Cookie"),
	/**Close*/
	CLOSE("Close");
	
	private String value;
	private Header(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
