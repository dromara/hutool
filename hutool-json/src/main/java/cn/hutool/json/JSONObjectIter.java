package cn.hutool.json;

import java.util.Iterator;

/**
 * 此类用于在JSONAray中便于遍历JSONObject而封装的Iterable，可以借助foreach语法遍历
 * 
 * @author looly
 * @since 4.0.12
 */
public class JSONObjectIter implements Iterable<JSONObject> {

	Iterator<Object> iter;
	
	public JSONObjectIter(Iterator<Object> iter) {
		this.iter = iter;
	}

	@Override
	public Iterator<JSONObject> iterator() {
		return new Iterator<JSONObject>() {

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public JSONObject next() {
				return (JSONObject) iter.next();
			}

			@Override
			public void remove() {
				iter.remove();
			}
		};
	}

}
