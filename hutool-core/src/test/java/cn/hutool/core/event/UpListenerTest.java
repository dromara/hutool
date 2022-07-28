package cn.hutool.core.event;

/**
 * <h2></h2>
 *
 * @author Create by liuwenhao on 2022/7/27 14:55
 */
public class UpListenerTest {

	@EventListener(value = EventUtilTest.UpEvent.class)
	public String UpListener1(EventUtilTest.UpEvent upEvent) {
		upEvent.setName("同步执行，普通监听器");
		return upEvent.getName();
	}

	@EventListener(value = EventUtilTest.UpEvent.class, isAsync = true, order = 2)
	public String UpListener2(EventUtilTest.UpEvent upEvent) {
		upEvent.setName("异步执行监听器");
		return upEvent.getName();
	}

	@EventListener(value = EventUtilTest.UpEvent.class, order = 5)
	public String UpListener3(EventUtilTest.UpEvent upEvent) {
		upEvent.setName("优先级低的监听器");
		return upEvent.getName();
	}

	@EventListener(value = EventUtilTest.UpEvent.class, throwHandler = "throwHandler4", order = 1)
	public String UpListener4(EventUtilTest.UpEvent upEvent) {
		upEvent.setName("有异常的监听器");
		throw new RuntimeException(upEvent.getName());
	}

	public String throwHandler4(Throwable throwable) {
		return throwable.getMessage();
	}
}
