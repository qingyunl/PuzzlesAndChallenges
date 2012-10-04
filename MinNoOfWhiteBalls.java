/*
There are "C" containers and "B" black balls given and a Probability "P" given
Minimize the number of white balls required such that it satisfies the following conditions:-
1. Each container should atleast contain 1 ball
2. If a ball is randomly picked from a randomly chosen container, the Probability of fetching white ball should be atleast "P".
*/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.text.html.MinimalHTMLWriter;

public class MinNoOfWhiteBalls {

	public static int findMinNoOfWhiteBalls(int C,int B, int P){
		Integer minWhiteBallsOption1 = 0;
		if(P == 100){
			if(B == 0){
				return C;
			}else{
				return Integer.MAX_VALUE;
			}
		}
		if(B < C){
			minWhiteBallsOption1 = (C-B);
		}
		int halfFirst = (int)(P*C/100.00);
		double mid = ((P/100.00) - (1.00/C)*halfFirst)*(1.00/C);
		int halfTwo = (int) Math.ceil((mid*B/(1-mid)));
		
		return (int) Math.max( halfFirst + halfTwo , minWhiteBallsOption1);
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream("C:\\Users\\Rushi\\Desktop\\Probability_testcases\\input01.txt")));
		String line = br.readLine();
		int T = Integer.parseInt(line);
		int count =0;
		while(count < T){
			line = br.readLine();
			String[] CBP = line.split(" ");
			Integer C,B,P;
			C = Integer.parseInt(CBP[0]);
			B = Integer.parseInt(CBP[1]);
			P = Integer.parseInt(CBP[2]);
			System.out.println(findMinNoOfWhiteBalls(C, B, P));
			count++;
		}

	}

}
