/*
Traverses the matrix in clockwise spiral manner
*/
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class matrixTraversal {

	public static void printMatrix(String[][] mat,int s_r,int e_r, int s_c, int e_c){
		if(s_r == e_r){ //Forward or Backward
			if(s_c > e_c){
				for(int i=s_c;i>=e_c;i--){
					System.out.print(mat[s_r][i]);
					System.out.print(' ');
				}
			}else{
				for(int i=s_c;i<=e_c;i++){
					System.out.print(mat[s_r][i]);
					System.out.print(' ');
				}
			}
		}else{ //Up or Down
			if(s_r > e_r){
				for(int i=s_r;i>=e_r;i--){
					System.out.print(mat[i][s_c]);
					System.out.print(' ');
				}
			}else{
				for(int i=s_r;i<=e_r;i++){
					System.out.print(mat[i][s_c]);
					System.out.print(' ');
				}
			}

		}
	}

	public static void traverseSpiral(String[][] mat,int row,int col){
		int s_r,s_c,e_r,e_c;
		s_c = 0;
		s_r = 0;
		e_c = col - 1;
		e_r = row - 1;

		while(!((s_r > e_r) && (s_c > e_c))){
			//Forward
			if(s_c>e_c)
				break;
			printMatrix(mat, s_r, s_r, s_c, e_c);
			//Downward
			if((s_r+1)>e_r)
				break;
			printMatrix(mat, s_r+1, e_r, e_c, e_c);
			//Backward
			if(s_c>(e_c-1))
				break;
			printMatrix(mat, e_r, e_r, e_c-1, s_c);
			//Upward
			if((s_r+1)>(e_r-1))
				break;
			printMatrix(mat, e_r-1, s_r+1, s_c, s_c);

			//Change the values
			s_r++;
			e_r--;
			s_c++;
			e_c--;
		}
	}

	public static String[][] buildMatrix(String[] matrixElements,int row,int col){
		String[][] mat = new String[row][col];
		int count = 0;
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				mat[i][j] = matrixElements[count];
				count++;
			}
		}
		return mat;
	}
	public static void main(String[] args) throws IOException{
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		//BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Rushi\\Desktop\\Interview Street Test Cases\\CodeEval\\MatrixTravers\\1.txt"));
		while(true){
			String line = br.readLine();
			if(line == null)
				break;
			String[] matrixChallenge = line.split(";");
			int rows = Integer.parseInt(matrixChallenge[0]);
			int columns = Integer.parseInt(matrixChallenge[1]);
			String[] matrixElements = matrixChallenge[2].split(" ");
			String[][] mat = buildMatrix(matrixElements,rows,columns);
			traverseSpiral(mat, rows, columns);
			System.out.print("\n");
		}
		
		//int[][] mat = {{1,2,3},{4,5,6},{7,8,9}};//,{9,10,11,12},{13,14,15,16}};
		//int[][] mat = {{1,2,3,4,5},{6,7,8,9,10},{11,12,13,14,15},{16,17,18,19,20}};
		//int[][] mat = {{1,2,3},{6,7,8}};
		
	}
}
