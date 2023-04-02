package org.dromara.hutool;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class Issue2447Test {

	@Test
	public void addIntegerTest() {
		Time time = new Time();
		time.setTime(LocalDateTime.of(1970, 1, 2, 10, 0, 1, 0));
		String timeStr = JSONUtil.toJsonStr(time);
		Assertions.assertEquals(timeStr, "{\"time\":93601000}");
		Assertions.assertEquals(JSONUtil.toBean(timeStr, Time.class).getTime(), time.getTime());
	}

	@Data
	static class Time {
		private LocalDateTime time;

		@Override
		public String toString() {
			return time.toString();
		}
	}
}
