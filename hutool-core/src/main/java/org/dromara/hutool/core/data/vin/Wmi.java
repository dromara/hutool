package org.dromara.hutool.core.data.vin;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The type Wmi.
 *
 * @author dax
 * @since 2023 /5/15 9:25
 */
class Wmi implements VinCode {
	private final String code;
	private final int mask;
	/**
	 * The Wmi codes.
	 */
	List<AlphanumericVinCode> wmiCodes;

	/**
	 * Instantiates a new Wmi.
	 *
	 * @param wmiCodes the wmi codes
	 */
	Wmi(List<AlphanumericVinCode> wmiCodes) {
		this.wmiCodes = wmiCodes;
		AtomicInteger mask = new AtomicInteger();
		this.code = wmiCodes.stream()
			.peek(alphanumericCode -> mask.addAndGet(alphanumericCode.getMask()))
			.map(AlphanumericVinCode::getCode)
			.collect(Collectors.joining());
		this.mask = mask.get();
	}

	/**
	 * 从VIN生成WMI
	 *
	 * @param vin the vin
	 * @return the wmi
	 */
	static Wmi from(String vin) {
		List<AlphanumericVinCode> codes = IntStream.range(0, 3)
			.mapToObj(index ->
				new AlphanumericVinCode(String.valueOf(vin.charAt(index)), index))
			.collect(Collectors.toList());
		return new Wmi(codes);
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "Wmi{" +
			"wmiCodes=" + wmiCodes +
			", code='" + code + '\'' +
			", mask=" + mask +
			'}';
	}

	/**
	 * Gets mask.
	 *
	 * @return the mask
	 */
	int getMask() {
		return mask;
	}


	/**
	 * 是否是年产量小于1000的车辆制造厂
	 *
	 * @return the boolean
	 */
	boolean isLessThan1000() {
		return this.code.matches("^.*9$");
	}
}
