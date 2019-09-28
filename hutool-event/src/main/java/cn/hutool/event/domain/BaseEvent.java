package cn.hutool.event.domain;

import java.util.EventObject;

/**
 * Class BaseEvent...
 * 基础事件模型
 * @author Ted.L
 * Created on 2019-09-28
 */
public abstract class BaseEvent extends EventObject {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 事件id */
	private final String      id;

	/** 事件产生时间 */
	private final long        timestamp;

	/**
	 * 构造函数
	 *
	 * @param source 产生事件的事件源
	 * @param id 事件id
	 */
	public BaseEvent(Object source, String id) {
		super(source);
		this.id = id;
		this.timestamp = System.currentTimeMillis();
	}

	/**
	 * 获取事件id
	 *
	 * @return 事件id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 获取事件产生的时间戳
	 *
	 * @return 事件产生的时间戳
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @see java.util.EventObject#toString()
	 */
	@Override
	public String toString() {
		return "BaseEvent [id=" + id + ", timestamp=" + timestamp + "]";
	}
}
