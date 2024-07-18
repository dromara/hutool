package org.dromara.hutool.http.client;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.http.meta.HeaderName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

public class Issue3658Test {

	@Test
	public void name() {
		// 请注意这里一定使用带form参数的get方法，参数随便设什么都行，url也随便
		final Request request = Request.of("https://timor.tech/api/holiday/year/2020-02")
			.form(Collections.singletonMap("11", "22"));

		final Collection<String> collection = request.headers().get(HeaderName.CONTENT_TYPE.getValue());
		final String s = CollUtil.get(collection, 0);
		Assertions.assertEquals("application/x-www-form-urlencoded;charset=UTF-8", s);
	}

}
