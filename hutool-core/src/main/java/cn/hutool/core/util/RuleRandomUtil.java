package cn.hutool.core.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 按照既定规则随机生成
 * <p>
 * 包含的的规则： UUID, Date, Time, Datetime, RegionCN, ProvinceCN, CityCN, CountyCN, PostalCodeCN, FirstNameEN,
 * LastNameEN, NameEN, FirstNameCN, LastNameCN, NameCN, ColorCN, ColorEn, IP, Port, NetAddr, Domain, URL, EmailAddr
 *
 * @author daidai21
 */
public class RuleRandomUtil {
	/**
	 * 生成GeneratorRule 接口
	 */
	@FunctionalInterface
	public interface GeneratorRule {
		String generate(List<String> param);
	}

	/**
	 * 规则名到生成方法的映射
	 */
	private final static ConcurrentHashMap<String, GeneratorRule> ruleNameToMethodMap = new ConcurrentHashMap<String, GeneratorRule>() {
		{
			put("UUID", paramList -> getRandomUUID(paramList));
			put("Date", paramList -> getRandomDate(paramList));
			put("Time", paramList -> getRandomTime(paramList));
			put("Datetime", paramList -> getRandomDatetime(paramList));
			put("RegionCN", paramList -> getRandomRegionCN(paramList));
			put("ProvinceCN", paramList -> getRandomProvinceCN(paramList));
			put("CityCN", paramList -> getRandomCityCN(paramList));
			put("CountyCN", paramList -> getRandomCountyCN(paramList));
			put("PostalCodeCN", paramList -> getRandomPostalCodeCN(paramList));
			put("FirstNameEN", paramList -> getRandomFirstNameEN(paramList));
			put("LastNameEN", paramList -> getRandomLastNameEN(paramList));
			put("NameEN", paramList -> getRandomNameEN(paramList));
			put("FirstNameCN", paramList -> getRandomFirstNameCN(paramList));
			put("LastNameCN", paramList -> getRandomLastNameCN(paramList));
			put("NameCN", paramList -> getRandomNameCN(paramList));
			put("ColorCN", paramList -> getRandomColorCN(paramList));
			put("ColorEn", paramList -> getRandomColorCN(paramList));
			put("IP", paramList -> getRandomIP(paramList));
			put("Port", paramList -> getRandomPort(paramList));
			put("NetAddr", paramList -> getRandomNetAddr(paramList));
			put("Domain", paramList -> getRandomDomain(paramList));
			put("URL", paramList -> getRandomURL(paramList));
			put("EmailAddr", paramList -> getRandomEmailAddr(paramList));
		}
	};

	/**
	 * 添加自定义随机规则
	 *
	 * @param ruleName
	 * @param ruleMethod {@link GeneratorRule}
	 * @return
	 */
	public static Boolean addRuleMethod(String ruleName, GeneratorRule ruleMethod) {
		if (ruleNameToMethodMap.containsKey(ruleName)) {
			return Boolean.FALSE;
		}
		ruleNameToMethodMap.put(ruleName, ruleMethod);
		return Boolean.TRUE;
	}

	/**
	 * 单个规则随机生成
	 *
	 * @param rule
	 * @return 生成的随机字符串
	 */
	public static String getSingleRuleRandom(String rule) {
		if (!checkSingleRuleParam(rule)) {
			throw new IllegalArgumentException("rule config error.");
		}
		// 获取规则名称
		String ruleName = "";
		if (rule.contains("|")) {
			ruleName = rule.substring(1, rule.indexOf('|'));
		} else {
			ruleName = rule.substring(1, rule.length() - 1);
		}
		// 切分规则入参
		List<String> paramList = new ArrayList<>();
		if (rule.contains("|")) {
			for (String param : rule.substring(rule.indexOf("|") + 1, rule.length() - 1).split(",")) {
				paramList.add(param);
			}
		}
		return ruleNameToMethodMap.get(ruleName).generate(paramList);
	}

	/**
	 * 多个规则随机生成（包含常量字符）
	 *
	 * @param rules
	 * @return 生成的随机字符串
	 */
	public static String getMultiRuleRandom(String rules) {
		String result = "";
		if (!checkMultiRuleParam(rules)) {
			throw new IllegalArgumentException("rules config error.");
		}
		// 切分规则
		Boolean isOpenSingleRule = false;
		String tempRule = "";
		for (int i = 0; i < rules.length(); ++i) {
			if ('[' == rules.charAt(i)) {
				tempRule += rules.charAt(i);
				isOpenSingleRule = true;
			} else if (']' == rules.charAt(i)) {
				tempRule += rules.charAt(i);
				result += getSingleRuleRandom(tempRule);
				tempRule = "";
				isOpenSingleRule = false;
			} else if (isOpenSingleRule) {
				tempRule += rules.charAt(i);
			} else {
				result += rules.charAt(i);
			}
		}
		return result;
	}

	/**
	 * 检查单个规则是否合法
	 * <p>
	 * 规则和参数用"|"隔开，多个参数之间用","隔开
	 *
	 * @param rule
	 * @return 规则是否合法
	 */
	private static Boolean checkSingleRuleParam(String rule) {
		if (rule.length() <= 2) {
			return Boolean.FALSE;
		}
		int leftSquareBracketsCount = 0;
		int rightSquareBracketsCount = 0;
		int colonCount = 0;
		int commaCount = 0;
		for (int i = 0; i < rule.length(); ++i) {
			// 更新符号出现的次数
			switch (rule.charAt(i)) {
				case '[':
					leftSquareBracketsCount++;
					break;
				case ']':
					rightSquareBracketsCount++;
					break;
				case '|':
					colonCount++;
					break;
				case ',':
					commaCount++;
					break;
				default:
					break;
			}
			// 左括号是在 右括号、冒号、分号、逗号 之前出现
			if (leftSquareBracketsCount == 0 && (rightSquareBracketsCount != 0 || colonCount != 0 || commaCount != 0)) {
				return Boolean.FALSE;
			}
			// 右括号是在 左括号 之后出现
			if (rightSquareBracketsCount != 0 && leftSquareBracketsCount == 0) {
				return Boolean.FALSE;
			}
			// 逗号在冒号之后出现
			if (commaCount != 0 && colonCount == 0) {
				return Boolean.FALSE;
			}
		}
		// 左括号和右括号只能出现一次
		if (leftSquareBracketsCount != 1 || rightSquareBracketsCount != 1) {
			return Boolean.FALSE;
		}
		// 冒号最多只能出现1次
		if (colonCount > 1) {
			return Boolean.FALSE;
		}
		// 获取规则名称
		String ruleName = "";
		if (colonCount == 1) {
			ruleName = rule.substring(1, rule.indexOf("|"));
		} else {
			ruleName = rule.substring(1, rule.length() - 1);
		}
		if (ruleName.length() == 0) {
			return Boolean.FALSE;
		}
		// 检查是否支持此规则
		if (!ruleNameToMethodMap.containsKey(ruleName)) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * 检查多个规则是否合法，包含其他常量字符
	 *
	 * @param rules
	 * @return 多个规则是否合法
	 */
	private static Boolean checkMultiRuleParam(String rules) {
		// 验证左方括号和右方括号是否匹配
		Boolean isOpenLeftSquareBrackets = false;
		for (int i = 0; i < rules.length(); ++i) {
			if (rules.charAt(i) == '[') {
				if (isOpenLeftSquareBrackets.equals(true)) {
					return Boolean.FALSE;
				} else {
					isOpenLeftSquareBrackets = true;
				}
			} else if (rules.charAt(i) == ']') {
				if (isOpenLeftSquareBrackets.equals(false)) {
					return Boolean.FALSE;
				} else {
					isOpenLeftSquareBrackets = false;
				}
			}
		}
		if (isOpenLeftSquareBrackets.equals(true)) {
			return Boolean.FALSE;
		}
		// 验证方括号调用的规则是否支持
		Boolean isSingleRule = false;
		String tempRule = "";
		for (int i = 0; i < rules.length(); ++i) {
			if (rules.charAt(i) == '[') {
				if (!isSingleRule) {
					isSingleRule = true;
					tempRule += rules.charAt(i);
				} else {
					return Boolean.FALSE;
				}
			} else if (rules.charAt(i) == ']') {
				if (isSingleRule) {
					isSingleRule = false;
					tempRule += rules.charAt(i);
					if (!checkSingleRuleParam(tempRule)) {
						return Boolean.FALSE;
					}
					tempRule = "";
				} else {
					return Boolean.FALSE;
				}
			} else if (isSingleRule) {
				tempRule += rules.charAt(i);
			}
		}
		return Boolean.TRUE;
	}

	/**
	 * 生成随机UUID
	 * <p>
	 * 规则为 "[UUID]"
	 *
	 * @param paramList
	 * @return UUID字符串
	 */
	private static String getRandomUUID(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return IdUtil.randomUUID();
	}

	/**
	 * 生成随机日期
	 * <p>
	 * 规则为 "[Date]", "[Date|yyyymmdd]", "[Date|yyyy-mm-dd]", "[Date|yy-mm-dd]", ...
	 * yyyy为年，yy为年后两位，mm为月，dd为天
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomDate(List<String> paramList) {
		Assert.isTrue(paramList.size() == 1 || paramList.size() == 0);
		gregorianCalendar.set(gregorianCalendar.YEAR, RandomUtil.randomInt(1900, 2100));
		gregorianCalendar.set(gregorianCalendar.DAY_OF_YEAR,
				RandomUtil.randomInt(1, gregorianCalendar.getActualMaximum(gregorianCalendar.DAY_OF_YEAR)));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(paramList.size() == 1 ? paramList.get(0) : "yyyy-mm-dd");
		simpleDateFormat.setCalendar(gregorianCalendar);
		return simpleDateFormat.format(gregorianCalendar.getTime());
	}

	/**
	 * getRandomDate()函数使用的
	 */
	private static GregorianCalendar gregorianCalendar = new GregorianCalendar();

	/**
	 * 随机生成时间
	 * <p>
	 * 规则为 "[Time]", "[Time|hh:mm:ss]", "[Time|hh:mm]", ...
	 * hh为小时，mm为分钟，ss为秒
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomTime(List<String> paramList) {
		Assert.isTrue(paramList.size() == 1 || paramList.size() == 0);
		Random random = new Random();
		Time time = new Time(random.nextInt(24 * 60 * 60 * 1000));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(paramList.size() == 1 ? paramList.get(0) : "hh:mm:ss");
		return simpleDateFormat.format(time);
	}


	/**
	 * 随机生成日期时间
	 * <p>
	 * 规则为 "[Datetime]", "[Datetime|yyyy-mm-dd hh:mm:ss]", ...
	 * yyyy为年，yy为年后两位，mm为月，dd为天
	 * hh为小时，mm为分钟，ss为秒
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomDatetime(List<String> paramList) {
		Assert.isTrue(paramList.size() == 1 || paramList.size() == 0);
		long datetimeBegin = Timestamp.valueOf("1900-01-01 00:00:00").getTime();
		long datetimeEnd = Timestamp.valueOf("2100-12-31 23:59:59").getTime();
		Timestamp timestamp = new Timestamp(datetimeBegin + (long) ((datetimeEnd - datetimeBegin + 1) * Math.random()));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(paramList.size() == 1 ? paramList.get(0) : "yyyy-hh-mm hh:mm:ss");
		return simpleDateFormat.format(timestamp);
	}

	/**
	 * 随机生成（中国）大区
	 * <p>
	 * 规则为 "[RegionCN]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomRegionCN(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomEle(dataRegionCN);
	}

	private static List<String> dataRegionCN = ListUtil.of("东北", "华北", "华东", "华中", "华南", "西南", "西北");

	/**
	 * 随机生成（中国）省（或直辖市、自治区、特别行政区）
	 * <p>
	 * 规则为 "[ProvinceCN]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomProvinceCN(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomEle(dataProvinceCN);
	}

	private static List<String> dataProvinceCN = ListUtil.of("北京市", "天津市", "河北省", "山西省", "内蒙古自治区", "辽宁省", "吉林省", "黑龙江省", "上海市", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省", "广东省", "广西壮族自治区", "海南省", "河南省", "湖北省", "湖南省", "重庆市", "四川省", "贵州省", "云南省", "西藏自治区", "陕西省", "甘肃省", "青海省", "宁夏回族自治区", "新疆维吾尔自治区", "香港特别行政区", "澳门特别行政区", "台湾省");

	/**
	 * 随机生成（中国）市
	 * <p>
	 * 规则为 "[CityCN]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomCityCN(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomEle(dataCityCN);
	}

	private static List<String> dataCityCN = ListUtil.of("北京市", "天津市", "石家庄市", "辛集市", "藁城市", "晋州市", "新乐市", "鹿泉市", "唐山市", "遵化市", "迁安市", "秦皇岛市", "邯郸市", "武安市", "邢台市", "南宫市", "沙河市", "保定市", "新市区", "北市区", "南市区", "涿州市", "定州市", "安国市", "高碑店市", "张家口市", "承德市", "沧州市", "泊头市", "任丘市", "黄骅市", "河间市", "廊坊市", "霸州市", "三河市", "衡水市", "冀州市", "深州市", "太原市", "古交市", "大同市", "阳泉市", "长治市", "潞城市", "晋城市", "高平市", "朔州市", "晋中市", "介休市", "运城市", "永济市", "河津市", "忻州市", "原平市", "临汾市", "侯马市", "霍州市", "吕梁市", "孝义市", "汾阳市", "呼和浩特市", "包头市", "乌海市", "赤峰市", "通辽市", "霍林郭勒市", "鄂尔多斯市", "呼伦贝尔市", "满洲里市", "牙克石市", "扎兰屯市", "额尔古纳市", "根河市", "巴彦淖尔市", "乌兰察布市", "丰镇市", "乌兰浩特市", "阿尔山市", "二连浩特市", "锡林浩特市", "沈阳市", "新民市", "大连市", "瓦房店市", "普兰店市", "庄河市", "鞍山市", "海城市", "抚顺市", "本溪市", "丹东市", "东港市", "凤城市", "锦州市", "凌海市", "北镇市", "营口市", "西市区", "盖州市", "大石桥市", "阜新市", "辽阳市", "灯塔市", "盘锦市", "铁岭市", "调兵山市", "开原市", "朝阳市", "北票市", "凌源市", "葫芦岛市", "兴城市", "长春市", "九台市", "榆树市", "德惠市", "吉林市", "蛟河市", "桦甸市", "舒兰市", "磐石市", "四平市", "公主岭市", "双辽市", "辽源市", "通化市", "梅河口市", "集安市", "白山市", "临江市", "松原市", "扶余市", "白城市", "洮南市", "大安市", "延吉市", "图们市", "敦化市", "珲春市", "龙井市", "和龙市", "哈尔滨市", "双城市", "尚志市", "五常市", "齐齐哈尔市", "讷河市", "鸡西市", "虎林市", "密山市", "鹤岗市", "双鸭山市", "大庆市", "伊春市", "铁力市", "佳木斯市", "同江市", "富锦市", "七台河市", "牡丹江市", "绥芬河市", "海林市", "宁安市", "穆棱市", "黑河市", "北安市", "五大连池市", "绥化市", "安达市", "肇东市", "海伦市", "上海市", "南京市", "无锡市", "江阴市", "宜兴市", "徐州市", "新沂市", "邳州市", "常州市", "溧阳市", "金坛市", "苏州市", "常熟市", "张家港市", "昆山市", "太仓市", "南通市", "启东市", "如皋市", "海门市", "连云港市", "淮安市", "盐城市", "东台市", "大丰市", "扬州市", "仪征市", "高邮市", "镇江市", "丹阳市", "扬中市", "句容市", "泰州市", "兴化市", "靖江市", "泰兴市", "宿迁市", "杭州市", "建德市", "富阳市", "临安市", "宁波市", "余姚市", "慈溪市", "奉化市", "温州市", "瑞安市", "乐清市", "嘉兴市", "海宁市", "平湖市", "桐乡市", "湖州市", "绍兴市", "诸暨市", "上虞市", "嵊州市", "金华市", "兰溪市", "义乌市", "东阳市", "永康市", "衢州市", "江山市", "舟山市", "台州市", "温岭市", "临海市", "丽水市", "龙泉市", "合肥市", "芜湖市", "蚌埠市", "淮南市", "马鞍山市", "淮北市", "铜陵市", "安庆市", "桐城市", "黄山市", "滁州市", "天长市", "明光市", "阜阳市", "界首市", "宿州市", "巢湖市", "六安市", "亳州市", "池州市", "宣城市", "宁国市", "福州市", "福清市", "长乐市", "厦门市", "莆田市", "三明市", "永安市", "泉州市", "石狮市", "晋江市", "南安市", "漳州市", "龙海市", "南平市", "邵武市", "武夷山市", "建瓯市", "建阳市", "龙岩市", "漳平市", "宁德市", "福安市", "福鼎市", "南昌市", "景德镇市", "乐平市", "萍乡市", "九江市", "瑞昌市", "共青城市", "新余市", "鹰潭市", "贵溪市", "赣州市", "瑞金市", "南康市", "吉安市", "井冈山市", "宜春市", "丰城市", "樟树市", "高安市", "抚州市", "上饶市", "德兴市", "济南市", "市中区", "章丘市", "青岛市", "市南区", "市北区", "胶州市", "即墨市", "平度市", "莱西市", "淄博市", "枣庄市", "市中区", "滕州市", "东营市", "烟台市", "龙口市", "莱阳市", "莱州市", "蓬莱市", "招远市", "栖霞市", "海阳市", "潍坊市", "青州市", "诸城市", "寿光市", "安丘市", "高密市", "昌邑市", "济宁市", "市中区", "曲阜市", "兖州市", "邹城市", "泰安市", "新泰市", "肥城市", "威海市", "文登市", "荣成市", "乳山市", "日照市", "莱芜市", "临沂市", "德州市", "乐陵市", "禹城市", "聊城市", "临清市", "滨州市", "菏泽市", "郑州市", "巩义市", "荥阳市", "新密市", "新郑市", "登封市", "开封市", "洛阳市", "偃师市", "平顶山市", "舞钢市", "汝州市", "安阳市", "林州市", "鹤壁市", "新乡市", "卫辉市", "辉县市", "焦作市", "济源市", "沁阳市", "孟州市", "濮阳市", "许昌市", "禹州市", "长葛市", "漯河市", "三门峡市", "义马市", "灵宝市", "南阳市", "邓州市", "商丘市", "永城市", "信阳市", "周口市", "项城市", "驻马店市", "武汉市", "黄石市", "大冶市", "十堰市", "丹江口市", "宜昌市", "宜都市", "当阳市", "枝江市", "襄阳市", "老河口市", "枣阳市", "宜城市", "鄂州市", "荆门市", "钟祥市", "孝感市", "应城市", "安陆市", "汉川市", "荆州市", "沙市区", "石首市", "洪湖市", "松滋市", "黄冈市", "麻城市", "武穴市", "咸宁市", "赤壁市", "随州市", "广水市", "恩施市", "利川市", "仙桃市", "潜江市", "天门市", "长沙市", "浏阳市", "株洲市", "醴陵市", "湘潭市", "湘乡市", "韶山市", "衡阳市", "耒阳市", "常宁市", "邵阳市", "武冈市", "岳阳市", "汨罗市", "临湘市", "常德市", "津市市", "张家界市", "益阳市", "沅江市", "郴州市", "资兴市", "永州市", "怀化市", "洪江市", "娄底市", "冷水江市", "涟源市", "吉首市", "广州市", "增城市", "从化市", "韶关市", "乐昌市", "南雄市", "深圳市", "珠海市", "汕头市", "佛山市", "江门市", "台山市", "开平市", "鹤山市", "恩平市", "湛江市", "廉江市", "雷州市", "吴川市", "茂名市", "高州市", "化州市", "信宜市", "肇庆市", "高要市", "四会市", "惠州市", "梅州市", "兴宁市", "汕尾市", "陆丰市", "河源市", "阳江市", "阳春市", "清远市", "英德市", "连州市", "东莞市", "中山市", "潮州市", "揭阳市", "普宁市", "云浮市", "罗定市", "南宁市", "柳州市", "桂林市", "梧州市", "岑溪市", "北海市", "防城港市", "东兴市", "钦州市", "贵港市", "桂平市", "玉林市", "北流市", "百色市", "贺州市", "河池市", "宜州市", "来宾市", "合山市", "崇左市", "凭祥市", "海口市", "三亚市", "三沙市", "五指山市", "琼海市", "儋州市", "文昌市", "万宁市", "东方市", "重庆市", "成都市", "都江堰市", "彭州市", "邛崃市", "崇州市", "自贡市", "攀枝花市", "泸州市", "德阳市", "广汉市", "什邡市", "绵竹市", "绵阳市", "江油市", "广元市", "遂宁市", "内江市", "市中区", "乐山市", "市中区", "峨眉山市", "南充市", "阆中市", "眉山市", "宜宾市", "广安市", "华蓥市", "达州市", "万源市", "雅安市", "巴中市", "资阳市", "简阳市", "西昌市", "贵阳市", "清镇市", "六盘水市", "遵义市", "赤水市", "仁怀市", "安顺市", "铜仁市", "兴义市", "毕节市", "凯里市", "都匀市", "福泉市", "昆明市", "安宁市", "曲靖市", "宣威市", "玉溪市", "保山市", "昭通市", "丽江市", "普洱市", "临沧市", "楚雄市", "个旧市", "开远市", "蒙自市", "弥勒市", "文山市", "景洪市", "大理市", "瑞丽市", "芒市", "拉萨市", "日喀则市", "西安市", "铜川市", "宝鸡市", "咸阳市", "兴平市", "渭南市", "韩城市", "华阴市", "延安市", "汉中市", "榆林市", "安康市", "商洛市", "兰州市", "嘉峪关市", "金昌市", "白银市", "天水市", "武威市", "张掖市", "平凉市", "酒泉市", "玉门市", "敦煌市", "庆阳市", "定西市", "陇南市", "临夏市", "合作市", "西宁市", "海东市", "玉树市", "格尔木市", "德令哈市", "银川市", "灵武市", "石嘴山市", "吴忠市", "青铜峡市", "固原市", "中卫市", "乌鲁木齐市", "新市区", "克拉玛依市", "吐鲁番市", "哈密市", "昌吉市", "阜康市", "博乐市", "阿拉山口市", "库尔勒市", "阿克苏市", "阿图什市", "喀什市", "和田市", "伊宁市", "奎屯市", "塔城市", "乌苏市", "阿勒泰市", "石河子市", "阿拉尔市", "图木舒克市", "五家渠市", "台北市", "高雄市", "台南市", "新市区", "台中市", "南投市", "基隆市", "新竹市", "嘉义市", "新北市", "宜兰市", "竹北市", "中坜市", "平镇市", "杨梅市", "桃园市", "八德市", "苗栗市", "彰化市", "太保市", "朴子市", "斗六市", "屏东市", "台东市", "花莲市", "马公市");

	/**
	 * 随机生成（中国）县
	 * <p>
	 * 规则为 "[CountyCN]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomCountyCN(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomEle(dataCountyCN);
	}

	private static List<String> dataCountyCN = ListUtil.of("密云县", "延庆县", "宁河县", "静海县", "蓟县", "井陉县", "正定县", "栾城县", "行唐县", "灵寿县", "高邑县", "深泽县", "赞皇县", "无极县", "平山县", "元氏县", "赵县", "滦县", "滦南县", "乐亭县", "迁西县", "玉田县", "青龙满族自治县", "昌黎县", "抚宁县", "卢龙县", "邯郸县", "临漳县", "成安县", "大名县", "涉县", "磁县", "肥乡县", "永年县", "邱县", "鸡泽县", "广平县", "馆陶县", "魏县", "曲周县", "邢台县", "临城县", "内丘县", "柏乡县", "隆尧县", "任县", "南和县", "宁晋县", "巨鹿县", "新河县", "广宗县", "平乡县", "威县", "清河县", "临西县", "满城县", "清苑县", "涞水县", "阜平县", "徐水县", "定兴县", "唐县", "高阳县", "容城县", "涞源县", "望都县", "安新县", "易县", "曲阳县", "蠡县", "顺平县", "博野县", "雄县", "宣化县", "张北县", "康保县", "沽源县", "尚义县", "蔚县", "阳原县", "怀安县", "万全县", "怀来县", "涿鹿县", "赤城县", "崇礼县", "承德县", "兴隆县", "平泉县", "滦平县", "隆化县", "丰宁满族自治县", "宽城满族自治县", "围场满族蒙古族自治县", "沧县", "青县", "东光县", "海兴县", "盐山县", "肃宁县", "南皮县", "吴桥县", "献县", "孟村回族自治县", "固安县", "永清县", "香河县", "大城县", "文安县", "大厂回族自治县", "枣强县", "武邑县", "武强县", "饶阳县", "安平县", "故城县", "景县", "阜城县", "清徐县", "阳曲县", "娄烦县", "阳高县", "天镇县", "广灵县", "灵丘县", "浑源县", "左云县", "大同县", "平定县", "盂县", "长治县", "襄垣县", "屯留县", "平顺县", "黎城县", "壶关县", "长子县", "武乡县", "沁县", "沁源县", "沁水县", "阳城县", "陵川县", "泽州县", "山阴县", "应县", "右玉县", "怀仁县", "榆社县", "左权县", "和顺县", "昔阳县", "寿阳县", "太谷县", "祁县", "平遥县", "灵石县", "临猗县", "万荣县", "闻喜县", "稷山县", "新绛县", "绛县", "垣曲县", "夏县", "平陆县", "芮城县", "定襄县", "五台县", "代县", "繁峙县", "宁武县", "静乐县", "神池县", "五寨县", "岢岚县", "河曲县", "保德县", "偏关县", "曲沃县", "翼城县", "襄汾县", "洪洞县", "古县", "安泽县", "浮山县", "吉县", "乡宁县", "大宁县", "隰县", "永和县", "蒲县", "汾西县", "文水县", "交城县", "兴县", "临县", "柳林县", "石楼县", "岚县", "方山县", "中阳县", "交口县", "托克托县", "和林格尔县", "清水河县", "武川县", "固阳县", "林西县", "宁城县", "开鲁县", "五原县", "磴口县", "卓资县", "化德县", "商都县", "兴和县", "凉城县", "突泉县", "多伦县", "辽中县", "康平县", "法库县", "长海县", "台安县", "岫岩满族自治县", "抚顺县", "新宾满族自治县", "清原满族自治县", "本溪满族自治县", "桓仁满族自治县", "宽甸满族自治县", "黑山县", "义县", "阜新蒙古族自治县", "彰武县", "辽阳县", "大洼县", "盘山县", "铁岭县", "西丰县", "昌图县", "朝阳县", "建平县", "喀喇沁左翼蒙古族自治县", "绥中县", "建昌县", "农安县", "永吉县", "梨树县", "伊通满族自治县", "东丰县", "东辽县", "通化县", "辉南县", "柳河县", "抚松县", "靖宇县", "长白朝鲜族自治县", "前郭尔罗斯蒙古族自治县", "长岭县", "乾安县", "镇赉县", "通榆县", "汪清县", "安图县", "依兰县", "方正县", "宾县", "巴彦县", "木兰县", "通河县", "延寿县", "龙江县", "依安县", "泰来县", "甘南县", "富裕县", "克山县", "克东县", "拜泉县", "鸡东县", "萝北县", "绥滨县", "集贤县", "友谊县", "宝清县", "饶河县", "肇州县", "肇源县", "林甸县", "杜尔伯特蒙古族自治县", "嘉荫县", "桦南县", "桦川县", "汤原县", "抚远县", "勃利县", "东宁县", "林口县", "嫩江县", "逊克县", "孙吴县", "望奎县", "兰西县", "青冈县", "庆安县", "明水县", "绥棱县", "呼玛县", "塔河县", "漠河县", "崇明县", "丰县", "沛县", "睢宁县", "海安县", "如东县", "赣榆县", "东海县", "灌云县", "灌南县", "涟水县", "洪泽县", "盱眙县", "金湖县", "响水县", "滨海县", "阜宁县", "射阳县", "建湖县", "宝应县", "沭阳县", "泗阳县", "泗洪县", "桐庐县", "淳安县", "象山县", "宁海县", "洞头县", "永嘉县", "平阳县", "苍南县", "文成县", "泰顺县", "嘉善县", "海盐县", "德清县", "长兴县", "安吉县", "绍兴县", "新昌县", "武义县", "浦江县", "磐安县", "常山县", "开化县", "龙游县", "岱山县", "嵊泗县", "玉环县", "三门县", "天台县", "仙居县", "青田县", "缙云县", "遂昌县", "松阳县", "云和县", "庆元县", "景宁畲族自治县", "长丰县", "肥东县", "肥西县", "芜湖县", "繁昌县", "南陵县", "怀远县", "五河县", "固镇县", "凤台县", "当涂县", "濉溪县", "铜陵县", "怀宁县", "枞阳县", "潜山县", "太湖县", "宿松县", "望江县", "岳西县", "歙县", "休宁县", "黟县", "祁门县", "来安县", "全椒县", "定远县", "凤阳县", "临泉县", "太和县", "阜南县", "颍上县", "砀山县", "萧县", "灵璧县", "泗县", "庐江县", "无为县", "含山县", "和县", "寿县", "霍邱县", "舒城县", "金寨县", "霍山县", "涡阳县", "蒙城县", "利辛县", "东至县", "石台县", "青阳县", "郎溪县", "广德县", "泾县", "绩溪县", "旌德县", "闽侯县", "连江县", "罗源县", "闽清县", "永泰县", "平潭县", "仙游县", "明溪县", "清流县", "宁化县", "大田县", "尤溪县", "沙县", "将乐县", "泰宁县", "建宁县", "惠安县", "安溪县", "永春县", "德化县", "金门县", "云霄县", "漳浦县", "诏安县", "长泰县", "东山县", "南靖县", "平和县", "华安县", "顺昌县", "浦城县", "光泽县", "松溪县", "政和县", "长汀县", "永定县", "上杭县", "武平县", "连城县", "霞浦县", "古田县", "屏南县", "寿宁县", "周宁县", "柘荣县", "南昌县", "新建县", "安义县", "进贤县", "浮梁县", "莲花县", "上栗县", "芦溪县", "九江县", "武宁县", "修水县", "永修县", "德安县", "星子县", "都昌县", "湖口县", "彭泽县", "分宜县", "余江县", "赣县", "信丰县", "大余县", "上犹县", "崇义县", "安远县", "龙南县", "定南县", "全南县", "宁都县", "于都县", "兴国县", "会昌县", "寻乌县", "石城县", "吉安县", "吉水县", "峡江县", "新干县", "永丰县", "泰和县", "遂川县", "万安县", "安福县", "永新县", "奉新县", "万载县", "上高县", "宜丰县", "靖安县", "铜鼓县", "南城县", "黎川县", "南丰县", "崇仁县", "乐安县", "宜黄县", "金溪县", "资溪县", "东乡县", "广昌县", "上饶县", "广丰县", "玉山县", "铅山县", "横峰县", "弋阳县", "余干县", "鄱阳县", "万年县", "婺源县", "平阴县", "济阳县", "商河县", "桓台县", "高青县", "沂源县", "垦利县", "利津县", "广饶县", "长岛县", "临朐县", "昌乐县", "微山县", "鱼台县", "金乡县", "嘉祥县", "汶上县", "泗水县", "梁山县", "宁阳县", "东平县", "五莲县", "莒县", "沂南县", "郯城县", "沂水县", "苍山县", "费县", "平邑县", "莒南县", "蒙阴县", "临沭县", "陵县", "宁津县", "庆云县", "临邑县", "齐河县", "平原县", "夏津县", "武城县", "阳谷县", "莘县", "茌平县", "东阿县", "冠县", "高唐县", "惠民县", "阳信县", "无棣县", "沾化县", "博兴县", "邹平县", "曹县", "单县", "成武县", "巨野县", "郓城县", "鄄城县", "定陶县", "东明县", "中牟县", "杞县", "通许县", "尉氏县", "开封县", "兰考县", "孟津县", "新安县", "栾川县", "嵩县", "汝阳县", "宜阳县", "洛宁县", "伊川县", "宝丰县", "叶县", "鲁山县", "郏县", "安阳县", "汤阴县", "滑县", "内黄县", "浚县", "淇县", "新乡县", "获嘉县", "原阳县", "延津县", "封丘县", "长垣县", "辉县市", "修武县", "博爱县", "武陟县", "温县", "清丰县", "南乐县", "范县", "台前县", "濮阳县", "许昌县", "鄢陵县", "襄城县", "舞阳县", "临颍县", "渑池县", "陕县", "卢氏县", "南召县", "方城县", "西峡县", "镇平县", "内乡县", "淅川县", "社旗县", "唐河县", "新野县", "桐柏县", "民权县", "睢县", "宁陵县", "柘城县", "虞城县", "夏邑县", "罗山县", "光山县", "新县", "商城县", "固始县", "潢川县", "淮滨县", "息县", "扶沟县", "西华县", "商水县", "沈丘县", "郸城县", "淮阳县", "太康县", "鹿邑县", "西平县", "上蔡县", "平舆县", "正阳县", "确山县", "泌阳县", "汝南县", "遂平县", "新蔡县", "阳新县", "郧县", "郧西县", "竹山县", "竹溪县", "房县", "远安县", "兴山县", "秭归县", "长阳土家族自治县", "五峰土家族自治县", "南漳县", "谷城县", "保康县", "京山县", "沙洋县", "孝昌县", "大悟县", "云梦县", "公安县", "监利县", "江陵县", "团风县", "红安县", "罗田县", "英山县", "浠水县", "蕲春县", "黄梅县", "嘉鱼县", "通城县", "崇阳县", "通山县", "随县", "建始县", "巴东县", "宣恩县", "咸丰县", "来凤县", "鹤峰县", "长沙县", "宁乡县", "株洲县", "攸县", "茶陵县", "炎陵县", "湘潭县", "衡阳县", "衡南县", "衡山县", "衡东县", "祁东县", "邵东县", "新邵县", "邵阳县", "隆回县", "洞口县", "绥宁县", "新宁县", "城步苗族自治县", "岳阳县", "华容县", "湘阴县", "平江县", "安乡县", "汉寿县", "澧县", "临澧县", "桃源县", "石门县", "慈利县", "桑植县", "南县", "桃江县", "安化县", "桂阳县", "宜章县", "永兴县", "嘉禾县", "临武县", "汝城县", "桂东县", "安仁县", "祁阳县", "东安县", "双牌县", "道县", "江永县", "宁远县", "蓝山县", "新田县", "江华瑶族自治县", "中方县", "沅陵县", "辰溪县", "溆浦县", "会同县", "麻阳苗族自治县", "新晃侗族自治县", "芷江侗族自治县", "靖州苗族侗族自治县", "通道侗族自治县", "双峰县", "新化县", "泸溪县", "凤凰县", "花垣县", "保靖县", "古丈县", "永顺县", "龙山县", "始兴县", "仁化县", "翁源县", "乳源瑶族自治县", "新丰县", "南澳县", "遂溪县", "徐闻县", "电白县", "广宁县", "怀集县", "封开县", "德庆县", "博罗县", "惠东县", "龙门县", "梅县", "大埔县", "丰顺县", "五华县", "平远县", "蕉岭县", "海丰县", "陆河县", "紫金县", "龙川县", "连平县", "和平县", "东源县", "阳西县", "阳东县", "佛冈县", "阳山县", "连山壮族瑶族自治县", "连南瑶族自治县", "饶平县", "揭西县", "惠来县", "新兴县", "郁南县", "云安县", "武鸣县", "隆安县", "马山县", "上林县", "宾阳县", "横县", "柳江县", "柳城县", "鹿寨县", "融安县", "融水苗族自治县", "三江侗族自治县", "阳朔县", "灵川县", "全州县", "兴安县", "永福县", "灌阳县", "龙胜各族自治县", "资源县", "平乐县", "荔浦县", "恭城瑶族自治县", "苍梧县", "藤县", "蒙山县", "合浦县", "上思县", "灵山县", "浦北县", "平南县", "容县", "陆川县", "博白县", "兴业县", "田阳县", "田东县", "平果县", "德保县", "靖西县", "那坡县", "凌云县", "乐业县", "田林县", "西林县", "隆林各族自治县", "昭平县", "钟山县", "富川瑶族自治县", "南丹县", "天峨县", "凤山县", "东兰县", "罗城仫佬族自治县", "环江毛南族自治县", "巴马瑶族自治县", "都安瑶族自治县", "大化瑶族自治县", "忻城县", "象州县", "武宣县", "金秀瑶族自治县", "扶绥县", "宁明县", "龙州县", "大新县", "天等县", "定安县", "屯昌县", "澄迈县", "临高县", "白沙黎族自治县", "昌江黎族自治县", "乐东黎族自治县", "陵水黎族自治县", "保亭黎族苗族自治县", "琼中黎族苗族自治县", "潼南县", "铜梁县", "荣昌县", "璧山县", "梁平县", "城口县", "丰都县", "垫江县", "武隆县", "忠县", "开县", "云阳县", "奉节县", "巫山县", "巫溪县", "石柱土家族自治县", "秀山土家族苗族自治县", "酉阳土家族苗族自治县", "彭水苗族土家族自治县", "金堂县", "双流县", "郫县", "大邑县", "蒲江县", "新津县", "荣县", "富顺县", "米易县", "盐边县", "泸县", "合江县", "叙永县", "古蔺县", "中江县", "罗江县", "三台县", "盐亭县", "安县", "梓潼县", "北川羌族自治县", "平武县", "旺苍县", "青川县", "剑阁县", "苍溪县", "蓬溪县", "射洪县", "大英县", "威远县", "资中县", "隆昌县", "犍为县", "井研县", "夹江县", "沐川县", "峨边彝族自治县", "马边彝族自治县", "南部县", "营山县", "蓬安县", "仪陇县", "西充县", "仁寿县", "彭山县", "洪雅县", "丹棱县", "青神县", "宜宾县", "江安县", "长宁县", "高县", "珙县", "筠连县", "兴文县", "屏山县", "岳池县", "武胜县", "邻水县", "宣汉县", "开江县", "大竹县", "渠县", "荥经县", "汉源县", "石棉县", "天全县", "芦山县", "宝兴县", "通江县", "南江县", "平昌县", "安岳县", "乐至县", "汶川县", "理县", "茂县", "松潘县", "九寨沟县", "金川县", "小金县", "黑水县", "马尔康县", "壤塘县", "阿坝县", "若尔盖县", "红原县", "康定县", "泸定县", "丹巴县", "九龙县", "雅江县", "道孚县", "炉霍县", "甘孜县", "新龙县", "德格县", "白玉县", "石渠县", "色达县", "理塘县", "巴塘县", "乡城县", "稻城县", "得荣县", "木里藏族自治县", "盐源县", "德昌县", "会理县", "会东县", "宁南县", "普格县", "布拖县", "金阳县", "昭觉县", "喜德县", "冕宁县", "越西县", "甘洛县", "美姑县", "雷波县", "开阳县", "息烽县", "修文县", "水城县", "盘县", "遵义县", "桐梓县", "绥阳县", "正安县", "道真仡佬族苗族自治县", "务川仡佬族苗族自治县", "凤冈县", "湄潭县", "余庆县", "习水县", "平坝县", "普定县", "镇宁布依族苗族自治县", "关岭布依族苗族自治县", "紫云苗族布依族自治县", "江口县", "玉屏侗族自治县", "石阡县", "思南县", "印江土家族苗族自治县", "德江县", "沿河土家族自治县", "松桃苗族自治县", "兴仁县", "普安县", "晴隆县", "贞丰县", "望谟县", "册亨县", "安龙县", "大方县", "黔西县", "金沙县", "织金县", "纳雍县", "威宁彝族回族苗族自治县", "赫章县", "黄平县", "施秉县", "三穗县", "镇远县", "岑巩县", "天柱县", "锦屏县", "剑河县", "台江县", "黎平县", "榕江县", "从江县", "雷山县", "麻江县", "丹寨县", "荔波县", "贵定县", "瓮安县", "独山县", "平塘县", "罗甸县", "长顺县", "龙里县", "惠水县", "三都水族自治县", "晋宁县", "富民县", "宜良县", "石林彝族自治县", "嵩明县", "禄劝彝族苗族自治县", "寻甸回族彝族自治县", "马龙县", "陆良县", "师宗县", "罗平县", "富源县", "会泽县", "沾益县", "江川县", "澄江县", "通海县", "华宁县", "易门县", "峨山彝族自治县", "新平彝族傣族自治县", "元江哈尼族彝族傣族自治县", "施甸县", "腾冲县", "龙陵县", "昌宁县", "鲁甸县", "巧家县", "盐津县", "大关县", "永善县", "绥江县", "镇雄县", "彝良县", "威信县", "水富县", "玉龙纳西族自治县", "永胜县", "华坪县", "宁蒗彝族自治县", "宁洱哈尼族彝族自治县", "墨江哈尼族自治县", "景东彝族自治县", "景谷傣族彝族自治县", "镇沅彝族哈尼族拉祜族自治县", "江城哈尼族彝族自治县", "孟连傣族拉祜族佤族自治县", "澜沧拉祜族自治县", "西盟佤族自治县", "凤庆县", "云县", "永德县", "镇康县", "双江拉祜族佤族布朗族傣族自治县", "耿马傣族佤族自治县", "沧源佤族自治县", "双柏县", "牟定县", "南华县", "姚安县", "大姚县", "永仁县", "元谋县", "武定县", "禄丰县", "屏边苗族自治县", "建水县", "石屏县", "泸西县", "元阳县", "红河县", "金平苗族瑶族傣族自治县", "绿春县", "河口瑶族自治县", "砚山县", "西畴县", "麻栗坡县", "马关县", "丘北县", "广南县", "富宁县", "勐海县", "勐腊县", "漾濞彝族自治县", "祥云县", "宾川县", "弥渡县", "南涧彝族自治县", "巍山彝族回族自治县", "永平县", "云龙县", "洱源县", "剑川县", "鹤庆县", "梁河县", "盈江县", "陇川县", "泸水县", "福贡县", "贡山独龙族怒族自治县", "兰坪白族普米族自治县", "香格里拉县", "德钦县", "维西傈僳族自治县", "林周县", "当雄县", "尼木县", "曲水县", "堆龙德庆县", "达孜县", "墨竹工卡县", "昌都县", "江达县", "贡觉县", "类乌齐县", "丁青县", "察雅县", "八宿县", "左贡县", "芒康县", "洛隆县", "边坝县", "乃东县", "扎囊县", "贡嘎县", "桑日县", "琼结县", "曲松县", "措美县", "洛扎县", "加查县", "隆子县", "错那县", "浪卡子县", "南木林县", "江孜县", "定日县", "萨迦县", "拉孜县", "昂仁县", "谢通门县", "白朗县", "仁布县", "康马县", "定结县", "仲巴县", "亚东县", "吉隆县", "聂拉木县", "萨嘎县", "岗巴县", "那曲县", "嘉黎县", "比如县", "聂荣县", "安多县", "申扎县", "索县", "班戈县", "巴青县", "尼玛县", "双湖县", "普兰县", "札达县", "噶尔县", "日土县", "革吉县", "改则县", "措勤县", "林芝县", "工布江达县", "米林县", "墨脱县", "波密县", "察隅县", "朗县", "蓝田县", "周至县", "户县", "高陵县", "宜君县", "凤翔县", "岐山县", "扶风县", "眉县", "陇县", "千阳县", "麟游县", "凤县", "太白县", "三原县", "泾阳县", "乾县", "礼泉县", "永寿县", "彬县", "长武县", "旬邑县", "淳化县", "武功县", "华县", "潼关县", "大荔县", "合阳县", "澄城县", "蒲城县", "白水县", "富平县", "延长县", "延川县", "子长县", "安塞县", "志丹县", "吴起县", "甘泉县", "富县", "洛川县", "宜川县", "黄龙县", "黄陵县", "南郑县", "城固县", "洋县", "西乡县", "勉县", "宁强县", "略阳县", "镇巴县", "留坝县", "佛坪县", "神木县", "府谷县", "横山县", "靖边县", "定边县", "绥德县", "米脂县", "佳县", "吴堡县", "清涧县", "子洲县", "汉阴县", "石泉县", "宁陕县", "紫阳县", "岚皋县", "平利县", "镇坪县", "旬阳县", "白河县", "洛南县", "丹凤县", "商南县", "山阳县", "镇安县", "柞水县", "永登县", "皋兰县", "榆中县", "永昌县", "靖远县", "会宁县", "景泰县", "清水县", "秦安县", "甘谷县", "武山县", "张家川回族自治县", "民勤县", "古浪县", "天祝藏族自治县", "肃南裕固族自治县", "民乐县", "临泽县", "高台县", "山丹县", "泾川县", "灵台县", "崇信县", "华亭县", "庄浪县", "静宁县", "金塔县", "瓜州县", "肃北蒙古族自治县", "阿克塞哈萨克族自治县", "庆城县", "环县", "华池县", "合水县", "正宁县", "宁县", "镇原县", "通渭县", "陇西县", "渭源县", "临洮县", "漳县", "岷县", "成县", "文县", "宕昌县", "康县", "西和县", "礼县", "徽县", "两当县", "临夏县", "康乐县", "永靖县", "广河县", "和政县", "东乡族自治县", "积石山保安族东乡族撒拉族自治县", "临潭县", "卓尼县", "舟曲县", "迭部县", "玛曲县", "碌曲县", "夏河县", "大通回族土族自治县", "湟中县", "湟源县", "平安县", "民和回族土族自治县", "互助土族自治县", "化隆回族自治县", "循化撒拉族自治县", "门源回族自治县", "祁连县", "海晏县", "刚察县", "同仁县", "尖扎县", "泽库县", "河南蒙古族自治县", "共和县", "同德县", "贵德县", "兴海县", "贵南县", "玛沁县", "班玛县", "甘德县", "达日县", "久治县", "玛多县", "杂多县", "称多县", "治多县", "囊谦县", "曲麻莱县", "乌兰县", "都兰县", "天峻县", "永宁县", "贺兰县", "平罗县", "盐池县", "同心县", "西吉县", "隆德县", "泾源县", "彭阳县", "中宁县", "海原县", "乌鲁木齐县", "鄯善县", "托克逊县", "巴里坤哈萨克自治县", "伊吾县", "呼图壁县", "玛纳斯县", "奇台县", "吉木萨尔县", "木垒哈萨克自治县", "精河县", "温泉县", "轮台县", "尉犁县", "若羌县", "且末县", "焉耆回族自治县", "和静县", "和硕县", "博湖县", "温宿县", "库车县", "沙雅县", "新和县", "拜城县", "乌什县", "阿瓦提县", "柯坪县", "阿克陶县", "阿合奇县", "乌恰县", "疏附县", "疏勒县", "英吉沙县", "泽普县", "莎车县", "叶城县", "麦盖提县", "岳普湖县", "伽师县", "巴楚县", "塔什库尔干塔吉克自治县", "和田县", "墨玉县", "皮山县", "洛浦县", "策勒县", "于田县", "民丰县", "伊宁县", "察布查尔锡伯自治县", "霍城县", "巩留县", "新源县", "昭苏县", "特克斯县", "尼勒克县", "额敏县", "沙湾县", "托里县", "裕民县", "和布克赛尔蒙古自治县", "布尔津县", "富蕴县", "福海县", "哈巴河县", "青河县", "吉木乃县", "金门县", "南投县", "宜兰县", "新竹县", "桃园县", "苗栗县", "彰化县", "嘉义县", "云林县", "屏东县", "台东县", "花莲县", "澎湖县", "连江县");

	/**
	 * 随机生成（中国）邮政编码
	 * <p>
	 * 规则为 "[PostalCodeCN]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomPostalCodeCN(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		String result = "";
		for (int i = 0; i < 6; ++i) {
			result += RandomUtil.randomInt(0, 9);
		}
		return result;
	}

	/**
	 * 随机生成（英文）姓
	 * <p>
	 * 规则为 "[FirstNameEN]", "[FirstNameEN|male]", "[FirstNameEN|female]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomFirstNameEN(List<String> paramList) {
		Assert.isTrue(paramList.size() == 0 || paramList.size() == 1);
		if (paramList.size() == 1) {
			Assert.isTrue("male".equals(paramList.get(0)) || "female".equals(paramList.get(0)));
		}
		if (paramList.size() == 0) {
			return RandomUtil.randomBoolean() ? RandomUtil.randomEle(dataFirstNameENFemale) : RandomUtil.randomEle(dataFirstNameENMale);
		} else {
			return paramList.get(0) == "male" ? RandomUtil.randomEle(dataFirstNameENMale) : RandomUtil.randomEle(dataFirstNameENFemale);
		}
	}

	private static List<String> dataFirstNameENMale = ListUtil.of("James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric");
	private static List<String> dataFirstNameENFemale = ListUtil.of("Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer", "Maria", "Susan", "Margaret", "Dorothy", "Lisa", "Nancy", "Karen", "Betty", "Helen", "Sandra", "Donna", "Carol", "Ruth", "Sharon", "Michelle", "Laura", "Sarah", "Kimberly", "Deborah", "Jessica", "Shirley", "Cynthia", "Angela", "Melissa", "Brenda", "Amy", "Anna");

	/**
	 * 随机生成（英文）名
	 * <p>
	 * 规则为 "[LastNameEN]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomLastNameEN(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomEle(dataLastNameEN);
	}

	private static List<String> dataLastNameEN = ListUtil.of("Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Garcia", "Rodriguez", "Wilson", "Martinez", "Anderson", "Taylor", "Thomas", "Hernandez", "Moore", "Martin", "Jackson", "Thompson", "White", "Lopez", "Lee", "Gonzalez", "Harris", "Clark", "Lewis", "Robinson", "Walker", "Perez", "Hall", "Young", "Allen");

	/**
	 * 随机生成（英文）姓名
	 * <p>
	 * 规则为"[NameEN]", "[NameEN|male]", "[NameEN|female]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomNameEN(List<String> paramList) {
		Assert.isTrue(paramList.size() == 0 || paramList.size() == 1);
		if (paramList.size() == 1) {
			Assert.isTrue("male".equals(paramList.get(0)) || "female".equals(paramList.get(0)));
		}
		if (paramList.size() == 0) {
			return getRandomFirstNameEN(ListUtil.of()) + " " + getRandomLastNameEN(ListUtil.of());
		} else {
			return paramList.get(0) == "male" ? getRandomFirstNameEN(ListUtil.of("male")) : getRandomFirstNameEN(ListUtil.of("female"))
					+ " " + getRandomLastNameEN(ListUtil.of());
		}
	}

	/**
	 * 随机生成（中文）姓
	 * <p>
	 * 规则为 "[FirstNameCN]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomFirstNameCN(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomEle(dataFirstNameCN);
	}

	private static List<String> dataFirstNameCN = ListUtil.of("王", "李", "张", "刘", "陈", "杨", "赵", "黄", "周", "吴", "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗", "梁", "宋", "郑", "谢", "韩", "唐", "冯", "于", "董", "萧", "程", "曹", "袁", "邓", "许", "傅", "沈", "曾", "彭", "吕", "苏", "卢", "蒋", "蔡", "贾", "丁", "魏", "薛", "叶", "阎", "余", "潘", "杜", "戴", "夏", "锺", "汪", "田", "任", "姜", "范", "方", "石", "姚", "谭", "廖", "邹", "熊", "金", "陆", "郝", "孔", "白", "崔", "康", "毛", "邱", "秦", "江", "史", "顾", "侯", "邵", "孟", "龙", "万", "段", "雷", "钱", "汤", "尹", "黎", "易", "常", "武", "乔", "贺", "赖", "龚", "文");

	/**
	 * 随机生成（中文）名
	 * <p>
	 * 规则为 "[LastNameCN]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomLastNameCN(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomEle(dataLastNameCN);
	}

	private static List<String> dataLastNameCN = ListUtil.of("伟", "芳", "娜", "秀英", "敏", "静", "丽", "强", "磊", "军", "洋", "勇", "艳", "杰", "娟", "涛", "明", "超", "秀兰", "霞", "平", "刚", "桂英");

	/**
	 * 随机生成（中文）姓名
	 * <p>
	 * 规则为 "[NameCN]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomNameCN(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return getRandomFirstNameCN(ListUtil.of()) + getRandomLastNameCN(ListUtil.of());
	}

	/**
	 * 生成随机（中文）颜色名称
	 * <p>
	 * 规则为 "[ColorCN]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomColorCN(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomEle(dataColorCN);
	}

	private static List<String> dataColorCN = ListUtil.of("黑色", "蓝色", "水", "紫红色", "灰色", "绿色", "石灰", "栗色", "海军", "橄榄", "橙", "紫", "红", "白", "银", "蓝绿色", "黄色");

	/**
	 * 生成随机（英文）颜色名称
	 * <p>
	 * 规则为 "[ColorEN]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomColorEN(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomEle(dataColorEN);
	}

	private static List<String> dataColorEN = ListUtil.of("monochrome", "red", "orange", "yellow", "green", "blue", "purple", "pink", "navy", "teal", "aqua", "olive", "lime", "maroon", "fuchsia", "silver", "gray", "black", "white");

	/**
	 * 生成随机IP地址
	 * <p>
	 * 规则为 "[IP]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomIP(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomInt(256) + "." +
				RandomUtil.randomInt(256) + "." +
				RandomUtil.randomInt(256) + "." +
				RandomUtil.randomInt(256);
	}

	/**
	 * 生成随机端口号
	 * <p>
	 * 规则为 "[Port]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomPort(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return String.valueOf(RandomUtil.randomInt(65535));
	}

	/**
	 * 生成随机网络地址
	 * <p>
	 * 规则为 "[NetAddr]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomNetAddr(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return getRandomIP(ListUtil.of()) + ":" + getRandomPort(ListUtil.of());
	}

	/**
	 * 随机生成一个域名
	 * <p>
	 * 规则为 "[Domain]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomDomain(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomString(5) + "." + RandomUtil.randomEle(dataTLD);
	}

	private static List<String> dataTLD = ListUtil.of("com", "net", "org", "edu", "gov", "int", "mil", "cn", "com.cn", "net.cn", "gov.cn", "org.cn", "tel", "biz", "cc", "tv", "info", "name", "hk", "mobi", "asia", "cd", "travel", "pro", "museum", "coop", "aero", "ad", "ae", "af", "ag", "ai", "al", "am", "an", "ao", "aq", "ar", "as", "at", "au", "aw", "az", "ba", "bb", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz", "ca", "cc", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cq", "cr", "cu", "cv", "cx", "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "es", "et", "ev", "fi", "fj", "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gh", "gi", "gl", "gm", "gn", "gp", "gr", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "in", "io", "iq", "ir", "is", "it", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw", "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md", "mg", "mh", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mv", "mw", "mx", "my", "mz", "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nt", "nu", "nz", "om", "qa", "pa", "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "pt", "pw", "py", "re", "ro", "ru", "rw", "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "st", "su", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj", "tk", "tm", "tn", "to", "tp", "tr", "tt", "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "va", "vc", "ve", "vg", "vn", "vu", "wf", "ws", "ye", "yu", "za", "zm", "zr", "zw");

	/**
	 * 随机生成一个URL
	 * <p>
	 * 规则为 "[URL]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomURL(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomEle(ListUtil.of("http", "https")) + "://" + getRandomDomain(ListUtil.of()) + "/" + RandomUtil.randomString(5);
	}

	/**
	 * 随机生成一个邮件地址
	 * <p>
	 * 规则为 "[EmailAddr]"
	 *
	 * @param paramList
	 * @return
	 */
	private static String getRandomEmailAddr(List<String> paramList) {
		Assert.isTrue(paramList.isEmpty());
		return RandomUtil.randomString(5).toLowerCase() + "." + RandomUtil.randomString(5).toUpperCase() +
				"@" + RandomUtil.randomEle(dataEmailDomain);
	}

	private static List<String> dataEmailDomain = ListUtil.of("126.com", "163.com", "aim.com", "alice.it", "aliceadsl.fr", "aliyun.com", "aol.com", "aol.it", "arcor.de", "arnet.com.ar", "att.net", "bellsouth.net", "bigpond.com", "bigpond.net.au", "blueyonder.co.uk", "bluewinch.ch", "bol.com.br", "bt.com", "btinternet.com", "centurytel.net", "charter.net", "chello.nl", "club-internet.fr", "comcast.net", "cox.net", "daum.net", "earthlink.net", "email.com", "email.it", "facebook.com", "fastmail.fm", "fibertel.com.ar", "foxmail.com", "free.fr", "freenet.de", "freeserve.co.uk", "frontiernet.net", "gmail.com", "games.com", "globo.com", "globomail.com", "gmx.com", "gmx.de", "gmx.fr", "gmx.net", "google.com", "googlemail.com", "hotmail.com", "hanmail.net", "hetnet.nl", "hotmail.be", "hotmail.co.uk", "hotmail.com.ar", "hotmail.com.br", "hotmail.com.mx", "hotmail.de", "hotmail.es", "hotmail.fr", "hotmail.it", "hush.com", "hushmail.com", "icloud.com", "ig.com.br", "iname.com", "inbox.com", "itelefonica.com.br", "juno.com", "live.com", "laposte.net", "lavabit.com", "libero.it", "list.ru", "live.be", "live.ca", "live.co.uk", "live.com.ar", "live.com.au", "live.com.mx", "live.de", "live.fr", "live.it", "live.nl", "love.com", "mac.com", "mail.com", "mail.ru", "me.com", "msn.com", "nate.com", "naver.com", "neuf.fr", "ntlworld.com", "outlook.com", "o2.co.uk", "oi.com.br", "online.de", "optionline.net", "optusnet.com.au", "orange.fr", "orange.net", "outlook.com.br", "planet.nl", "pobox.com", "poste.it", "prodigy.net.mx", "protonmail.com", "qq.com", "r7.com", "rambler.ru", "rediffmail.com", "rocketmail.com", "safe-mail.net", "sbcglobal.net", "sfr.fr", "shaw.ca", "sina.cn", "sina.com", "sky.com", "skynet.be", "speedy.com.ar", "sympatico.ca", "t-online.de", "talktalk.co.uk", "telenet.be", "teletu.it", "terra.com.br", "tin.it", "tiscali.co.uk", "tiscali.it", "tvcablenet.be", "uol.com.br", "voila.fr", "verizon.net", "virgilio.it", "virgin.net", "virginmedia.com", "voo.be", "wanadoo.co.uk", "wanadoo.fr", "web.de", "windstream.net", "wow.com", "yahoo.com", "ya.ru", "yahoo.ca", "yahoo.co.id", "yahoo.co.in", "yahoo.co.jp", "yahoo.co.kr", "yahoo.co.uk", "yahoo.com.ar", "yahoo.com.au", "yahoo.com.br", "yahoo.com.mx", "yahoo.com.ph", "yahoo.com.sg", "yahoo.de", "yahoo.es", "yahoo.fr", "yahoo.it", "yandex.com", "yandex.ru", "ygm.com", "ymail.com", "zipmail.com.br", "zoho.com", "zonnet.nl");
}
