/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;

/**
 * <a href="https://github.com/dromara/hutool/issues/2090">https://github.com/dromara/hutool/issues/2090</a>
 */
public class Issue2090Test {

	@Test
	public void parseTest(){
		final TestBean test = new TestBean();
		test.setLocalDate(LocalDate.now());

		final JSONObject json = JSONUtil.parseObj(test);
		final TestBean test1 = json.toBean(TestBean.class);
		Assertions.assertEquals(test, test1);
	}

	@Test
	public void parseLocalDateTest(){
		final LocalDate localDate = LocalDate.now();
		final JSONObject jsonObject = JSONUtil.parseObj(localDate);
		Assertions.assertNotNull(jsonObject.toString());
	}

	@Test
	public void toBeanLocalDateTest(){
		final LocalDate d = LocalDate.now();
		final JSONObject obj = JSONUtil.parseObj(d);
		final LocalDate d2 = obj.toBean(LocalDate.class);
		Assertions.assertEquals(d, d2);
	}

	@Test
	public void toBeanLocalDateTimeTest(){
		final LocalDateTime d = LocalDateTime.now();
		final JSONObject obj = JSONUtil.parseObj(d);
		final LocalDateTime d2 = obj.toBean(LocalDateTime.class);
		Assertions.assertEquals(d, d2);
	}

	@Test
	public void toBeanLocalTimeTest(){
		final LocalTime d = LocalTime.now();
		final JSONObject obj = JSONUtil.parseObj(d);
		final LocalTime d2 = obj.toBean(LocalTime.class);
		Assertions.assertEquals(d, d2);
	}

	@Test
	public void monthTest(){
		final JSONObject jsonObject = new JSONObject();
		jsonObject.putObj("month", Month.JANUARY);
		Assertions.assertEquals("{\"month\":1}", jsonObject.toString());

		final JSON parse = JSONUtil.parse(Month.JANUARY);
		Assertions.assertInstanceOf(JSONPrimitive.class, parse);
		Assertions.assertTrue(((JSONPrimitive) parse).isNumber());
		Assertions.assertEquals("1", parse.toString());
	}

	@Test
	public void weekTest(){
		final JSONObject jsonObject = new JSONObject();
		jsonObject.putObj("week", DayOfWeek.SUNDAY);
		Assertions.assertEquals("{\"week\":7}", jsonObject.toString());

		final JSON parse = JSONUtil.parse(DayOfWeek.SUNDAY);
		Assertions.assertInstanceOf(JSONPrimitive.class, parse);
		Assertions.assertTrue(((JSONPrimitive) parse).isNumber());
		Assertions.assertEquals("7", parse.toString());
	}

	@Data
	public static class TestBean{
		private LocalDate localDate;
	}
}
