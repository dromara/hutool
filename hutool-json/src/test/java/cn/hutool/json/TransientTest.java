package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class TransientTest {

	@Data
	static class Bill{
		private transient String id;
		private String bizNo;
	}

	@Test
	public void beanWithTransientTest(){
		Bill detailBill = new Bill();
		detailBill.setId("3243");
		detailBill.setBizNo("bizNo");

		final JSONObject jsonObject = new JSONObject(detailBill,
				JSONConfig.create().setIgnoreTransient(true));
		Assert.assertEquals("{\"bizNo\":\"bizNo\"}", jsonObject.toString());
	}
}
