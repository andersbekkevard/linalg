package utils.archive;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArchiveSnippets {

	// region From OriginalMatrix' old toString() method
	/*
	 * Adjuster for floating point arithmetic so we avoid -0.0 in the toStrig()
	 */
	private static final Function<Double, Double> floatingPointAdjuster = (d) -> {
		if (Math.abs(d) < 1e-10)
			return 0.0;
		return d;
	};

	// Swap .0f to .1f to show decimal
	private static final Function<Double, String> doubleToRoundedString = (d) -> String.format("%.1f", d);

	private static final Function<double[], String> arrayToString = (row) -> "["
			+ Arrays.stream(row).boxed().map(floatingPointAdjuster)
					.map(doubleToRoundedString).collect(Collectors.joining(", "))
			+ "]";

	// endregion
}
