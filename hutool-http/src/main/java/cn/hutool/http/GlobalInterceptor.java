package cn.hutool.http;

/**
 * 全局的拦截器<br>
 * 包括请求拦截器和响应拦截器
 *
 * @author looly
 * @since 5.8.0
 */
public enum GlobalInterceptor {
	INSTANCE;

	private final HttpInterceptor.Chain<HttpRequest> requestInterceptors = new HttpInterceptor.Chain<>();
	private final HttpInterceptor.Chain<HttpResponse> responseInterceptors = new HttpInterceptor.Chain<>();

	/**
	 * 设置拦截器，用于在请求前重新编辑请求
	 *
	 * @param interceptor 拦截器实现
	 * @return this
	 */
	synchronized public GlobalInterceptor addRequestInterceptor(HttpInterceptor<HttpRequest> interceptor) {
		this.requestInterceptors.addChain(interceptor);
		return this;
	}

	/**
	 * 设置拦截器，用于在响应读取后完成编辑或读取
	 *
	 * @param interceptor 拦截器实现
	 * @return this
	 */
	synchronized public GlobalInterceptor addResponseInterceptor(HttpInterceptor<HttpResponse> interceptor) {
		this.responseInterceptors.addChain(interceptor);
		return this;
	}

	/**
	 * 清空请求和响应拦截器
	 *
	 * @return this
	 */
	public GlobalInterceptor clear() {
		clearRequest();
		clearResponse();
		return this;
	}

	/**
	 * 清空请求拦截器
	 *
	 * @return this
	 */
	synchronized public GlobalInterceptor clearRequest() {
		requestInterceptors.clear();
		return this;
	}

	/**
	 * 清空响应拦截器
	 *
	 * @return this
	 */
	synchronized public GlobalInterceptor clearResponse() {
		responseInterceptors.clear();
		return this;
	}

	/**
	 * 复制请求过滤器列表
	 *
	 * @return {@link cn.hutool.http.HttpInterceptor.Chain}
	 */
	HttpInterceptor.Chain<HttpRequest> getCopiedRequestInterceptor() {
		final HttpInterceptor.Chain<HttpRequest> copied = new HttpInterceptor.Chain<>();
		for (HttpInterceptor<HttpRequest> interceptor : this.requestInterceptors) {
			copied.addChain(interceptor);
		}
		return copied;
	}

	/**
	 * 复制响应过滤器列表
	 *
	 * @return {@link cn.hutool.http.HttpInterceptor.Chain}
	 */
	HttpInterceptor.Chain<HttpResponse> getCopiedResponseInterceptor() {
		final HttpInterceptor.Chain<HttpResponse> copied = new HttpInterceptor.Chain<>();
		for (HttpInterceptor<HttpResponse> interceptor : this.responseInterceptors) {
			copied.addChain(interceptor);
		}
		return copied;
	}
}
