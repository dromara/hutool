package cn.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Issue3790Test {
	@Test
	void bigDecimalToStringTest() {
		BigDecimal bigDecimal = new BigDecimal("0.01");
		bigDecimal = bigDecimal.setScale(4, RoundingMode.HALF_UP);

		Dto dto = new Dto();
		dto.remain = bigDecimal;

		final String jsonStr = JSONUtil.toJsonStr(dto, JSONConfig.create().setStripTrailingZeros(false));
		Assertions.assertEquals("{\"remain\":0.0100}", jsonStr);
	}

	static class Dto {
		public BigDecimal remain;
	}
}
