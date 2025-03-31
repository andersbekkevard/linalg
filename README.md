# Java Linear Algebra Library

A Java-based linear algebra library built using object-oriented principles, created to support core linear algebra operations for educational and practical purposes.

## Features

- **Matrix Representation**: Flexible matrix interface with multiple implementations (standard, transposed, elementary).
- **Vector Representation**: Vector class with operations like scaling, addition, subtraction, and normalization.
- **Matrix Operations**:
  - Matrix multiplication
  - Matrix transposition
  - Matrix addition and subtraction
  - Scalar multiplication
- **Row Reduction**:
  - Converts matrices to row echelon form using a sequence of elementary row operations.
- **Inverse Calculation**:
  - Computes inverse of square matrices using row reduction and elementary matrices.
- **Vector Spaces**
  - Represents the mathematical object vector spaces, and allow for tests like whether a vector is contained
- **Matrix Generator**:
  - A utility class (`MatrixBank`) for generating identity, diagonal, invertible, singular, rectangular, sparse, and test matrices.

## AI Usage

JUnit tests and MatrixBank class are written by AI.

## Usage Example

```java
MatrixBank bank = new MatrixBank();
RowReducer reducer = new RowReducer();
MatrixCalculator calc = new MatrixCalculator();

Matrix m = bank.invertible(3);
Matrix n = reducer.inverse(m);
Matrix I = calc.multiply(n, m);

System.out.println(I); // Should be identity matrix
