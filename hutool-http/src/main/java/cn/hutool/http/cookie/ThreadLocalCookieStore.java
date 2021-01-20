package cn.hutool.http.cookie;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * 线程隔离的Cookie存储。多线程环境下Cookie隔离使用，防止Cookie覆盖<br>
 * 
 * 见：https://stackoverflow.com/questions/16305486/cookiemanager-for-multiple-threads
 * 
 * @author looly
 * @since 4.1.18
 */
public class ThreadLocalCookieStore implements CookieStore {

	private final static ThreadLocal<CookieStore> STORES = new ThreadLocal<CookieStore>() {
		@Override
		protected synchronized CookieStore initialValue() {
			/* InMemoryCookieStore */
			return (new CookieManager()).getCookieStore();
		}
	};

	/**
	 * 获取本线程下的CookieStore
	 * 
	 * @return CookieStore
	 */
	public CookieStore getCookieStore() {
		return STORES.get();
	}

	/**
	 * 移除当前线程的Cookie
	 * 
	 * @return this
	 */
	public ThreadLocalCookieStore removeCurrent() {
		STORES.remove();
		return this;
	}

	@Override
	public void add(URI uri, HttpCookie cookie) {
		getCookieStore().add(uri, cookie);
	}

	@Override
	public List<HttpCookie> get(URI uri) {
		return getCookieStore().get(uri);
	}

	@Override
	public List<HttpCookie> getCookies() {
		return getCookieStore().getCookies();
	}

	@Override
	public List<URI> getURIs() {
		return getCookieStore().getURIs();
	}

	@Override
	public boolean remove(URI uri, HttpCookie cookie) {
		return getCookieStore().remove(uri, cookie);
	}

	@Override
	public boolean removeAll() {
		return getCookieStore().removeAll();
	}
}
