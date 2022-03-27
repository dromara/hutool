package cn.hutool.http;

/**
 * 全局的拦截器
 *
 * @author looly
 * @since 5.8.0
 */
public enum GlobalInterceptor {
	INSTANCE;

	private final HttpInterceptor.Chain interceptors = new HttpInterceptor.Chain();

	/**
	 * 设置拦截器，用于在请求前重新编辑请求
	 *
	 * @param interceptor 拦截器实现
	 */
	synchronized public GlobalInterceptor addInterceptor(HttpInterceptor interceptor) {
		this.interceptors.addChain(interceptor);
		return this;
	}

	/**
	 * 清空
	 * @return this
	 */
	synchronized public GlobalInterceptor clear(){
		interceptors.clear();
		return this;
	}

	/**
	 * 复制过滤器列表
	 * @return {@link cn.hutool.http.HttpInterceptor.Chain}
	 */
	HttpInterceptor.Chain getCopied(){
		final HttpInterceptor.Chain copied = new HttpInterceptor.Chain();
		for (HttpInterceptor interceptor : this.interceptors) {
			copied.addChain(interceptor);
		}
		return copied;
	}
}
