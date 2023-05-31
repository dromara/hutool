package org.dromara.hutool.extra.vehicle.vin;

import java.time.Year;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * VIS
 *
 * @author dax
 * @since 2023 /5/15 12:07
 */
class Vis implements VinCode {

	private static final int YEAR_LOOP = 30;
	private static final List<String> YEAR_ID = Arrays.asList(
		"A", "B", "C", "D", "E",
		"F", "G", "H", "J", "K",
		"L", "M", "N", "P", "R",
		"S", "T", "V", "W", "X",
		"Y", "1", "2", "3", "4",
		"5", "6", "7", "8", "9");
	private static final Map<String, Integer> YEAR_MAP = new HashMap<>();

	static {
		for (int i = 0; i < YEAR_ID.size(); i++) {
			YEAR_MAP.put(YEAR_ID.get(i), i);
		}
	}

	private final AlphanumericVinCode year;
	private final AlphanumericVinCode oem;
	private final List<MaskVinCode> prodNo;
	private final String prodNoStr;
	private final String code;
	private final int mask;

	/**
	 * Instantiates a new Vis.
	 *
	 * @param year   the year
	 * @param oem    the oem
	 * @param prodNo the prod no
	 */
	Vis(AlphanumericVinCode year, AlphanumericVinCode oem, List<MaskVinCode> prodNo) {
		this.year = year;
		this.oem = oem;
		this.prodNo = prodNo;
		this.prodNoStr = prodNo.stream().map(MaskVinCode::getCode).collect(Collectors.joining());
		this.code = year.getCode() + oem.getCode() + prodNo.stream()
			.map(MaskVinCode::getCode)
			.collect(Collectors.joining());
		this.mask = year.getMask() + oem.getMask() + prodNo.stream().mapToInt(MaskVinCode::getMask).sum();
	}

	/**
	 * 从VIN生成VIS
	 *
	 * @param vin the vin
	 * @return the vis
	 */
	static Vis from(String vin) {
		AlphanumericVinCode year = new AlphanumericVinCode(String.valueOf(vin.charAt(9)), 9);
		AlphanumericVinCode factory = new AlphanumericVinCode(String.valueOf(vin.charAt(10)), 10);
		List<MaskVinCode> codes = IntStream.range(11, 17)
			.mapToObj(index -> index < 14 ? new AlphanumericVinCode(String.valueOf(vin.charAt(index)), index) :
				new NumericVinCode(String.valueOf(vin.charAt(index)), index))
			.collect(Collectors.toList());
		return new Vis(year, factory, codes);
	}

	/**
	 * Gets year.
	 *
	 * @param multiple the multiple
	 * @return the year
	 */
	Year getYear(int multiple) {
		int year = 1980 + YEAR_LOOP * multiple + YEAR_MAP.get(this.year.getCode()) % YEAR_LOOP;
		return Year.of(year);
	}

	/**
	 * Gets oem.
	 *
	 * @return the oem
	 */
	AlphanumericVinCode getOem() {
		return oem;
	}


	/**
	 * Gets prod no.
	 *
	 * @return the prod no
	 */
	List<MaskVinCode> getProdNo() {
		return prodNo;
	}

	/**
	 * Gets prod no str.
	 *
	 * @return the prod no str
	 */
	String getProdNoStr() {
		return prodNoStr;
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
	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "Vis{" +
			"year=" + year +
			", oem=" + oem +
			", prodNo=" + prodNo +
			", code='" + code + '\'' +
			", mask=" + mask +
			'}';
	}
}
