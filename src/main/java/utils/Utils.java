package utils;

public class Utils {
	private static final double TOLERANCE = 1e-5;

	public static boolean isZero(double value) {
		return Math.abs(value) < TOLERANCE;
	}

	public static boolean isZero(double value, double specifiedTolerance) {
		if (specifiedTolerance <= 0)
			throw new IllegalArgumentException("Tolerance must be greater than one");
		return Math.abs(value) < specifiedTolerance;
	}

	public static boolean isOne(double value) {
		return isZero(value - 1);
	}

	public static boolean isPositive(double value) {
		return value == Math.abs(value);
	}

	public static boolean isHalfPi(double value) {
		return isZero(value - Math.PI / 2);
	}

}
