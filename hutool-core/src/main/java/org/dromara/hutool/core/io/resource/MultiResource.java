/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.io.resource;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.IORuntimeException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * 多资源组合资源<br>
 * 此资源为一个利用游标自循环资源，只有调用{@link #next()} 方法才会获取下一个资源，使用完毕后调用{@link #reset()}方法重置游标
 *
 * @author looly
 * @since 4.1.0
 */
public class MultiResource implements Resource, Iterable<Resource>, Iterator<Resource>, Serializable {
	private static final long serialVersionUID = 1L;

	private final List<Resource> resources;
	private int cursor;

	/**
	 * 构造
	 *
	 * @param resources 资源数组
	 */
	public MultiResource(final Resource... resources) {
		this(ListUtil.of(resources));
	}

	/**
	 * 构造
	 *
	 * @param resources 资源列表
	 */
	public MultiResource(final Collection<Resource> resources) {
		if(resources instanceof List) {
			this.resources = (List<Resource>)resources;
		}else {
			this.resources = ListUtil.of(resources);
		}
	}

	@Override
	public String getName() {
		return resources.get(cursor).getName();
	}

	@Override
	public URL getUrl() {
		return resources.get(cursor).getUrl();
	}

	@Override
	public long size() {
		return resources.get(cursor).size();
	}

	@Override
	public InputStream getStream() {
		return resources.get(cursor).getStream();
	}

	@Override
	public boolean isModified() {
		return resources.get(cursor).isModified();
	}

	@Override
	public BufferedReader getReader(final Charset charset) {
		return resources.get(cursor).getReader(charset);
	}

	@Override
	public String readStr(final Charset charset) throws IORuntimeException {
		return resources.get(cursor).readStr(charset);
	}

	@Override
	public String readUtf8Str() throws IORuntimeException {
		return resources.get(cursor).readUtf8Str();
	}

	@Override
	public byte[] readBytes() throws IORuntimeException {
		return resources.get(cursor).readBytes();
	}

	@Override
	public Iterator<Resource> iterator() {
		return resources.iterator();
	}

	@Override
	public boolean hasNext() {
		return cursor < resources.size();
	}

	@Override
	public synchronized Resource next() {
		if (cursor >= resources.size()) {
			throw new ConcurrentModificationException();
		}
		this.cursor++;
		return this;
	}

	@Override
	public void remove() {
		this.resources.remove(this.cursor);
	}

	/**
	 * 重置游标
	 */
	public synchronized void reset() {
		this.cursor = 0;
	}

	/**
	 * 增加资源
	 * @param resource 资源
	 * @return this
	 */
	public MultiResource add(final Resource resource) {
		this.resources.add(resource);
		return this;
	}

}
