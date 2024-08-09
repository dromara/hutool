package cn.hutool.json;

import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TransientTest {

	@Data
	static class Bill{
		private transient String id;
		private String bizNo;
	}

	@Test
	public void beanWithoutTransientTest(){
		Bill detailBill = new Bill();
		detailBill.setId("3243");
		detailBill.setBizNo("bizNo");

		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject jsonObject = new JSONObject(detailBill,
				JSONConfig.create().setTransientSupport(false));
		assertEquals("{\"id\":\"3243\",\"bizNo\":\"bizNo\"}", jsonObject.toString());
	}

	@Test
	public void beanWithTransientTest(){
		Bill detailBill = new Bill();
		detailBill.setId("3243");
		detailBill.setBizNo("bizNo");

		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject jsonObject = new JSONObject(detailBill,
				JSONConfig.create().setTransientSupport(true));
		assertEquals("{\"bizNo\":\"bizNo\"}", jsonObject.toString());
	}

	@Test
	public void beanWithoutTransientToBeanTest(){
		Bill detailBill = new Bill();
		detailBill.setId("3243");
		detailBill.setBizNo("bizNo");

		final JSONObject jsonObject = new JSONObject(detailBill,
				JSONConfig.create().setTransientSupport(false));

		final Bill bill = jsonObject.toBean(Bill.class);
		assertEquals("3243", bill.getId());
		assertEquals("bizNo", bill.getBizNo());
	}

	@Test
	public void beanWithTransientToBeanTest(){
		Bill detailBill = new Bill();
		detailBill.setId("3243");
		detailBill.setBizNo("bizNo");

		final JSONObject jsonObject = new JSONObject(detailBill,
				JSONConfig.create().setTransientSupport(true));

		final Bill bill = jsonObject.toBean(Bill.class);
		assertNull(bill.getId());
		assertEquals("bizNo", bill.getBizNo());
	}
}
