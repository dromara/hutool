package org.dromara.hutool.core.data.vin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * VDS
 *
 * @author dax
 * @since 2023 /5/15 9:37
 */
class Vds implements VinCode {

	private final List<AlphanumericVinCode> vdCode;
	private final AlphanumericVinCode checksum;
	private final String code;
	private final int mask;

	/**
	 * Instantiates a new Vds.
	 *
	 * @param vdCode the vd code
	 */
	Vds(List<AlphanumericVinCode> vdCode) {
		this.vdCode = vdCode.subList(0, 5);
		this.checksum = vdCode.get(5);
		this.code = vdCode.stream().map(AlphanumericVinCode::getCode).collect(Collectors.joining());
		this.mask = vdCode.stream().mapToInt(AlphanumericVinCode::getMask).sum();
	}

	/**
	 * 从VIN生成VDS
	 *
	 * @param vin the vin
	 * @return the vds
	 */
	public static Vds from(String vin) {
		List<AlphanumericVinCode> vdCode = IntStream.range(3, 9)
			.mapToObj(index ->
				new AlphanumericVinCode(String.valueOf(vin.charAt(index)), index))
			.collect(Collectors.toList());
		return new Vds(vdCode);
	}

	/**
	 * Gets vd code.
	 *
	 * @return the vd code
	 */
	List<AlphanumericVinCode> getVdCode() {
		return vdCode;
	}

	/**
	 * Gets checksum.
	 *
	 * @return the checksum
	 */
	AlphanumericVinCode getChecksum() {
		return checksum;
	}

	@Override
	public String getCode() {
		return code;
	}

	/**
	 * Gets mask.
	 *
	 * @return the mask
	 */
	int getMask() {
		return mask;
	}

	@Override
	public String toString() {
		return "Vds{" +
			"vdCode=" + vdCode +
			", checksum=" + checksum +
			", code='" + code + '\'' +
			", mask=" + mask +
			'}';
	}
}
