package cn.hutool.http.webservice;

import javax.xml.soap.SOAPConstants;

/**
 * SOAP协议版本枚举
 * 
 * @author looly
 *
 */
public enum SoapProtocol {
	/** SOAP 1.1协议 */
	SOAP_1_1(SOAPConstants.SOAP_1_1_PROTOCOL),
	/** SOAP 1.2协议 */
	SOAP_1_2(SOAPConstants.SOAP_1_2_PROTOCOL);

	/**
	 * 构造
	 * 
	 * @param value {@link SOAPConstants} 中的协议版本值
	 */
	SoapProtocol(String value) {
		this.value = value;
	}

	private final String value;

	/**
	 * 获取版本值信息
	 * 
	 * @return 版本值信息
	 */
	public String getValue() {
		return this.value;
	}
}
