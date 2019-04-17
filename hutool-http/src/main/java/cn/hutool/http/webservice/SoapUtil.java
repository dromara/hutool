package cn.hutool.http.webservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import cn.hutool.core.util.XmlUtil;

/**
 * SOAP相关工具类
 * 
 * @author looly
 * @since 4.5.7
 */
public class SoapUtil {

	/**
	 * 创建SOAP客户端，默认使用soap1.1版本协议
	 * 
	 * @param url WS的URL地址
	 * @return {@link SoapClient}
	 */
	public static SoapClient createClient(String url) {
		return SoapClient.create(url);
	}

	/**
	 * 创建SOAP客户端
	 * 
	 * @param url WS的URL地址
	 * @param protocol 协议，见{@link SoapProtocol}
	 * @return {@link SoapClient}
	 */
	public static SoapClient createClient(String url, SoapProtocol protocol) {
		return SoapClient.create(url, protocol);
	}

	/**
	 * 创建SOAP客户端
	 * 
	 * @param url WS的URL地址
	 * @param protocol 协议，见{@link SoapProtocol}
	 * @param namespaceURI 方法上的命名空间URI
	 * @return {@link SoapClient}
	 * @since 4.5.6
	 */
	public static SoapClient createClient(String url, SoapProtocol protocol, String namespaceURI) {
		return SoapClient.create(url, protocol, namespaceURI);
	}

	/**
	 * {@link SOAPMessage} 转为字符串
	 * 
	 * @param message SOAP消息对象
	 * @param pretty 是否格式化
	 * @return SOAP XML字符串
	 */
	public static String toString(SOAPMessage message, boolean pretty) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			message.writeTo(out);
		} catch (SOAPException | IOException e) {
			throw new SoapRuntimeException(e);
		}
		return pretty ? XmlUtil.format(out.toString()) : out.toString();
	}
}
