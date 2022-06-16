package cn.hutool.core.util;


import cn.hutool.core.exceptions.CloneRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultCloneTest {

	@Test
	public void clone0() {
		final Car oldCar = new Car();
		oldCar.setId(1);
		oldCar.setWheelList(Stream.of(new Wheel("h")).collect(Collectors.toList()));

		final Car newCar = oldCar.clone();
		Assert.assertEquals(oldCar.getId(), newCar.getId());
		Assert.assertEquals(oldCar.getWheelList(), newCar.getWheelList());

		newCar.setId(2);
		Assert.assertNotEquals(oldCar.getId(), newCar.getId());
		newCar.getWheelList().add(new Wheel("s"));

		Assert.assertNotSame(oldCar, newCar);

	}

	@Data
	static class Car implements Cloneable {
		private Integer id;
		private List<Wheel> wheelList;

		@Override
		public Car clone() {
			try {
				return (Car) super.clone();
			} catch (final CloneNotSupportedException e) {
				throw new CloneRuntimeException(e);
			}
		}
	}

	@Data
	@AllArgsConstructor
	static class Wheel {
		private String direction;
	}

}


