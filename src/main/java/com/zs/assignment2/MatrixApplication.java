import java.util.Scanner;

public class MatrixApplication{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //Input for matrix A
        System.out.print("\nEnter the number of rows in matrix A: ");
        int r1 = sc.nextInt();
        System.out.print("Enter the number of columnns in matrix A: ");
        int c1 = sc.nextInt();
        Matrix A = new Matrix(r1, c1);
        System.out.println("Enter the matrix A: ");
        A.input(sc);
        //Input for matrix B
        System.out.print("\nEnter the number of rows in matrix B: ");
        int r2 = sc.nextInt();
        System.out.print("Enter the number of columnns in matrix B: ");
        int c2 = sc.nextInt();
        Matrix B = new Matrix(r2, c2);
        System.out.println("Enter the matrix B: ");
        B.input(sc);
        //Input for the scalar
        System.out.print("Enter the Scalar Values : ");
        int k = sc.nextInt();
        //Addition
        try {
            System.out.println("A+B : ");
            Matrix add = A.matrixAdd(B);
            add.display();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        //Subtraction
        try {
            System.out.println("A-B : ");
            Matrix sub = A.matrixSubtract(B);
            sub.display();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        //Multiply
        try {
            System.out.println("A*B : ");
            Matrix multi = A.matrixMultiply(B);
            multi.display();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        //Transpose
        System.out.println("Transpose of A is : ");
        Matrix ATranspose = A.matrixTranspose();
        ATranspose.display();

        //scalarmultiply
        System.out.println("Scalar k * A : ");
        Matrix scalar = A.matrixScalarMultiply(k);
        scalar.display();

        sc.close();


    }

}
