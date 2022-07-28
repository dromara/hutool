package cn.hutool.core.event;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExclusiveListenerPatternTest {

	@Test
	public void testGetListener() {
		ListenerPattern listenerPattern = new ExclusiveListenerPattern();

		List<Listener<Object, Object>> listeners = new ArrayList<>();
		listeners.add(o -> 2);
		listeners.add(o -> 4);
		listeners.add(o -> 1);

		Collection<Listener<Object, Object>> listener =
			listenerPattern.getListener(Listener.class, listeners);

		Assert.assertEquals(1, listener.size());

		ArrayList<Listener<Object, Object>> arrayList = new ArrayList<>(listener);
		Listener<Object, Object> objectListener = arrayList.get(0);
		Object o = objectListener.execEvent(null);

		Assert.assertEquals(2, o);
	}

}
