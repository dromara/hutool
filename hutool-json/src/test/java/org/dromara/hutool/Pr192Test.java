package org.dromara.hutool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class Pr192Test {

	@Test
	public void toBeanTest3() {
		//		测试数字类型精度丢失的情况
		final String number = "1234.123456789123456";
		final String jsonString = "{\"create\":{\"details\":[{\"price\":" + number + "}]}}";
		final WebCreate create = JSONUtil.toBean(jsonString, WebCreate.class);
		Assertions.assertEquals(number,create.getCreate().getDetails().get(0).getPrice().toString());
	}

	static class WebCreate {
		private Create create;
		@Override
		public String toString() {
			return "WebCreate{" +
					"create=" + create +
					'}';
		}

		public void setCreate(final Create create) {
			this.create = create;
		}

		public Create getCreate() {
			return create;
		}
	}

	static class Create {
		@Override
		public String toString() {
			return "Create{" +
					"details=" + details +
					'}';
		}

		private List<Detail> details;

		public void setDetails(final List<Detail> details) {
			this.details = details;
		}

		public List<Detail> getDetails() {
			return details;
		}
	}

	static class Detail {
		private BigDecimal price;

		public void setPrice(final BigDecimal price) {
			this.price = price;
		}

		@Override
		public String toString() {
			return "Detail{" +
					"price=" + price +
					'}';
		}

		public BigDecimal getPrice() {
			return price;
		}
	}
}
