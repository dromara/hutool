package cn.hutool.core.event;

import lombok.*;
import org.junit.Assert;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class TypeDefaultContextTest {

	@Test
	public void testBind() {
		TypeEventContext context = new TypeDefaultContext("name");
		Class<Integer> type = Integer.TYPE;
		// 没有时新增

		Listener<Integer, Integer> listener = o -> 1;
		context.bind(type, listener);
		Collection<Listener<Object, Object>> listeners = context.getListeners(type);
		Assert.assertEquals(1, listeners.size());

		// 已经有了就直接在原有的基础上添加
		context.bind(type, o -> 5);
		Collection<Listener<Object, Object>> listeners1 = context.getListeners(type);
		Assert.assertEquals(2, listeners1.size());

		// 如果添加重复的，则不会添加
		context.bind(type, listener);
		Collection<Listener<Object, Object>> listeners2 = context.getListeners(type);
		Assert.assertEquals(2, listeners2.size());

	}

	@Test
	public void testSetListenerPattern() {
		TypeEventContext context = new TypeDefaultContext("name");
		Class<Integer> type = Integer.TYPE;

		// 绑定监听器后添加默认的监听模式实现：BroadcastListenerPattern
		context.bind(type, o -> 1);
		context.bind(type, o -> 2);
		context.bind(type, o -> 3);

		Collection<Listener<Object, Object>> listeners = context.getListeners(type);
		Assert.assertEquals(3, listeners.size());

		// 可以修改内部的监听模式，来修改获取到的监听器
		context.setListenerPattern(type, new ExclusiveListenerPattern());

		// 更换监听模式为独占模式后仅会返回一个监听器
		Collection<Listener<Object, Object>> listeners1 = context.getListeners(type);
		Assert.assertEquals(1, listeners1.size());

	}

	@Test
	public void testUnbind() {
		TypeEventContext context = new TypeDefaultContext("name");
		Class<Integer> type = Integer.TYPE;

		// 绑定3个监听器
		Listener<Integer, Integer> listener1 = o -> 1;
		Listener<Integer, Integer> listener2 = o -> 2;
		Listener<Integer, Integer> listener3 = o -> 3;
		Listener<Integer, Integer> listener4 = o -> 4;
		context.bind(type, listener1);
		context.bind(type, listener2);
		context.bind(type, listener3);

		Collection<Listener<Object, Object>> listeners = context.getListeners(type);
		Assert.assertEquals(3, listeners.size());

		// 解绑
		context.unbind(type, listener1);
		Collection<Listener<Object, Object>> listeners1 = context.getListeners(type);
		Assert.assertEquals(2, listeners1.size());
		// 解绑不存在的

		context.unbind(type, listener4);
		Collection<Listener<Object, Object>> listeners2 = context.getListeners(type);
		Assert.assertEquals(2, listeners2.size());

	}

	@Test
	public void testUnbindAll() {
		TypeEventContext context = new TypeDefaultContext("name");
		Class<Integer> type1 = Integer.TYPE;
		Class<Long> type2 = Long.TYPE;

		// 分别绑定监听器
		context.bind(type1, o -> 1);
		context.bind(type1, o -> 2);
		context.bind(type1, o -> 3);
		context.bind(type2, o -> "a");

		Collection<Listener<Object, Object>> listeners1 = context.getListeners(type1);
		Assert.assertEquals(3, listeners1.size());
		Collection<Listener<Object, Object>> listeners2 = context.getListeners(type2);
		Assert.assertEquals(1, listeners2.size());
		// 解绑后
		context.unbindAll(type1);
		Collection<Listener<Object, Object>> listeners3 = context.getListeners(type1);
		Assert.assertEquals(0, listeners3.size());
		Collection<Listener<Object, Object>> listeners4 = context.getListeners(type2);
		Assert.assertEquals(1, listeners4.size());

	}

	@Test
	public void testGetListeners() {
		TypeEventContext context = new TypeDefaultContext("name");
		Class<Integer> type = Integer.TYPE;
		// 什么都不绑定时得到的是空集合
		Collection<Listener<Object, Object>> listeners = context.getListeners(type);
		Assert.assertTrue(listeners.isEmpty());

		// 绑定后
		context.bind(type, o -> 1);
		Collection<Listener<Object, Object>> listeners1 = context.getListeners(type);
		Assert.assertFalse(listeners1.isEmpty());

	}

	@Test
	public void testPublish() throws InterruptedException {
		TypeEventContext context = new TypeDefaultContext("name");
		Listener<AddEvent, String> listener = addEvent -> {
			addEvent.setName("新名字");
			return addEvent.getName();
		};
		// 绑定
		context.bind(AddEvent.class, listener);
		AddEvent addEvent = new AddEvent(this, "名字");
		context.publish(addEvent);
		Assert.assertEquals("新名字", addEvent.getName());

		// 绑定多个
		Listener<AddEvent, String> listener1 = ListenerDecorate.<AddEvent, String>build()
			.order(1)
			.listener(e -> {
				e.setName("新新名字");
				return e.getName();
			});
		context.bind(AddEvent.class, listener1);
		context.publish(addEvent);
		Assert.assertEquals("新新名字", addEvent.getName());

		// 传播继承 Event 的的事件
		UpdateEvent updateEvent = new UpdateEvent("修改名字");
		Listener<AddEvent, UpdateEvent> listener2 = ListenerDecorate.<AddEvent, UpdateEvent>build()
			.order(2)
			.listener(e -> {
				e.setName("新新新名字");
				return updateEvent;
			});
		context.bind(AddEvent.class, listener2);
		Listener<UpdateEvent, String> listener3 = ListenerDecorate.<UpdateEvent, String>build()
			.order(3)
			.listener(e -> {
				e.setName("新修改名字");
				return e.getName();
			});
		context.bind(UpdateEvent.class, listener3);
		context.publish(addEvent);
		Assert.assertEquals("新新新名字", addEvent.getName());
		Assert.assertEquals("新修改名字", updateEvent.getName());

		// 测试异步修改
		Listener<AsyncEvent, String> listener4 = ListenerDecorate.<AsyncEvent, String>build()
			.order(1)
			.async(true)
			.listener(e -> {
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException ex) {
					throw new RuntimeException(ex);
				}
				e.setName("异步新名字");
				return e.getName();
			});
		context.bind(AsyncEvent.class, listener4);
		AsyncEvent asyncEvent = new AsyncEvent("异步名字");
		context.publish(asyncEvent);

		Assert.assertEquals("异步名字", asyncEvent.getName());
		TimeUnit.SECONDS.sleep(2);
		// 等待两秒后修改成功
		Assert.assertEquals("新修改名字", updateEvent.getName());

	}

	@Test
	public void testParameterizedTypePublish() {
		TypeEventContext context = new TypeDefaultContext("name");
		Listener<DeleteEvent<Integer>, Integer> listener1 = deleteEvent -> {
			deleteEvent.setName(2022);
			return deleteEvent.getName();
		};

		Listener<DeleteEvent<String>, String> listener2 = deleteEvent -> {
			deleteEvent.setName("删除事件");
			return deleteEvent.getName();
		};

		// 绑定
		ParameterizedType parameterizedType1 = ParameterizedTypeImpl.make(DeleteEvent.class, new Class[]{Integer.class}, TypeDefaultContextTest.class);
		context.bind(parameterizedType1, listener1);
		ParameterizedType parameterizedType2 = ParameterizedTypeImpl.make(DeleteEvent.class, new Class[]{String.class}, TypeDefaultContextTest.class);
		context.bind(parameterizedType2, listener2);
		DeleteEvent<Integer> integerDeleteEvent = new DeleteEvent<>(0);
		DeleteEvent<String> stringDeleteEvent = new DeleteEvent<>("删除");

		// 发布
		context.publish(integerDeleteEvent, parameterizedType1);
		Assert.assertEquals(new Integer(2022), integerDeleteEvent.getName());
		Assert.assertEquals("删除", stringDeleteEvent.getName());
		context.publish(stringDeleteEvent, parameterizedType2);
		Assert.assertEquals(new Integer(2022), integerDeleteEvent.getName());
		Assert.assertEquals("删除事件", stringDeleteEvent.getName());
	}

	@Getter
	@Setter
	@ToString
	static class AddEvent extends Event {

		private String name;

		AddEvent(Object source, String name) {
			super(source);
			this.name = name;
		}
	}

	@Getter
	@Setter
	@ToString
	static class UpdateEvent {

		private String name;

		UpdateEvent(String name) {
			this.name = name;
		}
	}

	@Getter
	@Setter
	@ToString
	static class AsyncEvent {

		private String name;

		AsyncEvent(String name) {
			this.name = name;
		}
	}

	@Getter
	@Setter
	@ToString
	static class DeleteEvent<T> {
		T name;

		public DeleteEvent(T name) {
			this.name = name;
		}
	}

}
