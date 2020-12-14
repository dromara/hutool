package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * https://gitee.com/loolly/dashboard/issues?id=I1F8M2
 */
public class IssueI1F8M2 {
	@Test
	public void toBeanTest() {
		String jsonStr = "{\"eventType\":\"fee\",\"fwdAlertingTime\":\"2020-04-22 16:34:13\",\"fwdAnswerTime\":\"\"}";
		Param param = JSONUtil.toBean(jsonStr, Param.class);
		Assert.assertEquals("2020-04-22T16:34:13", param.getFwdAlertingTime().toString());
		Assert.assertNull(param.getFwdAnswerTime());
	}

	// Param类的字段
	@Data
	static class Param {
		/**
		 * fee表示话单事件
		 */
		private String eventType;
		/**
		 * 转接呼叫后振铃时间
		 */
		private LocalDateTime fwdAlertingTime;
		/**
		 * 转接呼叫后应答时间
		 */
		private LocalDateTime fwdAnswerTime;

	}
}
