package cn.hutool.http.webservice;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;

/**
 * SOAP请求对象，包括请求地址、方法、参数等信息
 * 
 * @author looly
 * @since 4.1.8
 */
public class SoapRequest {
	
	private static final String TEXT_XML_CONTENT_TYPE = "text/xml;charset=";
	
	/** 编码 */
	private Charset charset = CharsetUtil.CHARSET_UTF_8;
	/** Webservice请求地址 */
	private String url;
	/** 方法的命名空间 */
	private String methodNamespace;
	/** 命名空间envelope(ns) */
	private String xmlns = "soapenv";
	/** 请求方法 */
	private String method;
	/** 请求参数 */
	private Map<String, String> params;
	
	/**
	 * 构造
	 * 
	 * @param url Webservice请求地址
	 * @param methodNamespace 命名空间
	 */
	public SoapRequest(String url, String methodNamespace) {
		this.url = url;
		this.methodNamespace = methodNamespace;
	}
	
	/**
	 * 设置编码
	 * @param charset 编码
	 * @return this
	 */
	public SoapRequest setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 设置Webservice请求地址
	 * @param url Webservice请求地址
	 * @return this
	 */
	public SoapRequest setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * 设置方法的命名空间
	 * @param namespace 命名空间
	 * @return this
	 */
	public SoapRequest setMethodNamespace(String namespace) {
		this.methodNamespace = namespace;
		return this;
	}
	
	/**
	 * 设置命名空间envelope(ns)
	 * @param namespace 命名空间
	 * @return this
	 */
	public SoapRequest setXmlns(String xmlns) {
		this.xmlns = xmlns;
		return this;
	}

	/**
	 * 设置请求方法
	 * @param method 请求方法
	 * @return this
	 */
	public SoapRequest setMethod(String method) {
		this.method = method;
		return this;
	}

	/**
	 * 设置 请求参数
	 * @param params 请求参数
	 * @return this
	 */
	public SoapRequest setParams(Map<String, String> params) {
		this.params = params;
		return this;
	}
	
	/**
	 * 增加请求参数
	 * @param name 参数名
	 * @param value 参数值
	 * @return this
	 */
	public SoapRequest addParam(String name, String value) {
		Map<String, String> params = this.params;
		if(null == params) {
			params = new LinkedHashMap<>();
			this.params = params;
		}
		params.put(name, value);
		return this;
	}
	
	/**
	 * 执行Webservice请求，既发送SOAP内容
	 * @return 返回结果
	 */
	public String execute() {
		return HttpRequest.post(this.url).body(toSoapXml()).contentType(getXmlContentType()).execute().body();
	}
	
	//------------------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 生成SOAP请求的XML文本
	 * @return SOAP请求的XML文本
	 */
	private String toSoapXml() {
		StringBuilder sb = StrUtil.builder();
		sb.append(StrUtil.format("<{}:Envelope xmlns:{}=\"http://schemas.xmlsoap.org/soap/envelope/\">\n", this.xmlns, this.xmlns));
		sb.append(StrUtil.format("  <{}:Body>\n", this.xmlns));
		// 传入method和namespace
		Assert.notBlank(this.method, "Method must be not blank !");
		sb.append("    <").append(this.method).append(" xmlns=\"").append(this.methodNamespace).append("\">\n");
		// 动态构造参数和值
		if(null != this.params) {
			for (Map.Entry<String, String> entry : this.params.entrySet()) {
				sb.append("      <").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">\n");
			}
		}
		sb.append("    </").append(this.method).append(">\n");
		sb.append(StrUtil.format("  </{}:Body>\n", this.xmlns));
		sb.append(StrUtil.format("</{}:Envelope>", this.xmlns));
		
		return sb.toString();
	}
	
	/**
	 * 获取请求的Content-Type，附加编码信息
	 * @return 请求的Content-Type
	 */
	private String getXmlContentType() {
		return TEXT_XML_CONTENT_TYPE.concat(this.charset.toString());
	}
	//------------------------------------------------------------------------------------------------------------------------- Private method end
}
