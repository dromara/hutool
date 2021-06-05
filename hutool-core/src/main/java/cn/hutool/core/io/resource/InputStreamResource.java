package cn.hutool.core.io.resource;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

/**
 * 基于{@link InputStream}的资源获取器<br>
 * 注意：此对象中getUrl方法始终返回null
 *
 * @author looly
 * @since 4.0.9
 */
public class InputStreamResource implements Resource, Serializable {
	private static final long serialVersionUID = 1L;

	private final InputStream in;
	private final String name;

	/**
	 * 构造
	 *
	 * @param in {@link InputStream}
	 */
	public InputStreamResource(InputStream in) {
		this(in, null);
	}

	/**
	 * 构造
	 *
	 * @param in {@link InputStream}
	 * @param name 资源名称
	 */
	public InputStreamResource(InputStream in, String name) {
		this.in = in;
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public InputStream getStream() {
		return this.in;
	}
}
