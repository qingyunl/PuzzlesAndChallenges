package com.Amazon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/*
method "getLargestSum" finds the largest contigous sum of elements using
Kadane algo
*/
public class largest_sum {

	public static int getLargestSum(int[] list){
		if(list.length == 0)
			return 0;
		int currentSum = list[0];
		int maxSum = list[0];
		for(int i=1;i<list.length;i++){
			currentSum = Math.max(currentSum + list[i], list[i]);
			maxSum = Math.max(maxSum,currentSum);
		}
		return maxSum;
	}
	public static int[] buildList(String[] listElements){
		int len = listElements.length;
		int[] list = new int[len];
		for(int i=0;i<len;i++){
			list[i] = Integer.parseInt(listElements[i].trim());
		}
		return list;
	}
	public static void main(String[] args) throws IOException {
		//BufferedReader br = new BufferedReader(new FileReader(args[0]));
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Rushi\\Desktop\\Interview Street Test Cases\\CodeEval\\largest_sum\\1.txt"));
		while(true){
			String line = br.readLine();
			if(line == null)
				break;
			String[] listElements = line.split(",");
			int[] list = buildList(listElements);
			System.out.println(getLargestSum(list));
		}

	}

}
