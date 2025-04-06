# Java Linear Algebra Library

Linear algebra library built using object-oriented principles. Created for educational and practical purposes.

## Functionality

- **Row Reduction**:
  - Converts matrices to row echelon form using a sequence of elementary row operations.
- **Inverse Calculation**:
  - Computes inverse of square matrices using row reduction and elementary matrices.
- **Vector Spaces**
  - Represents the mathematical object vector spaces, and allow for tests like whether a vector is contained in the space
- **Projection**
  - Projects vectors onto other vectors or vector spaces
  - Creates orthogonal or orthonormal bases
  - Finds projection matrix of a vector space

## Simple Features in Implementation

- **Matrix Representation**: Flexible matrix interface with multiple implementations (standard, transposed, elementary).
- **Vector Representation**: Vector class with operations like scaling, addition, subtraction, and normalization.
- **Vector Operations**:
  - Inner product
  - Angle between vectors
  - Cross product for 3D vectors

- **Matrix Operations**:
  - Matrix multiplication
  - Matrix transposition
  - Matrix addition and subtraction
  - Scalar multiplication

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
