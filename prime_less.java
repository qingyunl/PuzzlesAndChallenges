/*
Prints the prime number that are less than given number
Expects input file containing input in format:
1100
1300
--i.e. Each line contains number for which all primes less that that are required to print
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//Sample code to read in test cases:
public class prime_less {
    public static Boolean isPrime(int N){
        if(N <= 1)
            return false;
        if( N % 2 == 0 )
            return false;
        double sqrt = Math.sqrt(N);
        for(int i=3;i<=sqrt;i++){
            if((N % i) == 0)
                return false;
        }
        return true;
    }
    public static void printPrime(int num){
        if(num >= 2){
            System.out.print(2);
        }else{
            return;
        }
        for(int i=3;i<num;i+=2){
            if(isPrime(i)){
                System.out.print(",");
                System.out.print(i);
            }
        }
        return;
    }
    public static void main (String[] args) throws NumberFormatException, IOException {
    
    //File file = new File(args[0]);
    //BufferedReader in = new BufferedReader(new FileReader(file));
    BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Rushi\\Desktop\\Interview Street Test Cases\\CodeEval\\prime_less\\1.txt"));
    String line;
    while ((line = in.readLine()) != null) {
        String[] lineArray = line.split("\n");
        if (lineArray.length > 0) {
            printPrime(Integer.parseInt(lineArray[0]));
            System.out.println("");
        }
    }
  }
}

