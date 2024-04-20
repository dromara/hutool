package org.dromara.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class IssueI9HQQETest {

	@Test
	public void toBeanTest(){
		final String json = "{\"currentDate\":\"18/04/2024\", \"currentDateTime\":\"18/04/2024\"}";
		final JSONConfig jsonConfig = new JSONConfig();
		jsonConfig.setDateFormat("dd/MM/yyyy");

		final JSONObject entries = JSONUtil.parseObj(json, jsonConfig);

		final MMHBo mmh = entries.toBean(MMHBo.class);
		Assertions.assertNotNull(mmh.getCurrentDate());
		Assertions.assertNotNull(mmh.getCurrentDateTime());
	}

	@Data
	public static class MMHBo {
		/**
		 * 当前时间
		 */
		private LocalDate currentDate;
		private LocalDateTime currentDateTime;
	}
}
