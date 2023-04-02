package org.dromara.hutool.client.body;

import org.dromara.hutool.io.resource.HttpResource;
import org.dromara.hutool.io.resource.StringResource;
import org.dromara.hutool.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MultipartBodyTest {

	@Test
	public void buildTest(){
		final Map<String, Object> form = new HashMap<>();
		form.put("pic1", "pic1 content");
		form.put("pic2", new HttpResource(
				new StringResource("pic2 content"), "text/plain"));
		form.put("pic3", new HttpResource(
				new StringResource("pic3 content", "pic3.jpg"), "image/jpeg"));

		final MultipartBody body = MultipartBody.of(form, CharsetUtil.UTF_8);

		Assertions.assertNotNull(body.toString());
//		Console.log(body);
	}
}
