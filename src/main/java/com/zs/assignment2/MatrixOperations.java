import java.util.Scanner;
class Matrix{
    private int rows;
    private int cols;
    private int[][] data;
    public Matrix(int rows,int cols){
        this.rows=rows;
        this.cols=cols;
        data=new int[rows][cols];
    }
    public void input(Scanner sc){
        System.out.println("Enter " + (rows*cols) + " elements:");
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                data[i][j]=sc.nextInt();
            }
        }
    }
    public void display(){
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                System.out.print(data[i][j]+"\t");
            }
            System.out.println();
        }
    }
    public Matrix MatrixAdd(Matrix other){
        if(rows!=other.rows || cols!=other.cols){
            throw new IllegalArgumentException("Addition not possible dimesions must be same for both of the matrix for addition");
        }
        Matrix result=new Matrix(rows,cols);
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                result.data[i][j]=data[i][j]+other.data[i][j];
            }
        }

        return result;
    }
    public Matrix MatrixSubtract(Matrix other){
        if(rows!=other.rows || cols!=other.cols){
            throw new IllegalArgumentException("Addition not possible dimesions must be same for both of the matrix for addition");
        }
        Matrix result=new Matrix(rows,cols);
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                result.data[i][j]=data[i][j]-other.data[i][j];
            }
        }

        return result;
    }
    public Matrix MatrixMutiply(Matrix other) {
        if (cols != other.rows) {
            throw new IllegalArgumentException("Multiplication not possible");
        }
        Matrix result = new Matrix(rows, other.cols);


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                for (int k = 0; k < cols; k++) {
                    result.data[i][j] += data[i][k] * other.data[k][j];
                }
            }
        }
        return result;
    }
 public Matrix MatrixScalarMultiply(int scalar){
        Matrix result=new Matrix(rows,cols);
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                result.data[i][j]=scalar*data[i][j];
            }
        }
return result;

 }
public Matrix MatrixTranspose(){
        Matrix result=new Matrix(rows,cols);
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                result.data[j][i]=data[i][j];
            }
        }
        return result;


}
}
public class MatrixOperations{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        //Input for matrix A
        System.out.print("\nEnter the number of rows in matrix A: ");
        int r1=sc.nextInt();
        System.out.print("Enter the number of columne in matrix A: ");
        int c1=sc.nextInt();
        Matrix A=new Matrix(r1,c1);
        System.out.println("Enter the matrix A: ");
        A.input(sc);
        //Input for matrix B
        System.out.print("\nEnter the number of rows in matrix B: ");
        int r2=sc.nextInt();
        System.out.print("Enter the number of columne in matrix B: ");
        int c2=sc.nextInt();
        Matrix B=new Matrix(r1,c1);
        System.out.println("Enter the matrix B: ");
        B.input(sc);
        //Input for the scalar
        System.out.print("Enter the Scalar Values : ");
        int k=sc.nextInt();
        //Addition
        try{
            System.out.println("A+B : ");
            Matrix add=A.MatrixAdd(B);
            add.display();
        }
        catch(Exception e)
        {   System.out.println(e.getMessage());

        }
    //Subtraction
        try{
            System.out.println("A-B : ");
            Matrix sub=A.MatrixSubtract(B);
            sub.display();
        }
        catch(Exception e)
        {   System.out.println(e.getMessage());

        }
        //Multiply
        try{
            System.out.println("A*B : ");
            Matrix multi=A.MatrixMutiply(B);
            multi.display();
        }
        catch(Exception e)
        {   System.out.println(e.getMessage());

        }
        //Transpose
       System.out.println("Transpose of A is : ");
        Matrix ATranspose=A.MatrixTranspose();
        ATranspose.display();

        //scalarmultiply
        System.out.println("Scalar k * A : ");
        Matrix scalar=A.MatrixScalarMultiply(k);
        scalar.display();

        sc.close();


    }

}