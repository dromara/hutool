package cn.hutool.http;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Http拦截器接口，通过实现此接口，完成请求发起前对请求的编辑工作
 *
 * @author looly
 * @since 5.7.16
 */
@FunctionalInterface
public interface HttpInterceptor {

	/**
	 * 处理请求
	 *
	 * @param request 请求
	 */
	void process(HttpRequest request);

	/**
	 * 拦截器链
	 *
	 * @author looly
	 * @since 5.7.16
	 */
	class Chain implements cn.hutool.core.lang.Chain<HttpInterceptor, Chain> {
		private final List<HttpInterceptor> interceptors = new LinkedList<>();


		@Override
		public Chain addChain(HttpInterceptor element) {
			interceptors.add(element);
			return this;
		}

		@Override
		public Iterator<HttpInterceptor> iterator() {
			return interceptors.iterator();
		}
	}
}
