package cn.hutool.core.bean;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.NamingCase;
import lombok.Data;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Issue2202Test {

	@Test
	public void toBeanWithFieldNameEditorTest(){
		Map<String, String> headerMap = new HashMap<>(5);
		headerMap.put("wechatpay-serial", "serial");
		headerMap.put("wechatpay-nonce", "nonce");
		headerMap.put("wechatpay-timestamp", "timestamp");
		headerMap.put("wechatpay-signature", "signature");
		ResponseSignVerifyParams case1 = BeanUtil.toBean(headerMap, ResponseSignVerifyParams.class, CopyOptions.create().setFieldNameEditor(field -> NamingCase.toCamelCase(field, '-')));
		Console.log(case1);
	}

	@Data
	static class ResponseSignVerifyParams {
		private String wechatpaySerial;
		private String wechatpaySignature;
		private String wechatpayTimestamp;
		private String wechatpayNonce;
		private String body;
	}
}
