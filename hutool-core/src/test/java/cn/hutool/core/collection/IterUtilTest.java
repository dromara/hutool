package cn.hutool.core.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

/**
 * {@link IterUtil} 单元测试
 * @author looly
 *
 */
public class IterUtilTest {
	
	@Test
	public void fieldValueMapTest() {
		ArrayList<Car> carList = CollUtil.newArrayList(new Car("123", "大众"), new Car("345", "奔驰"), new Car("567", "路虎"));
		Map<String, Car> carNameMap = IterUtil.fieldValueMap(carList.iterator(), "carNumber");

		Assert.assertEquals("大众", carNameMap.get("123").getCarName());
		Assert.assertEquals("奔驰", carNameMap.get("345").getCarName());
		Assert.assertEquals("路虎", carNameMap.get("567").getCarName());
	}

	@Test
	public void joinTest() {
		ArrayList<String> list = CollUtil.newArrayList("1", "2", "3", "4");
		String join = IterUtil.join(list.iterator(), ":");
		Assert.assertEquals("1:2:3:4", join);
		
		ArrayList<Integer> list1 = CollUtil.newArrayList(1, 2, 3, 4);
		String join1 = IterUtil.join(list1.iterator(), ":");
		Assert.assertEquals("1:2:3:4", join1);
		
		ArrayList<String> list2 = CollUtil.newArrayList("1", "2", "3", "4");
		String join2 = IterUtil.join(list2.iterator(), ":", "\"", "\"");
		Assert.assertEquals("\"1\":\"2\":\"3\":\"4\"", join2);
	}

	public static class Car {
		private String carNumber;
		private String carName;

		public Car(String carNumber, String carName) {
			this.carNumber = carNumber;
			this.carName = carName;
		}

		public String getCarNumber() {
			return carNumber;
		}

		public void setCarNumber(String carNumber) {
			this.carNumber = carNumber;
		}

		public String getCarName() {
			return carName;
		}

		public void setCarName(String carName) {
			this.carName = carName;
		}
	}
}
