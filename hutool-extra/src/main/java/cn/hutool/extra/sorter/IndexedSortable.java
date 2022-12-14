package cn.hutool.extra.sorter;


/**
 * 排序接口定义
 * @author zhaosheng
 */
public interface IndexedSortable {

	/**
	 * 比较给定位置的值，返回值大于0则执行交换，反之什么也不做
	 */
	int compare(int i, int j);

	/**
	 * 交换数据
	 */
	void swap(int i, int j);
}
