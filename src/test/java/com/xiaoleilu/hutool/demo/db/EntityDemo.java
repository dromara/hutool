package com.xiaoleilu.hutool.demo.db;

import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.lang.DateTime;

/**
 * 实体使用样例
 * @author Looly
 *
 */
public class EntityDemo {
	
	/**
	 * pojo类
	 * @author Looly
	 *
	 */
	public static class DemoPojo {
		private DateTime date;
		private String name;
		private int age;

		public DateTime getDate() {
			return date;
		}
		public void setDate(DateTime date) {
			this.date = date;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
		@Override
		public String toString() {
			return "DemoPojo [date=" + date + ", name=" + name + ", age=" + age + "]";
		}
	}

	public static void main(String[] args) {
		Entity entity = new Entity();
		entity
			.set("date", new DateTime())
			.set("name", "Lucy")
			.set("age", 24);

		DemoPojo vo = entity.toVo(DemoPojo.class);
		System.out.println(vo);
	}
}
