/**
 * 
 */
package com.xiaoleilu.hutool.tuple;

/**
 * @author zxx
 *
 */
public class TupleUtil {
	/**
	 * 生成二元组
	 * @param a
	 * @param b
	 * @return
	 */
	public static <A, B> TwoTuple<A, B> tuple(A a, B b) {
		return new TwoTuple<A, B>(a, b);
	}

	/**
	 * 生成三元组
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static <A, B, C> ThreeTuple<A, B, C> tuple(A a, B b, C c) {
		return new ThreeTuple<A, B, C>(a, b, c);
	}

	// 测试
	public static void main(String[] args) {
		//		List<GoodsBean> goodsBeans = new ArrayList<GoodsBean>();
		//		for (int i = 1; i < 26; i++) {
		//			GoodsBean goodsBean = new GoodsBean();
		//			goodsBean.setGoodsId(i);
		//			goodsBeans.add(goodsBean);
		//		}
		//		Integer totalProperty = 47;
		//		//      TupleUtil<List<GoodsBean>, Integer> tupleUtil = new TupleUtil<List<GoodsBean>, Integer>(goodsBeans, totalProperty);
		//		TwoTuple<List<GoodsBean>, Integer> twoTuple = TupleUtil.tuple(goodsBeans, totalProperty);
		//		List<GoodsBean> list = twoTuple.first;
		//		System.out.println(list);
		//		System.out.println(twoTuple.second);
	}
}
