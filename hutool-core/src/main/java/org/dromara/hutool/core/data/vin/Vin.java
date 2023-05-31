package org.dromara.hutool.core.data.vin;


import java.time.Year;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * VIN是Vehicle Identification Number的缩写，即车辆识别号码。VIN码是全球通行的车辆唯一标识符，由17位数字和字母组成。
 * <p>
 * 不同位数代表着不同意义，具体解释如下：
 * <ul>
 *     <li>1-3位：制造商标示符，代表车辆制造商信息</li>
 *     <li>4-8位：车型识别代码，代表车辆品牌、车系、车型及其排量等信息</li>
 *     <li>9位：校验位，通过公式计算出来，用于验证VIN码的正确性</li>
 *     <li>10位：年份代号，代表车辆生产的年份</li>
 *     <li>11位：工厂代码，代表车辆生产工厂信息</li>
 *     <li>12-17位：流水号，代表车辆的生产顺序号</li>
 * </ul>
 * VIN码可以找到汽车详细的个人、工程、制造方面的信息，是判定一个汽车合法性及其历史的重要依据。
 * <p>
 * 本实现参考以下标准：
 * <ul>
 *     <li><a href="https://www.iso.org/standard/52200.html">ISO 3779</a></li>
 *     <li><a href="http://www.catarc.org.cn/upload/202004/24/202004241005284241.pdf">车辆识别代号管理办法</a></li>
 *     <li><a href="https://en.wikipedia.org/wiki/Vehicle_identification_number">Wikipedia</a></li>
 * </ul>
 *
 * @author dax
 * @since 2023 /5/15 9:40
 */
public final class Vin implements VinCode {
	private static final Pattern GB16735_VIN_REGEX = Pattern.compile("^[A-HJ-NPR-Z\\d]{8}[X\\d][A-HJ-NPR-Z\\d]{8}$");
	private final Wmi wmi;
	private final Vds vds;
	private final Vis vis;
	private final String code;

	/**
	 * Instantiates a new Vin.
	 *
	 * @param wmi  the wmi
	 * @param vds  the vds
	 * @param vis  the vis
	 * @param code the code
	 */
	Vin(Wmi wmi, Vds vds, Vis vis, String code) {
		this.wmi = wmi;
		this.vds = vds;
		this.vis = vis;
		this.code = code;
	}

	/**
	 * 从VIN字符串生成{@code Vin}对象
	 *
	 * @param vin VIN字符串
	 * @return VIN对象 vin
	 */
	public static Vin of(String vin) throws IllegalArgumentException {
		if (!GB16735_VIN_REGEX.matcher(vin).matches()) {
			throw new IllegalArgumentException("VIN格式不正确，需满足正则 " + GB16735_VIN_REGEX.pattern());
		}
		Wmi wmi = Wmi.from(vin);
		Vds vds = Vds.from(vin);
		Vis vis = Vis.from(vin);
		int factor = (wmi.getMask() + vds.getMask() + vis.getMask()) % 11;
		String checked = factor != 10 ? String.valueOf(factor) : "X";
		if (!Objects.equals(vds.getChecksum().getCode(), checked)) {
			throw new IllegalArgumentException("VIN校验不通过");
		}
		return new Vin(wmi, vds, vis, vin);
	}

	/**
	 * 仅判断一个字符串是否符合VIN规则
	 *
	 * @param vinStr vinStr
	 * @return {@code true} 符合
	 */
	public static boolean isValidVinCode(String vinStr) {
		if (GB16735_VIN_REGEX.matcher(vinStr).matches()) {
			int weights = IntStream.range(0, 17)
				.map(i -> calculateWeight(vinStr, i))
				.sum();
			int factor = weights % 11;
			char checked = factor != 10 ? (char) (factor + '0') : 'X';
			return vinStr.charAt(8) == checked;
		}
		return false;
	}

	private static int calculateWeight(String vinStr, int i) {
		char c = vinStr.charAt(i);
		Integer factor = MaskVinCode.WEIGHT_FACTORS.get(i);
		return c <= '9' ?
			Character.getNumericValue(c) * factor :
			VinCodeMaskEnum.valueOf(String.valueOf(c)).getMaskCode() * factor;
	}

	/**
	 * 标识一个国家或者地区
	 *
	 * @return the string
	 */
	public String geoCode() {
		String wmiCode = this.wmi.getCode();
		return wmiCode.substring(0, 2);
	}

	/**
	 * 制造厂标识码
	 * <p>
	 * 年产量大于1000为符合GB16737规定的{@link Wmi}，年产量小于1000固定为9，需要结合VIN的第12、13、14位字码确定唯一
	 *
	 * @return 主机厂识别码 string
	 */
	public String manufacturerCode() {
		String wmiCode = this.wmi.getCode();
		return isLessThan1000() ?
			wmiCode.concat(this.vis.getProdNoStr().substring(0, 3)) : wmiCode;
	}

	/**
	 * 是否是年产量小于1000的车辆制造厂
	 *
	 * @return 是否年产量小于1000 boolean
	 */
	public boolean isLessThan1000() {
		return this.wmi.isLessThan1000();
	}

	/**
	 * 获取WMI码
	 *
	 * @return WMI值 string
	 */
	public String wmiCode() {
		return wmi.getCode();
	}

	/**
	 * 获取车辆特征描述码
	 *
	 * @return VDS值 string
	 */
	public String vdsCode() {
		return this.vds.getCode().substring(0, 5);
	}

	/**
	 * 获取默认车型年份，接近于本年度
	 *
	 * @return the int
	 */
	public Year defaultYear() {
		return this.year(1);
	}

	/**
	 * 获取车型年份
	 * <p>
	 * 自1980年起，30年一个周期
	 *
	 * @param multiple 1 代表从 1980年开始的第一个30年
	 * @return 返回年份对象 year
	 * @see <a href="https://en.wikipedia.org/wiki/Vehicle_identification_number#Model_year_encoding">年份编码模型</a>
	 */
	public Year year(int multiple) {
		return this.vis.getYear(multiple);
	}

	/**
	 * 生产序号
	 * <p>
	 * 年产量大于1000为6位，年产量小于1000的为3位
	 *
	 * @return 生产序号 string
	 */
	public String prodNo() {
		String prodNoStr = this.vis.getProdNoStr();
		return isLessThan1000() ?
			prodNoStr.substring(3, 6) : prodNoStr;
	}

	/**
	 * 获取装配厂字码
	 *
	 * @return 由厂家自行定义的装配厂字码 string
	 */
	public String oemCode() {
		return this.vis.getOem().getCode();
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "Vin{" +
			"wmi=" + wmi +
			", vds=" + vds +
			", vis=" + vis +
			", code='" + code + '\'' +
			'}';
	}

}
