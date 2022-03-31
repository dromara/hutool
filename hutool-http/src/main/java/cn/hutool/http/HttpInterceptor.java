package cn.hutool.http;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Http拦截器接口，通过实现此接口，完成请求发起前或结束后对请求的编辑工作
 *
 * @param <T> 过滤参数类型，HttpRequest或者HttpResponse
 * @author looly
 * @since 5.7.16
 */
@FunctionalInterface
public interface HttpInterceptor<T extends HttpBase<T>> {

	/**
	 * 处理请求
	 *
	 * @param httpObj 请求或响应对象
	 */
	void process(T httpObj);

	/**
	 * 拦截器链
	 *
	 * @param <T> 过滤参数类型，HttpRequest或者HttpResponse
	 * @author looly
	 * @since 5.7.16
	 */
	class Chain<T extends HttpBase<T>> implements cn.hutool.core.lang.Chain<HttpInterceptor<T>, Chain<T>> {
		private final List<HttpInterceptor<T>> interceptors = new LinkedList<>();

		@Override
		public Chain<T> addChain(HttpInterceptor<T> element) {
			interceptors.add(element);
			return this;
		}

		@Override
		public Iterator<HttpInterceptor<T>> iterator() {
			return interceptors.iterator();
		}

		/**
		 * 清空
		 *
		 * @return this
		 * @since 5.8.0
		 */
		public Chain<T> clear() {
			interceptors.clear();
			return this;
		}
	}
}
