package org.dromara.hutool.extra.vehicle.vin;

/**
 * Vin掩码枚举
 *
 * @author dax
 * @since 2023 /5/15 10:49
 */
enum VinCodeMaskEnum {
	/**
	 * A 掩码.
	 */
	A(1),
	/**
	 * B 掩码.
	 */
	B(2),
	/**
	 * C 掩码.
	 */
	C(3),
	/**
	 * D 掩码.
	 */
	D(4),
	/**
	 * E 掩码.
	 */
	E(5),
	/**
	 * F 掩码.
	 */
	F(6),
	/**
	 * G 掩码.
	 */
	G(7),
	/**
	 * H 掩码.
	 */
	H(8),
	/**
	 * J 掩码.
	 */
	J(1),
	/**
	 * K 掩码.
	 */
	K(2),
	/**
	 * L 掩码.
	 */
	L(3),
	/**
	 * M 掩码.
	 */
	M(4),
	/**
	 * N 掩码.
	 */
	N(5),
	/**
	 * P 掩码.
	 */
	P(7),
	/**
	 * R 掩码.
	 */
	R(9),
	/**
	 * S 掩码.
	 */
	S(2),
	/**
	 * T 掩码.
	 */
	T(3),
	/**
	 * U 掩码.
	 */
	U(4),
	/**
	 * V 掩码.
	 */
	V(5),
	/**
	 * W 掩码.
	 */
	W(6),
	/**
	 * X 掩码.
	 */
	X(7),
	/**
	 * Y 掩码.
	 */
	Y(8),
	/**
	 * Z 掩码.
	 */
	Z(9);

	private final int maskCode;

	VinCodeMaskEnum(int maskCode) {
		this.maskCode = maskCode;
	}

	/**
	 * 获取掩码值.
	 *
	 * @return the mask code
	 */
	public int getMaskCode() {
		return maskCode;
	}
}
