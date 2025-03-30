package utils.records;

import java.util.List;

import representations.ElementaryMatrix;
import representations.Matrix;

public record ReductionResult(Matrix reducedMatrix, List<ElementaryMatrix> operations) {
};
