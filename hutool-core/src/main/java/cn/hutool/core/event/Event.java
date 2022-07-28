package cn.hutool.core.event;

import java.util.EventObject;

/**
 * {@link Event}是所有事件的基类，不过为了使用方便，并不要求强制继承{@link Event}<br>
 * 如果发布的事件继承了本类，那么按照正常的方式，事件的hashcode将会成为监听器识别事件的标识<br>
 *
 * @author Create by liuwenhao on 2022/7/19 19:54
 */
public abstract class Event extends EventObject {

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	Event(Object source) {
		super(source);
	}

}
