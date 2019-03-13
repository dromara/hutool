package cn.hutool.http.test;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

/**
 * Rest类型请求单元测试
 * 
 * @author looly
 *
 */
public class RestTest {

	@Test
	public void contentTypeTest() {
		HttpRequest request = HttpRequest.post("http://localhost:8090/rest/restTest/")//
				.body(JSONUtil.createObj().put("aaa", "aaaValue").put("键2", "值2"));
		Assert.assertEquals("application/json;charset=UTF-8", request.header("Content-Type"));
	}

	@Test
	@Ignore
	public void postTest() {
		HttpRequest request = HttpRequest.post("http://localhost:8090/rest/restTest/")//
				.body(JSONUtil.createObj().put("aaa", "aaaValue").put("键2", "值2"));
		Console.log(request.execute().body());
	}

	@Test
	@Ignore
	public void postTest2() {
		String result = HttpUtil.post("http://localhost:8090/rest/restTest/", JSONUtil.createObj()//
				.put("aaa", "aaaValue").put("键2", "值2").toString());
		Console.log(result);
	}

	@Test
	@Ignore
	public void postTest3() {
		HttpRequest request = HttpRequest.post("http://211.162.39.204:8181/jeesite-simple/a/open/bizGwbnService/test")//
				.body(JSONUtil.createObj().put("aaa", "aaaValue").put("键2", "值2"));
		Console.log(request.execute().body());
	}

	@Test
//	@Ignore
	public void deleteTest() {
		final String TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjRDNTQwNzlDQTU4OUZDRUREMTg5NzYzRUZBQTU4MTUzNTQ5MUNCOEMiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJURlFIbktXSl9PM1JpWFktLXFXQlUxU1J5NHcifQ.eyJuYmYiOjE1NTIzNTc4MTksImV4cCI6MTU1MjUzMDYxOSwiaXNzIjoiaHR0cDovL29hdXRoOjQwMDAiLCJhdWQiOlsiaHR0cDovL29hdXRoOjQwMDAvcmVzb3VyY2VzIiwib3BlbmFwaSJdLCJjbGllbnRfaWQiOiJhcHAuc2RrLnJlZnJlc2giLCJzdWIiOiItMSIsImF1dGhfdGltZSI6MTU1MTgzNDI5NSwiaWRwIjoibG9jYWwiLCJlY29kZSI6IkUwMDE4OTc3IiwibG9naW5fbmFtZSI6IkUwMDE4OTc3IiwiaWRlbnRpZmljYXRpb24iOiIiLCJzY29wZSI6WyJvcGVuYXBpIiwib2ZmbGluZV9hY2Nlc3MiXSwiYW1yIjpbImVudGVycHJpc2VfYXBwIl19.PYp0Q6L8dCxj_T_22tVb_adc4e4eOZqMaOfO57rHseQokdETXADHGTQila8fIsH_HWrz1pjplfetBt4WMTIMxo6uG2SCYTLu0fukVf81c2BWCrrAigj_8crGuBlpcE_jUiWKUGOImEAwWTiHT5B6lG60buwdHwGiICt1RQX5-sCQCNl-LX6A1SohEmUMXs-azxTG_G2CnmyylFDqZv4tPvxvrd3gCTRYpxL9o77i45uhuTPcihX2CJlKXvt86JLE4UDTBeV_lj9djl22VqHpQCVC5uWToLLMRrnVO6kuejfmo3e50uCEVDHoFVVrVJkq17dAlBAkSVc2n7vs8MQbVVjERY0PhsEH5W65ZbPCZR2q8XEDXq_5sgO7WKDaC9koCwvBA6KH5CQGhPQSjrSbJZc6N8E2DPEgLk4Rjssg0FvSVdnf16B97R2lspyNEntkP26hw_6b3r4Rl5KEmNWfA4N4K9sWMs1HfMCh5AyelQeUT70WG06xBlDDF21h99ngShS6OwfZb-BHEc9RdHc7tSIpgPfsMHwyPBGPLechbUgHzZvZT3PBFxK4CkL88aOd1f4SkWlsyr93V2aiVngoIrvgApJDlKuAfjxdVLqs0AcQl17I1UScYOULTj78FLmeqI9BAYHMudhV227RBnb9fex_zWgC8gFb3DdusYSH7-4";
		final String TRUCKS_URL = "https://oapi-staging.alct56.com/api/v1/openapi/drivers/140624198907073517/trucks";

		String url = TRUCKS_URL + "?" + "licensePlateNo=鲁Q18863";
		Console.log(url);
		HttpRequest request = HttpRequest.delete(url)
				.setEncodeUrlParams(true)
				.header("Authorization", "Bearer " + TOKEN);
		Assert.assertEquals("{\"code\":\"OB050803\",\"message\":\"该车辆已被此司机用作税登\"}", request.execute().body());
	}
}
