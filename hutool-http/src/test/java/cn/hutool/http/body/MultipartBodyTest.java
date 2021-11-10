package cn.hutool.http.body;

import cn.hutool.core.io.resource.StringResource;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpResource;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MultipartBodyTest {

	@Test
	public void buildTest(){
		Map<String, Object> form = new HashMap<>();
		form.put("pic1", "pic1 content");
		form.put("pic2", new HttpResource(
				new StringResource("pic2 content"), "text/plain"));
		form.put("pic3", new HttpResource(
				new StringResource("pic3 content", "pic3.jpg"), "image/jpeg"));

		final MultipartBody body = MultipartBody.create(form, CharsetUtil.CHARSET_UTF_8);

		Assert.assertNotNull(body.toString());
//		Console.log(body);
	}
}
