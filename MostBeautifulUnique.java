/*
A string s is said to be unique if no two characters of s are same.
A string s1 is producible from s2 by removing some of the characters from s2.
A string s1 is said to be more beautiful than s2 if length of s1 is more than s2 or if both have same length and s1 is lexicographically greater than s2( ex: ba is more beautiful than ab)
TestCase
1)
Input: babab

Output: ba
2)
Input:
nlhthgrfdnnlprjtecpdrthigjoqdejsfkasoctjijaoebqlrgaiakfsbljmpibkidjsrtkgrdnqsknbarpabgokbsrfhmeklrle

output: tsocrpkijgdqnbafhmle
*/

import java.io.*;
import java.util.Hashtable;
public class MostBeautifulUnique {
	public static void putMeAtMyPlace(StringBuilder strB,String str,int pos,Hashtable<Integer,Integer> hash){
		if(strB.length() < 2)
			return;
		if(strB.charAt(strB.length()-2) < strB.charAt(strB.length()-1)){//Remove it if it exists later in the string
			int i = str.indexOf(strB.charAt(strB.length()-2), pos+1);
			if(i != -1){//Remove it and Recursive look further
				hash.remove((int)strB.charAt(strB.length()-2));
				strB.deleteCharAt(strB.length()-2);
				putMeAtMyPlace(strB,str,pos,hash);
				return;
			}
		}
	}
	public static void main(String[] args) throws IOException {
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream("C:\\Users\\Rushi\\Desktop\\MultiplyExceptSelf_testcases\\input01.txt")));
		String line = br.readLine();
		Integer N = line.length();
		StringBuilder strB = new StringBuilder();
		Hashtable<Integer,Integer> hash = new Hashtable<Integer,Integer>();
		for(int i=0;i< N;i++){
			if(!hash.containsKey((int)line.charAt(i))){
				hash.put((int)line.charAt(i),(int)line.charAt(i));
				strB.append(line.charAt(i));
				putMeAtMyPlace(strB, line, i,hash);
				System.out.println(strB.toString());
			}
		}
		System.out.println(strB.toString());
	}
}

