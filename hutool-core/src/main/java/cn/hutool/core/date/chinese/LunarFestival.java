package cn.hutool.core.date.chinese;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.TableMap;

import java.util.List;

/**
 * 节假日（农历）封装
 *
 * @author looly
 * @since 5.4.1
 */
public class LunarFestival {

	//农历节日  *表示放假日
	// 来自：https://baike.baidu.com/item/%E4%B8%AD%E5%9B%BD%E4%BC%A0%E7%BB%9F%E8%8A%82%E6%97%A5/396100
	private static final TableMap<Pair<Integer, Integer>, String> lFtv = new TableMap<>(16);
	static{
		lFtv.put(new Pair<>(1, 1), "春节");
		lFtv.put(new Pair<>(1, 2), "犬日");
		lFtv.put(new Pair<>(1, 3), "猪日");
		lFtv.put(new Pair<>(1, 4), "羊日");
		lFtv.put(new Pair<>(1, 5), "牛日 破五日");
		lFtv.put(new Pair<>(1, 6), "马日 送穷日");
		lFtv.put(new Pair<>(1, 7), "人日 人胜节");
		lFtv.put(new Pair<>(1, 8), "谷日 八仙日");
		lFtv.put(new Pair<>(1, 9), "天日 九皇会");
		lFtv.put(new Pair<>(1, 10), "地日 石头生日");
		lFtv.put(new Pair<>(1, 12), "火日 老鼠娶媳妇日");
		lFtv.put(new Pair<>(1, 13), "上（试）灯日 关公升天日");
		lFtv.put(new Pair<>(1, 15), "元宵节");
		lFtv.put(new Pair<>(1, 18), "落灯日");

		// 二月
		lFtv.put(new Pair<>(2, 1), "中和节 太阳生日");
		lFtv.put(new Pair<>(2, 2), "龙抬头");
		lFtv.put(new Pair<>(2, 12), "花朝节");
		lFtv.put(new Pair<>(2, 19), "观世音圣诞");

		// 三月
		lFtv.put(new Pair<>(3, 3), "上巳节");

		// 四月
		lFtv.put(new Pair<>(4, 1), "祭雹神");
		lFtv.put(new Pair<>(4, 4), "文殊菩萨诞辰");
		lFtv.put(new Pair<>(4, 8), "佛诞节");

		// 五月
		lFtv.put(new Pair<>(5, 5), "端午节");

		// 六月
		lFtv.put(new Pair<>(6, 6), "晒衣节 姑姑节");
		lFtv.put(new Pair<>(6, 6), "天贶节");
		lFtv.put(new Pair<>(6, 24), "彝族火把节");

		// 七月
		lFtv.put(new Pair<>(7, 7), "七夕");
		lFtv.put(new Pair<>(7, 14), "鬼节(南方)");
		lFtv.put(new Pair<>(7, 15), "中元节");
		lFtv.put(new Pair<>(7, 15), "盂兰盆节");
		lFtv.put(new Pair<>(7, 30), "地藏节");

		// 八月
		lFtv.put(new Pair<>(8, 15), "中秋节");

		// 九月
		lFtv.put(new Pair<>(9, 9), "重阳节");

		// 十月
		lFtv.put(new Pair<>(10, 1), "祭祖节");
		lFtv.put(new Pair<>(10, 15), "下元节");

		// 十一月
		lFtv.put(new Pair<>(11, 17), "阿弥陀佛圣诞");

		// 腊月
		lFtv.put(new Pair<>(12, 8), "腊八节");
		lFtv.put(new Pair<>(12, 16), "尾牙");
		lFtv.put(new Pair<>(12, 23), "小年");
		lFtv.put(new Pair<>(12, 29), "除夕");
		lFtv.put(new Pair<>(12, 30), "除夕");
	}

	/**
	 * 获得节日列表
	 *
	 * @param month 月
	 * @param day   日
	 * @return 获得农历节日
	 */
	public static List<String> getFestivals(int month, int day) {
		return lFtv.getValues(new Pair<>(month, day));
	}
}
