package org.dromara.hutool;

import org.dromara.hutool.lang.Console;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

/**
 * <a href="https://github.com/dromara/hutool/issues/2090">https://github.com/dromara/hutool/issues/2090</a>
 */
public class Issue2090Test {

	@Test
	public void parseTest(){
		final TestBean test = new TestBean();
		test.setLocalDate(LocalDate.now());

		final JSONObject json = JSONUtil.parseObj(test);
		Console.log(json);
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
		jsonObject.set("month", Month.JANUARY);
		Assertions.assertEquals("{\"month\":1}", jsonObject.toString());
	}

	@Data
	public static class TestBean{
		private LocalDate localDate;
	}
}
