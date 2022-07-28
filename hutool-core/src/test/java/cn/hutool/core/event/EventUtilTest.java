package cn.hutool.core.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EventUtilTest {

	@Test
	public void testClear() {
		EventUtil.<UpEvent, String>bind(UpEvent.class, o -> {
			o.setName("向上");
			return o.getName();
		});
		TypeEventContext context = EventUtil.eventContext();
		Collection<Listener<Object, Object>> listeners = context.getListeners(UpEvent.class);
		Assert.assertEquals(1, listeners.size());
		TypeEventContext clear = EventUtil.clear();
		Collection<Listener<Object, Object>> listeners1 = clear.getListeners(UpEvent.class);
		Assert.assertEquals(0, listeners1.size());
	}

	/**
	 * {@link  EventUtil#bind(Object)}
	 */
	@Test
	public void testBind() {
		UpListenerTest listenerTest = new UpListenerTest();
		TypeEventContext eventContext = EventUtil.bind(listenerTest);
		ParameterizedTypeImpl type = ParameterizedTypeImpl.make(UpEvent.class, new Type[0], EventUtilTest.class);
		Collection<Listener<Object, Object>> listeners = eventContext.getListeners(type);
		Assert.assertEquals(4, listeners.size());
		UpEvent upEvent = new UpEvent("上");
		EventUtil.publish(upEvent, type);
		List<Listener<Object, Object>> listeners1 = (List<Listener<Object, Object>>) eventContext.getListeners(type);
		Object result1 = ((ListenerDecorate<Object, Object>) listeners1.get(0)).getResult();
		Assert.assertEquals("同步执行，普通监听器", result1);
		Object result2 = ((ListenerDecorate<Object, Object>) listeners1.get(1)).getResult();
		Assert.assertNull(result2);
		Object result3 = ((ListenerDecorate<Object, Object>) listeners1.get(2)).getResult();
		Assert.assertEquals("异步执行监听器", result3);
		Object result4 = ((ListenerDecorate<Object, Object>) listeners1.get(3)).getResult();
		Assert.assertEquals("优先级低的监听器", result4);
	}

	/**
	 * {@link  EventUtil#bind(Method, Object)}
	 */
	@Test
	public void testTestBind() throws NoSuchMethodException {
		EventUtil.clear();
		UpListenerTest upListenerTest = new UpListenerTest();
		Method upListener1 = UpListenerTest.class.getMethod("UpListener1", UpEvent.class);
		TypeEventContext eventContext = EventUtil.bind(upListener1, upListenerTest);
		ParameterizedTypeImpl type = ParameterizedTypeImpl.make(UpEvent.class, new Type[0], EventUtilTest.class);
		Collection<Listener<Object, Object>> listeners = eventContext.getListeners(type);
		Assert.assertEquals(1, listeners.size());
		UpEvent upEvent = new UpEvent("上");
		EventUtil.publish(upEvent, type);
		List<Listener<Object, Object>> listeners1 = (List<Listener<Object, Object>>) eventContext.getListeners(type);
		Object result1 = ((ListenerDecorate<Object, Object>) listeners1.get(0)).getResult();
		Assert.assertEquals("同步执行，普通监听器", result1);
	}

	/**
	 * {@link  EventUtil#bind(Type, Listener)}
	 */
	@Test
	public void testTestBind1() {
		EventUtil.<UpEvent, String>bind(UpEvent.class, o -> {
			o.setName("向上");
			return o.getName();
		});
		UpEvent upEvent = new UpEvent("上");
		EventUtil.publish(upEvent);

		Assert.assertEquals("向上", upEvent.getName());
	}

	/**
	 * {@link EventUtil#bind(Type, ListenerBuilder)}
	 */
	@Test
	public void testTestBind2() throws InterruptedException {
		ListenerBuilder<UpEvent, String> build = EventUtil
			.<UpEvent, String>createListener(o -> {
				o.setName("向上");
				return o.getName();
			}).async(true).order(5);

		EventUtil.bind(UpEvent.class, build);
		UpEvent upEvent = new UpEvent("上");
		EventUtil.publish(upEvent);
		// 异步执行
		TimeUnit.SECONDS.sleep(1);
		Assert.assertEquals("向上", upEvent.getName());

	}

	/**
	 * {@link EventUtil#unbind(Type, Listener)}
	 */
	@Test
	public void testUnbind() {
		String str = "修改";
		Listener<UpEvent, String> listener = o -> {
			o.setName("向上" + str);
			return o.getName();
		};
		EventUtil.bind(UpEvent.class, listener);
		UpEvent upEvent = new UpEvent("上");
		EventUtil.publish(upEvent);
		Assert.assertEquals("向上修改", upEvent.getName());
		EventUtil.unbind(UpEvent.class, listener);
		EventUtil.publish(upEvent);
		Assert.assertNotEquals("向上修改修改", upEvent.getName());
	}

	@Getter
	@Setter
	@ToString
	static class UpEvent {
		private String name;

		UpEvent(String name) {
			this.name = name;
		}
	}

}
