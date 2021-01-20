package cn.hutool.json;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;

public class Issue488Test {

	@Test
	public void toBeanTest() {
		String jsonStr = ResourceUtil.readUtf8Str("issue488.json");
		
		ResultSuccess<List<EmailAddress>> result = JSONUtil.toBean(jsonStr,
				new TypeReference<ResultSuccess<List<EmailAddress>>>() {}, false);
		
		Assert.assertEquals("https://graph.microsoft.com/beta/$metadata#Collection(microsoft.graph.emailAddress)", result.getContext());
		
		List<EmailAddress> adds = result.getValue();
		Assert.assertEquals("会议室101", adds.get(0).getName());
		Assert.assertEquals("MeetingRoom101@abc.com", adds.get(0).getAddress());
		Assert.assertEquals("会议室102", adds.get(1).getName());
		Assert.assertEquals("MeetingRoom102@abc.com", adds.get(1).getAddress());
		Assert.assertEquals("会议室103", adds.get(2).getName());
		Assert.assertEquals("MeetingRoom103@abc.com", adds.get(2).getAddress());
		Assert.assertEquals("会议室219", adds.get(3).getName());
		Assert.assertEquals("MeetingRoom219@abc.com", adds.get(3).getAddress());
	}

	@Test
	public void toCollctionBeanTest() {
		String jsonStr = ResourceUtil.readUtf8Str("issue488Array.json");

		List<ResultSuccess<List<EmailAddress>>> resultList = JSONUtil.toBean(jsonStr,
				new TypeReference<List<ResultSuccess<List<EmailAddress>>>>() {}, false);

		ResultSuccess<List<EmailAddress>> result = resultList.get(0);

		Assert.assertEquals("https://graph.microsoft.com/beta/$metadata#Collection(microsoft.graph.emailAddress)", result.getContext());

		List<EmailAddress> adds = result.getValue();
		Assert.assertEquals("会议室101", adds.get(0).getName());
		Assert.assertEquals("MeetingRoom101@abc.com", adds.get(0).getAddress());
		Assert.assertEquals("会议室102", adds.get(1).getName());
		Assert.assertEquals("MeetingRoom102@abc.com", adds.get(1).getAddress());
		Assert.assertEquals("会议室103", adds.get(2).getName());
		Assert.assertEquals("MeetingRoom103@abc.com", adds.get(2).getAddress());
		Assert.assertEquals("会议室219", adds.get(3).getName());
		Assert.assertEquals("MeetingRoom219@abc.com", adds.get(3).getAddress());
	}

	@Data
	public static class ResultSuccess<T> {
		private String context;
		private T value;
	}

	@Data
	public static class EmailAddress {
		private String name;
		private String address;
	}
}
