package cn.hutool.core.event;

/**
 * 边缘传播<br>
 * 除非返回事件自身或完全同类型的实例，否则会将其返回值按照{@link SpreadPattern#publishEvents(TypeEventContext, Object)}的逻辑继续发布<br>
 *
 * @author Create by liuwenhao on 2022/7/21 17:10
 */
public class EdgeSpreadPattern implements SpreadPattern {

	@Override
	public <E, R> void spread(TypeEventContext context, R r, E e) throws Throwable {
		if (!r.equals(e) && !r.getClass().equals(e.getClass())) {
			publishEvents(context, r);
		}
	}
}
