package cn.hutool.core.clone;


import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultCloneTest {

	@Test
	public void clone0() {
		Car oldCar = new Car();
		oldCar.setId(1);
		oldCar.setWheelList(Stream.of(new Wheel("h")).collect(Collectors.toList()));

		Car newCar = oldCar.clone0();
		Assert.assertEquals(oldCar.getId(),newCar.getId());
		Assert.assertEquals(oldCar.getWheelList(),newCar.getWheelList());

		newCar.setId(2);
		Assert.assertNotEquals(oldCar.getId(),newCar.getId());
		newCar.getWheelList().add(new Wheel("s"));

		Assert.assertNotSame(oldCar, newCar);

	}

}

class Car implements DefaultClone {
	private Integer id;
	private List<Wheel> wheelList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Wheel> getWheelList() {
		return wheelList;
	}

	public void setWheelList(List<Wheel> wheelList) {
		this.wheelList = wheelList;
	}

	@Override
	public String toString() {
		return "Car{" +
				"id=" + id +
				", wheelList=" + wheelList +
				'}';
	}
}


class Wheel {
	private String direction;

	public Wheel(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "Wheel{" +
				"direction='" + direction + '\'' +
				'}';
	}
}


